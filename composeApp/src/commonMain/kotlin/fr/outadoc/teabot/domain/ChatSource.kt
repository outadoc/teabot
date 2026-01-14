package fr.outadoc.teabot.domain

import fr.outadoc.teabot.data.irc.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatSource {
    fun getMessages(channelUserName: String): Flow<ChatMessage>
}
