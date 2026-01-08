package fr.outadoc.teabot.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.search_clear_cd
import fr.outadoc.teabot.generated.search_hint
import fr.outadoc.teabot.presentation.model.UiTea
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
fun TeaList(
    teaList: ImmutableList<UiTea>,
    selectedTea: UiTea?,
    query: String = "",
    modifier: Modifier = Modifier,
    onSelect: (teaId: String) -> Unit = {},
    onArchivedChange: (teaId: String, Boolean) -> Unit = { _, _ -> },
    onQueryChange: (String) -> Unit = {},
    scrollState: LazyListState = rememberLazyListState(),
) {
    Column(
        modifier = modifier,
    ) {
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

        LazyColumn(
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
    }
}
