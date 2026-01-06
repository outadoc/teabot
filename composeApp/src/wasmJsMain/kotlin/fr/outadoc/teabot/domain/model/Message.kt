package fr.outadoc.teabot.domain.model

import kotlin.time.Instant

data class Message(
    val messageId: String,
    val sentAt: Instant,
    val text: String,
)
