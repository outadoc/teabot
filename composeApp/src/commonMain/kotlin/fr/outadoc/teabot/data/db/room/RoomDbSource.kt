package fr.outadoc.teabot.data.db.room

import fr.outadoc.teabot.data.irc.model.ChatMessage
import fr.outadoc.teabot.domain.DbSource
import fr.outadoc.teabot.domain.model.Tea
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class RoomDbSource(
    private val dao: TeaMessageDao,
) : DbSource {
    override suspend fun saveMessage(message: ChatMessage) {
        TODO("Not yet implemented")
    }

    override suspend fun setTeaArchived(
        teaId: String,
        isArchived: Boolean,
    ) {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<ImmutableList<Tea>> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        // No need for manual refresh, flow gets updated by Room
    }
}
