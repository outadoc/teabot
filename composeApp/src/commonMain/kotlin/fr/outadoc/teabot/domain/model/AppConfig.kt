package fr.outadoc.teabot.domain.model

data class AppConfig(
    val broadcasterUsername: String,
    val displayPrefix: String,
    val matchedPrefixes: Set<String>,
)
