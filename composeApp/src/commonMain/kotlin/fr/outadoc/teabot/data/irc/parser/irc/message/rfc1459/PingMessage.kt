package fr.outadoc.teabot.data.irc.parser.irc.message.rfc1459

import fr.outadoc.teabot.data.irc.parser.core.ICommand

object PingMessage : ICommand {
    override val command = "PING"
}
