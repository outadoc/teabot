package fr.outadoc.teabot.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.grumpy
import fr.outadoc.teabot.generated.list_empty_title
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                16.dp,
                Alignment.CenterVertically,
            ),
    ) {
        Image(
            bitmap = imageResource(Res.drawable.grumpy),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.widthIn(max = 400.dp),
            text = stringResource(Res.string.list_empty_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
    }
}
