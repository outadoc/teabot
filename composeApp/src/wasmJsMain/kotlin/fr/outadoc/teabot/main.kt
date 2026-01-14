package fr.outadoc.teabot

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import fr.outadoc.teabot.presentation.App
import fr.outadoc.teabot.presentation.di.MainModule
import org.koin.compose.KoinApplication

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        KoinApplication({
            modules(MainModule)
        }) {
            App()
        }
    }
}
