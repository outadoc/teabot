package fr.outadoc.teabot.data.db

import Database
import KeyPath
import com.juul.indexeddb.external.IDBKey
import fr.outadoc.teabot.data.db.model.DbMessage
import fr.outadoc.teabot.data.db.model.DbTea
import fr.outadoc.teabot.data.db.model.DbUser
import fr.outadoc.teabot.data.irc.model.ChatMessage
import fr.outadoc.teabot.domain.model.Message
import fr.outadoc.teabot.domain.model.Tea
import fr.outadoc.teabot.domain.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import openDatabase
import kotlin.random.Random
import kotlin.time.Instant

@OptIn(ExperimentalWasmJsInterop::class)
class DbSource {
    private val mutex = Mutex()
    private var database: Database? = null
    private val refresh = MutableSharedFlow<Int>(replay = 1)

    suspend fun getOrCreateDb(): Database {
        database?.let { return it }
        mutex.withLock {
            database?.let { return it }
            return openDatabase(DB_NAME, 1) { database, oldVersion, newVersion ->
                if (oldVersion < 1) {
                    database.createObjectStore(STORE_USERS, KeyPath("user_id")).apply {
                        createIndex("user_id", KeyPath("user_id"), unique = false)
                    }
                }
            }.also { database = it }
        }
    }

    suspend fun saveMessage(message: ChatMessage) {
        getOrCreateDb().writeTransaction(STORE_USERS) {
            val store = objectStore(STORE_USERS)

            val newMessage =
                Message(
                    messageId = message.messageId,
                    sentAt = message.sentAt,
                    text = message.text,
                )

            val existingUser: User? =
                (store.get(IDBKey(message.userId)) as DbUser?)
                    ?.toDomain()

            val user: User =
                existingUser
                    ?: User(
                        userId = message.userId,
                        userName = message.userName,
                        teas = persistentListOf(),
                    )

            // If the last tea is unarchived, we'll add the message to it.
            // Otherwise, we'll prepare some new tea.
            val hotTea: Tea? =
                user.teas.firstOrNull()?.takeIf { tea -> !tea.isArchived }

            val updatedUser =
                user.copy(
                    teas =
                        if (hotTea == null) {
                            user.teas.add(
                                0,
                                Tea(
                                    isArchived = false,
                                    sentAt = message.sentAt,
                                    messages = persistentListOf(newMessage),
                                ),
                            )
                        } else {
                            user.teas
                                .removeAt(0)
                                .add(
                                    0,
                                    hotTea.copy(
                                        messages = hotTea.messages.add(newMessage),
                                    ),
                                )
                        },
                )

            store.put(updatedUser.toData())

            refresh()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(): Flow<ImmutableList<User>> =
        refresh
            .onStart { refresh() }
            .mapLatest {
                getOrCreateDb().transaction(STORE_USERS) {
                    objectStore(STORE_USERS)
                        .openCursor()
                        .map { it.value as DbUser }
                        .map { user -> user.toDomain() }
                        .toList()
                        .toPersistentList()
                }
            }.onEach {
                println("Reloading from db: ${it.count()} items")
            }

    suspend fun refresh() {
        refresh.emit(Random.nextInt())
    }

    private fun DbUser.toDomain(): User =
        User(
            userId = user_id,
            userName = user_name,
            teas =
                teas
                    .toList()
                    .map { tea -> tea.toDomain() }
                    .sortedByDescending { tea -> tea.sentAt }
                    .toPersistentList(),
        )

    private fun DbTea.toDomain(): Tea =
        Tea(
            sentAt = Instant.fromEpochMilliseconds(sent_at_ts),
            isArchived = is_archived,
            messages =
                messages
                    .toList()
                    .map { message -> message.toDomain() }
                    .sortedBy { message -> message.sentAt }
                    .toPersistentList(),
        )

    private fun DbMessage.toDomain(): Message =
        Message(
            messageId = message_id,
            sentAt = Instant.fromEpochMilliseconds(sent_at_ts),
            text = text,
        )

    private fun Message.toData(): DbMessage =
        jso<DbMessage>().apply {
            message_id = this@toData.messageId
            sent_at_ts = this@toData.sentAt.toEpochMilliseconds()
            text = this@toData.text
        }

    private fun Tea.toData(): DbTea =
        jso<DbTea>().apply {
            is_archived = this@toData.isArchived
            sent_at_ts = this@toData.sentAt.toEpochMilliseconds()
            messages =
                this@toData
                    .messages
                    .sortedBy { message -> message.sentAt }
                    .map { it.toData() }
                    .toJsArray()
        }

    private fun User.toData(): DbUser =
        jso<DbUser>().apply {
            user_id = this@toData.userId
            user_name = this@toData.userName
            teas =
                this@toData
                    .teas
                    .sortedByDescending { tea -> tea.sentAt }
                    .map { tea -> tea.toData() }
                    .toJsArray()
        }

    private companion object {
        const val DB_NAME = "teabot-db"
        const val STORE_USERS = "users"
    }
}
