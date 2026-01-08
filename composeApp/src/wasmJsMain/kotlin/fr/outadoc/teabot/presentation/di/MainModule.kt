package fr.outadoc.teabot.presentation.di

import fr.outadoc.teabot.AppConfig
import fr.outadoc.teabot.data.db.DbSource
import fr.outadoc.teabot.data.irc.IrcChatSource
import fr.outadoc.teabot.domain.ChatSource
import fr.outadoc.teabot.presentation.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val MainModule =
    module {
        single {
            AppConfig(
                broadcasterUsername = "antoinedaniel",
                displayPrefix = "!thé",
                matchedPrefixes = emptySet(),
            )
        }

//        single {
//            AppConfig(
//                broadcasterUsername = "outadoc",
//                displayPrefix = "!thé",
//                allPrefixes = setOf("!the", "!thé"),
//            )
//        }

        single {
            HttpClient {
                install(WebSockets) {
                    pingIntervalMillis = 10_000
                }
            }
        }

        single<ChatSource> { IrcChatSource(get()) }
        single<DbSource> { DbSource() }
        viewModel { MainViewModel(get(), get(), get()) }
    }
