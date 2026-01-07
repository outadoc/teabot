package fr.outadoc.teabot.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.teabot.presentation.model.UiTea
import kotlinx.collections.immutable.ImmutableList

@Composable
fun TeaList(
    teaList: ImmutableList<UiTea>,
    selectedTea: UiTea?,
    modifier: Modifier = Modifier,
    onSelect: (UiTea) -> Unit = {},
    onArchivedChange: (UiTea, Boolean) -> Unit = { _, _ -> },
    scrollState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
    ) {
        items(
            items = teaList,
            key = { tea -> "${tea.user.userId}-${tea.sentAt}" },
        ) { tea ->
            TeaItem(
                modifier =
                    Modifier
                        .animateItem()
                        .clickable { onSelect(tea) },
                tea = tea,
                isSelected = tea == selectedTea,
                onArchivedChange = { isArchived ->
                    onArchivedChange(tea, isArchived)
                },
            )
        }
    }
}
