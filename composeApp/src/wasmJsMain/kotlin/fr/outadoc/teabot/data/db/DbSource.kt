package fr.outadoc.teabot.data.db

import Database
import KeyPath
import com.juul.indexeddb.external.IDBKey
import fr.outadoc.teabot.data.db.model.DbMessage
import fr.outadoc.teabot.data.db.model.DbTea
import fr.outadoc.teabot.data.irc.model.ChatMessage
import fr.outadoc.teabot.domain.model.Message
import fr.outadoc.teabot.domain.model.Tea
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalWasmJsInterop::class, ExperimentalUuidApi::class)
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
                    database.createObjectStore(STORE_TEA, KeyPath("tea_id")).apply {
                        createIndex("tea_id", KeyPath("tea_id"), unique = true)
                        createIndex("user_id", KeyPath("user_id"), unique = false)
                        createIndex("sent_at_ts", KeyPath("sent_at_ts"), unique = false)
                    }
                }
            }.also { database = it }
        }
    }

    suspend fun saveMessage(message: ChatMessage) {
        getOrCreateDb().writeTransaction(STORE_TEA) {
            val newMessage =
                Message(
                    messageId = message.messageId,
                    sentAt = message.sentAt,
                    text = message.text,
                )

            val store = objectStore(STORE_TEA)
            val existingTea: List<Tea> =
                store
                    .index("user_id")
                    .openCursor(IDBKey(message.userId), autoContinue = true)
                    .map { row -> (row.value as DbTea).toDomain() }
                    .toList()
                    .sortedByDescending { tea -> tea.sentAt }

            // If the last tea is unarchived, we'll add the message to it.
            // Otherwise, we'll prepare some new tea.
            val hotTea: Tea =
                existingTea
                    .firstOrNull()
                    ?.takeIf { tea -> !tea.isArchived }
                    ?.let { latestTea ->
                        latestTea.copy(
                            messages =
                                latestTea.messages
                                    .removeAll { it.messageId == newMessage.messageId }
                                    .add(newMessage),
                        )
                    }
                    ?: Tea(
                        teaId = Uuid.random().toHexString(),
                        userId = message.userId,
                        userName = message.userName,
                        isArchived = false,
                        sentAt = message.sentAt,
                        messages = persistentListOf(newMessage),
                    )

            store.put(hotTea.toData())

            refresh()
        }
    }

    suspend fun setTeaArchived(
        teaId: String,
        isArchived: Boolean,
    ) {
        getOrCreateDb().writeTransaction(STORE_TEA) {
            val store = objectStore(STORE_TEA)

            val tea: DbTea? =
                store.get(IDBKey(teaId)) as DbTea?

            checkNotNull(tea) {
                "This tea was not found in the database"
            }

            tea.is_archived = isArchived

            store.put(tea)

            refresh()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(): Flow<ImmutableList<Tea>> =
        refresh
            .onStart { refresh() }
            .mapLatest {
                getOrCreateDb().transaction(STORE_TEA) {
                    objectStore(STORE_TEA)
                        .openCursor(autoContinue = true)
                        .map { row -> (row.value as DbTea).toDomain() }
                        .toList()
                        .sortedByDescending { tea -> tea.sentAt }
                        .toPersistentList()
                }
            }.onEach {
                println("Reloading from db: ${it.count()} items")
            }

    suspend fun refresh() {
        refresh.emit(Random.nextInt())
    }

    private fun DbTea.toDomain(): Tea =
        Tea(
            teaId = tea_id,
            userId = user_id,
            userName = user_name,
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
            tea_id = this@toData.teaId
            user_id = this@toData.userId
            user_name = this@toData.userName
            is_archived = this@toData.isArchived
            sent_at_ts = this@toData.sentAt.toEpochMilliseconds()
            messages =
                this@toData
                    .messages
                    .sortedBy { message -> message.sentAt }
                    .map { it.toData() }
                    .toJsArray()
        }

    private companion object {
        const val DB_NAME = "teabot-db"
        const val STORE_TEA = "tea"
    }
}
