package fr.outadoc.teabot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.outadoc.teabot.parser.irc.message.IrcMessageParser
import fr.outadoc.teabot.parser.irc.message.rfc1459.PingMessage
import fr.outadoc.teabot.parser.irc.message.rfc1459.PrivMsgMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(private val client: HttpClient) : ViewModel() {

    fun onStart() {
        viewModelScope.launch {
            client.webSocket("wss://irc-ws.chat.twitch.tv:443") {
                send("NICK justinfan${Random.nextInt(1000, 10_000)}")
                send("CAP REQ :twitch.tv/tags")
                send("JOIN #${AppConstants.CHANNEL_USERNAME}")

                while (isActive) {
                    when (val message = incoming.receive()) {
                        is Frame.Text -> {
                            val lines = message.readText().lines()
                            lines.forEach { line ->
                                when (val message = parse(line)) {
                                    is Message.Ping -> {
                                        send("PONG :tmi.twitch.tv")
                                    }

                                    is Message.PrivMsg -> {
                                        println(message.toString())
                                    }

                                    null -> {}
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    sealed interface Message {
        data object Ping : Message
        data class PrivMsg(
            val userId: String,
            val userName: String,
            val messageId: String,
            val message: String,
        ) : Message
    }

    private fun parse(line: String): Message? {
        val parsed = IrcMessageParser.parse(line)
        return when (parsed?.command) {
            PingMessage.command -> {
                Message.Ping
            }

            PrivMsgMessage.command -> {
                val msg: PrivMsgMessage.Message =
                    PrivMsgMessage.Message.Parser.parse(parsed)
                        ?: return null

                Message.PrivMsg(
                    userId = parsed.tags["user-id"] ?: return null,
                    userName = parsed.tags["display-name"] ?: return null,
                    messageId = parsed.tags["id"] ?: return null,
                    message = msg.message,
                )
            }

            else -> null
        }
    }
}
