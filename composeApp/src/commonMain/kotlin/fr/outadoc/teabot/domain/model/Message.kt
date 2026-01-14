package fr.outadoc.teabot.domain.model

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class Message(
    val messageId: String,
    val sentAt: Instant,
    val text: String,
)
