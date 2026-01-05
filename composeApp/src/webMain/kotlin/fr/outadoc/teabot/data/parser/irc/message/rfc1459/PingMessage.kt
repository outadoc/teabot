package fr.outadoc.teabot.data.parser.irc.message.rfc1459

import fr.outadoc.teabot.data.parser.core.ICommand

object PingMessage : ICommand {
    override val command = "PING"
}
