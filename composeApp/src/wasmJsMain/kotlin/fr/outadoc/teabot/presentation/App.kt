package fr.outadoc.teabot.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.teabot.presentation.di.MainModule
import fr.outadoc.teabot.presentation.theme.AppTheme
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication({
        modules(MainModule)
    }) {
        AppTheme {
            val viewModel: MainViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.onStart()
            }

            MainScreen(
                state = state,
                onSelect = viewModel::onSelect,
                onArchivedChange = viewModel::onArchivedChange,
                onQueryChange = viewModel::onQueryChange,
            )
        }
    }
}
