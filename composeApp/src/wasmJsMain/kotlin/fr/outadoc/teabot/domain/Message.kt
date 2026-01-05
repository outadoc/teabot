package fr.outadoc.teabot.domain

import kotlin.time.Instant

data class Message(
    val userId: String,
    val userName: String,
    val messageId: String,
    val sentAt: Instant,
    val text: String,
)
