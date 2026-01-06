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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import openDatabase
import kotlin.time.Instant

@OptIn(ExperimentalWasmJsInterop::class)
class DbSource {
    private val mutex = Mutex()
    private var database: Database? = null

    private val additions = MutableSharedFlow<DbMessage>()

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

            val existingUser: DbUser? =
                store.get(IDBKey(message.userId)) as DbUser?

            val user: DbUser =
                existingUser
                    ?: jso<DbUser>().apply {
                        user_id = message.userId
                        user_name = message.userName
                        teas = JsArray()
                    }

            // Find latest unarchived tea
            val existingTea: DbTea? =
                user.teas
                    .toList()
                    .filter { tea -> !tea.is_archived }
                    .maxByOrNull { tea -> tea.sent_at_ts }

            val tea: DbTea =
                existingTea
                    ?: jso<DbTea>().apply {
                        is_archived = false
                        sent_at_ts = message.sentAt.toEpochMilliseconds()
                        messages = JsArray()
                    }

            val newMessage: DbMessage =
                jso<DbMessage>().apply {
                    message_id = message.messageId
                    sent_at_ts = message.sentAt.toEpochMilliseconds()
                    text = message.text
                }

            tea.messages =
                tea.messages
                    .toList()
                    .toPersistentList()
                    .add(newMessage)
                    .toJsArray()

            additions.emit(message)

            store.add(newMessage)
        }
    }

    fun getAll(): Flow<ImmutableList<User>> =
        flow {
            var list =
                getOrCreateDb().transaction(STORE_USERS) {
                    objectStore(STORE_USERS)
                        .index("sent_at_iso")
                        .openCursor()
                        .map { it.value as DbUser }
                        .map { user -> user.toDomain() }
                        .toList()
                        .toPersistentList()
                }

            emit(list)

            additions.collect { newMessage ->
                list = list.add(newMessage)
                emit(list)
            }
        }

    private fun DbUser.toDomain(): User =
        User(
            userId = user_id,
            userName = user_name,
            teas =
                teas
                    .toList()
                    .map { tea -> tea.toDomain() }
                    .toImmutableList(),
        )

    private fun DbTea.toDomain(): Tea =
        Tea(
            sentAt = Instant.fromEpochMilliseconds(sent_at_ts),
            isArchived = is_archived,
            messages =
                messages
                    .toList()
                    .map { message -> message.toDomain() }
                    .toImmutableList(),
        )

    private fun DbMessage.toDomain(): Message =
        Message(
            messageId = message_id,
            sentAt = Instant.fromEpochMilliseconds(sent_at_ts),
            text = text,
        )

    private companion object {
        const val DB_NAME = "teabot-db"
        const val STORE_USERS = "users"
    }
}
