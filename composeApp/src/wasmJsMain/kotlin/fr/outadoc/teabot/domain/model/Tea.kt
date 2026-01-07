package fr.outadoc.teabot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import kotlin.time.Instant

@Immutable
data class Tea(
    val teaId: String,
    val userId: String,
    val userName: String,
    val sentAt: Instant,
    val isArchived: Boolean,
    val messages: PersistentList<Message>,
)
