package fr.outadoc.teabot.domain

import kotlinx.coroutines.flow.Flow

interface ChatSource {
    fun getMessages(channelUserName: String): Flow<Message>
}
