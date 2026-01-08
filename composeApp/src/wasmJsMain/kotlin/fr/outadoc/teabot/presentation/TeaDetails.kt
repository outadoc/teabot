package fr.outadoc.teabot.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.tea_title
import fr.outadoc.teabot.presentation.model.UiTea
import fr.outadoc.teabot.presentation.utils.localized
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

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
                item("header") {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = stringResource(Res.string.tea_title, tea.user.userName),
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }
                }

                items(
                    tea.messages,
                    key = { message -> message.messageId },
                ) { message ->
                    Column(
                        modifier = Modifier.animateItem(),
                    ) {
                        Text(
                            message.sentAt
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .localized(),
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
