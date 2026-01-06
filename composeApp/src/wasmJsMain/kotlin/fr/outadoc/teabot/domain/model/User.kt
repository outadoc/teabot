package fr.outadoc.teabot.domain.model

import kotlinx.collections.immutable.PersistentList

data class User(
    val userId: String,
    val userName: String,
    val teas: PersistentList<Tea>,
)
