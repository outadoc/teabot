package fr.outadoc.teabot.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlin.time.Instant

@Immutable
data class UiTea(
    val sentAt: Instant,
    val isArchived: Boolean,
    val user: UiUser,
    val messages: ImmutableList<UiMessage>,
)
