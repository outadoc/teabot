package fr.outadoc.teabot.presentation.model

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class UiMessage(
    val messageId: String,
    val sentAt: Instant,
    val text: String,
)
