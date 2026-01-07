package fr.outadoc.teabot.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.teabot.presentation.model.UiTea
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TeaDetails(
    tea: UiTea,
    modifier: Modifier = Modifier,
) {
    SelectionContainer(modifier = modifier) {
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
}
