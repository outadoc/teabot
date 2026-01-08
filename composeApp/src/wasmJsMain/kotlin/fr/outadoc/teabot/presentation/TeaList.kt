package fr.outadoc.teabot.presentation

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.help_button_cd
import fr.outadoc.teabot.generated.list_title
import fr.outadoc.teabot.generated.search_clear_cd
import fr.outadoc.teabot.generated.search_hint
import fr.outadoc.teabot.presentation.model.UiTea
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaList(
    teaList: ImmutableList<UiTea>,
    selectedTea: UiTea?,
    query: String = "",
    modifier: Modifier = Modifier,
    onSelect: (teaId: String) -> Unit = {},
    onArchivedChange: (teaId: String, Boolean) -> Unit = { _, _ -> },
    onQueryChange: (String) -> Unit = {},
    onHelpVisibilityChange: (Boolean) -> Unit = {},
    scrollState: LazyListState = rememberLazyListState(),
) {
    LaunchedEffect(query) {
        if (query.isEmpty() && selectedTea != null) {
            teaList
                .indexOfFirst { tea -> tea == selectedTea }
                .takeIf { idx -> idx > 0 }
                ?.let { idx ->
                    scrollState.scrollToItem(idx)
                }
        }
    }

    Column(
        modifier = modifier,
    ) {
        TopAppBar(
            title = { Text(stringResource(Res.string.list_title)) },
            actions = {
                TooltipBox(
                    state = rememberTooltipState(),
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(
                            positioning = TooltipAnchorPosition.Below,
                            spacingBetweenTooltipAndAnchor = 8.dp,
                        ),
                    tooltip = {
                        PlainTooltip {
                            Text(stringResource(Res.string.help_button_cd))
                        }
                    },
                ) {
                    IconButton(
                        onClick = { onHelpVisibilityChange(true) },
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Help,
                            contentDescription = stringResource(Res.string.help_button_cd),
                        )
                    }
                }
            },
        )

        if (teaList.isEmpty() && query.isEmpty()) {
            EmptyList(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
            )
        } else {
            TextField(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                value = query,
                onValueChange = onQueryChange,
                label = {
                    Text(stringResource(Res.string.search_hint))
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") },
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = stringResource(Res.string.search_clear_cd),
                            )
                        }
                    }
                },
            )

            Row(
                horizontalArrangement = Arrangement.End,
            ) {
                LazyColumn(
                    modifier =
                        Modifier.weight(
                            weight = 1f,
                            fill = true,
                        ),
                    state = scrollState,
                ) {
                    items(
                        items = teaList,
                        key = { tea -> tea.teaId },
                    ) { tea ->
                        TeaItem(
                            modifier =
                                Modifier
                                    .animateItem()
                                    .clickable {
                                        onSelect(tea.teaId)
                                    },
                            tea = tea,
                            isSelected = tea == selectedTea,
                            onArchivedChange = { isArchived ->
                                onArchivedChange(tea.teaId, isArchived)
                            },
                        )
                    }
                }

                VerticalScrollbar(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .padding(end = 8.dp),
                    adapter = rememberScrollbarAdapter(scrollState),
                )
            }
        }
    }
}
