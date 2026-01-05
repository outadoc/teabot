package fr.outadoc.teabot.presentation

import fr.outadoc.teabot.data.IrcChatSource
import fr.outadoc.teabot.domain.ChatSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val MainModule =
    module {
        single {
            HttpClient {
                install(WebSockets)
            }
        }

        single<ChatSource> { IrcChatSource(get()) }
        viewModel { MainViewModel(get()) }
    }
