package fr.outadoc.teabot.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlin.time.Instant

@Immutable
data class UiTea(
    val user: UiUser,
    val sentAt: Instant,
    val isArchived: Boolean,
    val messages: ImmutableList<UiMessage>,
)
