package fr.outadoc.teabot.data.db

import Database
import KeyPath
import fr.outadoc.teabot.domain.Message
import kotlinx.collections.immutable.ImmutableList
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

class DbSource {
    private val mutex = Mutex()
    private var database: Database? = null

    private val additions = MutableSharedFlow<Message>()

    suspend fun getOrCreateDb(): Database {
        database?.let { return it }
        mutex.withLock {
            database?.let { return it }
            return openDatabase(DB_NAME, 1) { database, oldVersion, newVersion ->
                if (oldVersion < 1) {
                    val store = database.createObjectStore(STORE_MESSAGES, KeyPath("message_id"))
                    store.createIndex("message_id", KeyPath("message_id"), unique = true)
                    store.createIndex("sent_at_iso", KeyPath("sent_at_iso"), unique = false)
                    store.createIndex("user_id", KeyPath("user_id"), unique = false)
                }
            }.also { database = it }
        }
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    suspend fun saveMessage(message: Message) {
        val db = getOrCreateDb()
        db.writeTransaction(STORE_MESSAGES) {
            val store = objectStore(STORE_MESSAGES)
            val newMessage =
                jso<JsonMessage>().apply {
                    message_id = message.messageId
                    user_id = message.userId
                    user_name = message.userName
                    sent_at_iso = message.sentAt.toString()
                    text = message.text
                }

            additions.emit(message)
            store.add(newMessage)
        }
    }

    fun getAll(): Flow<ImmutableList<Message>> =
        flow {
            var list =
                getOrCreateDb().transaction(STORE_MESSAGES) {
                    objectStore(STORE_MESSAGES)
                        .index("sent_at_iso")
                        .openCursor()
                        .map { it.value as JsonMessage }
                        .map {
                            Message(
                                userId = it.user_id,
                                userName = it.user_name,
                                messageId = it.message_id,
                                sentAt = Instant.parse(it.sent_at_iso),
                                text = it.text,
                            )
                        }.toList()
                        .toPersistentList()
                }

            emit(list)

            additions.collect { newMessage ->
                list = list.add(newMessage)
                emit(list)
            }
        }

    private companion object {
        const val DB_NAME = "teabot-db"
        const val STORE_MESSAGES = "messages"
    }
}
