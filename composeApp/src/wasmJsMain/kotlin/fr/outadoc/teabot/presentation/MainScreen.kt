package fr.outadoc.teabot.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    state: MainViewModel.State,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    onSelect: (teaId: String) -> Unit = {},
    onArchivedChange: (teaId: String, Boolean) -> Unit = { _, _ -> },
    onQueryChange: (String) -> Unit = {},
    onHelpVisibilityChange: (Boolean) -> Unit = {},
) {
    Scaffold(modifier = modifier) {
        if (state.isHelpDisplayed) {
            HelpDialog(
                onDismissRequest = { onHelpVisibilityChange(false) },
            )
        }

        Row {
            TeaList(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(300.dp),
                scrollState = scrollState,
                teaList = state.teaList,
                selectedTea = state.selectedTea,
                query = state.query,
                onSelect = onSelect,
                onArchivedChange = onArchivedChange,
                onQueryChange = onQueryChange,
                onHelpVisibilityChange = onHelpVisibilityChange,
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .weight(1f, fill = true)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val tea = state.selectedTea
                if (tea != null) {
                    TeaDetails(
                        tea = tea,
                    )
                } else {
                    EmptyScreen(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
