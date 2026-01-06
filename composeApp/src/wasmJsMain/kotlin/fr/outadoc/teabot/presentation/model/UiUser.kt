package fr.outadoc.teabot.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiUser(
    val userId: String,
    val userName: String,
)
