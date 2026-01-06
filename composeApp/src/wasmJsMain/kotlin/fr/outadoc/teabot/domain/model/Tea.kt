package fr.outadoc.teabot.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlin.time.Instant

data class Tea(
    val sentAt: Instant,
    val isArchived: Boolean,
    val messages: ImmutableList<Message>,
)
