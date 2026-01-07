package fr.outadoc.teabot.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import teabot.composeapp.generated.resources.Res
import teabot.composeapp.generated.resources.sip

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
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
            bitmap = imageResource(Res.drawable.sip),
            contentDescription = "Dino qui boit son petit thé",
        )

        Text(
            modifier = Modifier.widthIn(max = 400.dp),
            text = "Choisis un thé pour commencer",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun EmptyScreenPreview() {
    MaterialTheme {
        Surface {
            EmptyScreen()
        }
    }
}
