package fr.outadoc.teabot.data

import fr.outadoc.teabot.domain.ChatSource
import fr.outadoc.teabot.domain.Message
import fr.outadoc.teabot.parser.irc.message.IrcMessageParser
import fr.outadoc.teabot.parser.irc.message.rfc1459.PingMessage
import fr.outadoc.teabot.parser.irc.message.rfc1459.PrivMsgMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.random.Random

class IrcChatSource(
    private val httpClient: HttpClient,
) : ChatSource {

    override fun getMessages(channelUserName: String): Flow<Message> = flow {
        httpClient.webSocket("wss://irc-ws.chat.twitch.tv:443") {
            send("NICK justinfan${Random.nextInt(1000, 10_000)}")
            send("CAP REQ :twitch.tv/tags")
            send("JOIN #${channelUserName}")

            while (isActive) {
                when (val message = incoming.receive()) {
                    is Frame.Text -> {
                        val lines = message.readText().lines()
                        lines.forEach { line ->
                            when (val message = parse(line)) {
                                is IrcMessage.Ping -> {
                                    send("PONG :tmi.twitch.tv")
                                }

                                is IrcMessage.PrivMsg -> {
                                    emit(
                                        Message(
                                            userId = message.userId,
                                            userName = message.userName,
                                            messageId = message.messageId,
                                            message = message.message,
                                        )
                                    )
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

    private fun parse(line: String): IrcMessage? {
        val parsed = IrcMessageParser.parse(line)
        return when (parsed?.command) {
            PingMessage.command -> {
                IrcMessage.Ping
            }

            PrivMsgMessage.command -> {
                val msg: PrivMsgMessage.Message =
                    PrivMsgMessage.Message.Parser.parse(parsed)
                        ?: return null

                IrcMessage.PrivMsg(
                    userId = parsed.tags["user-id"] ?: return null,
                    userName = parsed.tags["display-name"] ?: return null,
                    messageId = parsed.tags["id"] ?: return null,
                    message = msg.message,
                )
            }

            else -> null
        }
    }

    private sealed interface IrcMessage {
        data object Ping : IrcMessage
        data class PrivMsg(
            val userId: String,
            val userName: String,
            val messageId: String,
            val message: String,
        ) : IrcMessage
    }
}
