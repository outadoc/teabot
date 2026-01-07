package fr.outadoc.teabot.presentation.theme

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable (() -> Unit),
) {
    val colorScheme =
        when {
            darkTheme -> {
                darkScheme
            }

            else -> {
                lightScheme
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = {
            CompositionLocalProvider(
                LocalScrollbarStyle provides
                    defaultScrollbarStyle().copy(
                        hoverColor = MaterialTheme.colorScheme.surfaceTint,
                        unhoverColor = MaterialTheme.colorScheme.surfaceTint,
                    ),
                content = content,
            )
        },
    )
}
