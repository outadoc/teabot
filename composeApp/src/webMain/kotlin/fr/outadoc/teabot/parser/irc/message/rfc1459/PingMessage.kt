package fr.outadoc.teabot.parser.irc.message.rfc1459

import fr.outadoc.teabot.parser.core.ICommand


object PingMessage : ICommand {
    override val command = "PING"
}
