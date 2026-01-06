package fr.outadoc.teabot.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

            LaunchedEffect(Unit) {
                viewModel.onStart()
            }

            Scaffold {
                Row {
                    Surface {
                        LazyColumn(
                            modifier =
                                Modifier
                                    .width(300.dp)
                                    .fillMaxHeight(),
                        ) {
                            items(state.users, key = { user -> user.userId }) { user ->
                                ListItem(
                                    modifier =
                                        Modifier.clickable {
                                            viewModel.onSelect(user)
                                        },
                                    headlineContent = {
                                        Text(user.userName)
                                    },
                                )
                            }
                        }
                    }

                    VerticalDivider()

                    Column(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .weight(1f, fill = true),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SelectionContainer {
                            LazyColumn(
                                modifier = Modifier.widthIn(max = 600.dp),
                                contentPadding = PaddingValues(16.dp),
                            ) {
                                items(
                                    state.users
                                        .firstOrNull()
                                        ?.teas
                                        ?.firstOrNull()
                                        ?.messages
                                        .orEmpty(),
                                    key = { message -> message.messageId },
                                ) { message ->
                                    Text(
                                        message.sentAt.toString(),
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
                }
            }
        }
    }
}
