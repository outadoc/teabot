package fr.outadoc.teabot.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication({
        modules(MainModule)
    }) {
        MaterialTheme(
            colorScheme =
                if (isSystemInDarkTheme()) {
                    darkColorScheme()
                } else {
                    lightColorScheme()
                },
        ) {
            val viewModel: MainViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            Scaffold {
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

                    LazyColumn {
                        items(state.users) { item ->
                            Text(item.toString())
                        }
                    }
                }
            }
        }
    }
}
