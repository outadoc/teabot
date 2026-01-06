package fr.outadoc.teabot.domain.model

import kotlinx.collections.immutable.ImmutableList

data class User(
    val userId: String,
    val userName: String,
    val teas: ImmutableList<Tea>,
)
