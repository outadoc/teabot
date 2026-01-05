package fr.outadoc.teabot.data.db

import Database
import KeyPath
import fr.outadoc.teabot.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import openDatabase

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
                    text = message.text
                }

            additions.emit(message)
            store.add(newMessage)
        }
    }

    fun getAll(): Flow<List<Message>> =
        flow {
            var list = listOf<Message>()
            val db = getOrCreateDb()
            db.transaction(STORE_MESSAGES) {
                list =
                    objectStore(STORE_MESSAGES)
                        .openCursor()
                        .map { it.value as JsonMessage }
                        .map {
                            Message(
                                userId = it.user_id,
                                userName = it.user_name,
                                messageId = it.message_id,
                                text = it.text,
                            )
                        }.toList()

                emit(list)
            }

            additions.collect { newMessage ->
                list = list + newMessage
                emit(list)
            }
        }

    private companion object {
        const val DB_NAME = "teabot-db"
        const val STORE_MESSAGES = "messages"
    }
}
