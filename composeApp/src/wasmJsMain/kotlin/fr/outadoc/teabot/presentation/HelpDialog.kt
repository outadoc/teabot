package fr.outadoc.teabot.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.outadoc.teabot.AppConfig
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.comfy
import fr.outadoc.teabot.generated.help_action_ok
import fr.outadoc.teabot.generated.help_icon_cd
import fr.outadoc.teabot.generated.help_title
import fr.outadoc.teabot.generated.think
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun HelpDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
) {
    val appConfig = koinInject<AppConfig>()

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
            Text(
                buildAnnotatedString {
                    append("Bienvenue dans la théière non-officielle de la Pire Commu™ !")

                    append("\n\n")

                    append("Pour poster votre thé, postez-le simplement dans le tchat de la chaîne ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(appConfig.broadcasterUsername)
                    }
                    append(" en ajoutant ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(appConfig.displayPrefix)
                    }
                    append(" au début de votre message. ")

                    append("Pour un thé en plusieurs parties, commencez bien ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("chaque")
                    }
                    append(" message par ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(appConfig.displayPrefix)
                    }
                    append(".")

                    append("\n\n")

                    append("Attention, vos messages ne sont collectés dans la boîte à thé que lorsque le site est ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("ouvert")
                    }
                    append(" ! Attendez le signal de la streameuse.")

                    append("\n\n")

                    append("Bonne dégustation ")
                    appendInlineContent(id = ":comfy:")

                    append("\n\n")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append("− outadoc")
                    }
                },
                inlineContent =
                    mapOf(
                        ":comfy:" to
                            InlineTextContent(
                                placeholder =
                                    Placeholder(
                                        width = 20.sp,
                                        height = 20.sp,
                                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                                    ),
                            ) {
                                Image(
                                    imageResource(Res.drawable.comfy),
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = "Emote",
                                )
                            },
                    ),
            )
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
