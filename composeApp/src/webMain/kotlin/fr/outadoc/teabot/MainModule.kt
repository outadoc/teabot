package fr.outadoc.teabot

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val MainModule = module {
    single {
        HttpClient {
            install(WebSockets)
        }
    }

    viewModel { MainViewModel(get()) }
}
