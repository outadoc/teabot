package fr.outadoc.teabot.domain

data class Message(
    val userId: String,
    val userName: String,
    val messageId: String,
    val message: String,
)
