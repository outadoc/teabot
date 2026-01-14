package fr.outadoc.teabot.presentation.di

import fr.outadoc.teabot.data.db.indexeddb.IndexedDbSource
import fr.outadoc.teabot.data.irc.IrcChatSource
import fr.outadoc.teabot.domain.ChatSource
import fr.outadoc.teabot.domain.DbSource
import fr.outadoc.teabot.domain.model.AppConfig
import fr.outadoc.teabot.presentation.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val MainModule =
    module {
        single {
            AppConfig(
                broadcasterUsername = "angledroit",
                displayPrefix = "!thé",
                matchedPrefixes = setOf("!the", "!thé"),
            )
        }

        single {
            HttpClient {
                install(WebSockets) {
                    pingIntervalMillis = 10_000
                }
            }
        }

        single<ChatSource> { IrcChatSource(get()) }
        single<DbSource> { IndexedDbSource() }
        viewModel { MainViewModel(get(), get(), get()) }
    }
