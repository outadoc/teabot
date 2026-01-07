package fr.outadoc.teabot.presentation

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.teabot.presentation.model.UiTea
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun SingleTea(
    tea: UiTea,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(tea.user.userName)
        },
        colors =
            if (!isSelected) {
                ListItemDefaults.colors()
            } else {
                ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                )
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
