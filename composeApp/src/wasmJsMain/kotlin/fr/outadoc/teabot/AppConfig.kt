package fr.outadoc.teabot

data class AppConfig(
    val broadcasterUsername: String,
    val displayPrefix: String,
    val matchedPrefixes: Set<String>,
)
