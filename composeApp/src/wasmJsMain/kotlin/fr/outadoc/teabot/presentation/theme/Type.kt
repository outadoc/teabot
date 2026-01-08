package fr.outadoc.teabot.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.outadoc.teabot.generated.Neuton_Regular
import fr.outadoc.teabot.generated.Res
import org.jetbrains.compose.resources.Font

private val displayFontFamily: FontFamily
    @Composable
    get() =
        FontFamily(
            Font(
                Res.font.Neuton_Regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal,
            ),
        )

// Default Material 3 typography values
private val baseline = Typography()

val AppTypography
    @Composable
    get() =
        Typography(
            displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
            displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
            displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
            headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
            headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
            headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
            titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily, fontSize = 26.sp),
            titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
            titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
            bodyLarge = baseline.bodyLarge,
            bodyMedium = baseline.bodyMedium,
            bodySmall = baseline.bodySmall,
            labelLarge = baseline.labelLarge,
            labelMedium = baseline.labelMedium,
            labelSmall = baseline.labelSmall,
        )
