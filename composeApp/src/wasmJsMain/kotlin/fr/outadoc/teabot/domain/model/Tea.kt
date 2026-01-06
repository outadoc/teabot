package fr.outadoc.teabot.domain.model

import kotlinx.collections.immutable.PersistentList
import kotlin.time.Instant

data class Tea(
    val sentAt: Instant,
    val isArchived: Boolean,
    val messages: PersistentList<Message>,
)
