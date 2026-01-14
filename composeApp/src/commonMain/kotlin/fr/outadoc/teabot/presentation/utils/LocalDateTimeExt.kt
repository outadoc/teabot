package fr.outadoc.teabot.presentation.utils

import androidx.compose.runtime.Composable
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.months
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringArrayResource

@Composable
fun LocalDateTime.localized(): String {
    val monthStrings = stringArrayResource(Res.array.months)

    if (monthStrings.isEmpty()) {
        // resources are initially empty, which would cause a crash here
        return toString()
    }

    val monthNames = MonthNames(monthStrings)

    return format(
        LocalDateTime.Format {
            chars("le ")
            day(Padding.NONE)
            char(' ')
            monthName(monthNames)
            chars(" à ")
            hour()
            char(':')
            minute()
        },
    )
}
