package fr.outadoc.teabot.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication({
        modules(MainModule)
    }) {
        MaterialTheme {
            val viewModel: MainViewModel = koinViewModel()

            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .safeContentPadding()
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { viewModel.onStart() }) {
                    Text("Start")
                }
            }
        }
    }
}
