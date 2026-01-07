package fr.outadoc.teabot.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.teabot.presentation.model.UiTea
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TeaList(
    teaList: ImmutableList<UiTea>,
    modifier: Modifier = Modifier,
    onSelect: (UiTea) -> Unit = {},
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
            ListItem(
                modifier = Modifier.clickable { onSelect(tea) },
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
}
