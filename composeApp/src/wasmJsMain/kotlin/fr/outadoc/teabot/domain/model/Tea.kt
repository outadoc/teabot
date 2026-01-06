package fr.outadoc.teabot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import kotlin.time.Instant

@Immutable
data class Tea(
    val sentAt: Instant,
    val isArchived: Boolean,
    val messages: PersistentList<Message>,
)
