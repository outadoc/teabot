package fr.outadoc.teabot.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.list_mark_read
import fr.outadoc.teabot.generated.list_mark_unread
import fr.outadoc.teabot.presentation.model.UiTea
import fr.outadoc.teabot.presentation.utils.localized
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
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
                    TooltipBox(
                        state = rememberTooltipState(),
                        positionProvider =
                            TooltipDefaults.rememberTooltipPositionProvider(
                                positioning = TooltipAnchorPosition.End,
                                spacingBetweenTooltipAndAnchor = 8.dp,
                            ),
                        tooltip = {
                            PlainTooltip {
                                Text(stringResource(Res.string.list_mark_unread))
                            }
                        },
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = stringResource(Res.string.list_mark_unread),
                        )
                    }
                }
            } else {
                TooltipBox(
                    state = rememberTooltipState(),
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(
                            positioning = TooltipAnchorPosition.End,
                            spacingBetweenTooltipAndAnchor = 8.dp,
                        ),
                    tooltip = {
                        PlainTooltip {
                            Text(stringResource(Res.string.list_mark_read))
                        }
                    },
                ) {
                    IconButton(
                        onClick = { onArchivedChange(true) },
                    ) {
                        Icon(
                            Icons.Outlined.Circle,
                            contentDescription = stringResource(Res.string.list_mark_read),
                        )
                    }
                }
            }
        },
        headlineContent = {
            Text(
                tea.user.userName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
