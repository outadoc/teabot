package fr.outadoc.teabot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList

@Immutable
data class User(
    val userId: String,
    val userName: String,
    val teas: PersistentList<Tea>,
)
