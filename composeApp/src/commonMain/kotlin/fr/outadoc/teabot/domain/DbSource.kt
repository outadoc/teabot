package fr.outadoc.teabot.domain

import fr.outadoc.teabot.data.irc.model.ChatMessage
import fr.outadoc.teabot.domain.model.Tea
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface DbSource {
    suspend fun saveMessage(message: ChatMessage)

    suspend fun setTeaArchived(
        teaId: String,
        isArchived: Boolean,
    )

    fun getAll(): Flow<ImmutableList<Tea>>

    suspend fun refresh()
}
