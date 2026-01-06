package fr.outadoc.teabot.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import teabot.composeapp.generated.resources.Res
import teabot.composeapp.generated.resources.sip

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
            val scrollState = rememberLazyListState()

            LaunchedEffect(Unit) {
                viewModel.onStart()
            }

            Scaffold {
                Row {
                    LazyColumn(
                        modifier =
                            Modifier
                                .width(300.dp)
                                .fillMaxHeight(),
                        state = scrollState,
                    ) {
                        items(
                            state.teaList,
                            key = { tea -> "${tea.user.userId}-${tea.sentAt}" },
                        ) { tea ->
                            ListItem(
                                modifier =
                                    Modifier.clickable {
                                        viewModel.onSelect(tea)
                                    },
                                headlineContent = {
                                    Text(tea.user.userName)
                                },
                                supportingContent = {
                                    Text(
                                        tea.sentAt
                                            .toLocalDateTime(TimeZone.currentSystemDefault())
                                            .toString(),
                                    )
                                },
                            )
                        }
                    }

                    VerticalScrollbar(
                        modifier = Modifier.fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(scrollState),
                        style =
                            defaultScrollbarStyle().copy(
                                hoverColor = MaterialTheme.colorScheme.surfaceTint,
                                unhoverColor = MaterialTheme.colorScheme.surfaceTint,
                            ),
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
                            SelectionContainer {
                                Column(
                                    modifier = Modifier.widthIn(max = 600.dp),
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                    ) {
                                        items(
                                            tea.messages,
                                            key = { message -> message.messageId },
                                        ) { message ->
                                            Text(
                                                message.sentAt
                                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                                    .toString(),
                                                style = MaterialTheme.typography.labelSmall,
                                            )

                                            Text(
                                                message.text,
                                                style = MaterialTheme.typography.bodyLarge,
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement =
                                    Arrangement.spacedBy(
                                        16.dp,
                                        Alignment.CenterVertically,
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Image(
                                    bitmap = imageResource(Res.drawable.sip),
                                    contentDescription = "Dino qui boit son petit thé",
                                )

                                Text(
                                    modifier = Modifier.widthIn(max = 400.dp),
                                    text = "Choisis un thé pour commencer",
                                    style = MaterialTheme.typography.headlineLarge,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
