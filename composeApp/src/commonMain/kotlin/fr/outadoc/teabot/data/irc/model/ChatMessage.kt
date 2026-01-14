package fr.outadoc.teabot.data.irc.model

import kotlin.time.Instant

data class ChatMessage(
    val userId: String,
    val userName: String,
    val messageId: String,
    val sentAt: Instant,
    val text: String,
)
