package fr.outadoc.teabot.presentation

import androidx.compose.foundation.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.help_action_ok
import fr.outadoc.teabot.generated.help_description
import fr.outadoc.teabot.generated.help_icon_cd
import fr.outadoc.teabot.generated.help_title
import fr.outadoc.teabot.generated.think
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HelpDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(Res.string.help_title))
        },
        icon = {
            Image(
                imageResource(Res.drawable.think),
                contentDescription = stringResource(Res.string.help_icon_cd),
            )
        },
        text = {
            Text(stringResource(Res.string.help_description))
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(stringResource(Res.string.help_action_ok))
            }
        },
    )
}
