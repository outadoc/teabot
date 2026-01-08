package fr.outadoc.teabot.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.teabot.presentation.model.UiTea
import fr.outadoc.teabot.presentation.utils.localized
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TeaItem(
    tea: UiTea,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onArchivedChange: (Boolean) -> Unit = {},
) {
    ListItem(
        modifier = modifier,
        colors =
            if (!isSelected) {
                ListItemDefaults.colors()
            } else {
                ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                )
            },
        trailingContent = {
            if (tea.isArchived) {
                IconButton(
                    onClick = { onArchivedChange(false) },
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Mark unread",
                    )
                }
            } else {
                IconButton(
                    onClick = { onArchivedChange(true) },
                ) {
                    Icon(
                        Icons.Outlined.Circle,
                        contentDescription = "Mark read",
                    )
                }
            }
        },
        headlineContent = {
            Text(tea.user.userName)
        },
        supportingContent = {
            Text(
                tea.sentAt
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .localized(),
            )
        },
    )
}
