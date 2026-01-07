package fr.outadoc.teabot

data class AppConfig(
    val broadcasterUsername: String,
    val messagePrefixes: Set<String>,
)
