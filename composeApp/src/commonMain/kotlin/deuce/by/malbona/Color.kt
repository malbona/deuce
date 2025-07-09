@file:Suppress("NonAsciiCharacters", "NonAsciiCharacters")

package deuce.by.malbona

import androidx.compose.ui.graphics.Color

@Suppress("EnumEntryName")
enum class DefinedColor(val color: Color) {
    `Blue🇺🇦`(strongAzure),
    `Yellow🇺🇦`(yellow),
    Brat(green),
    `Red🇨🇳`(red)
}

val strongAzure = Color(red = 0, green = 91, blue = 187)
val yellow = Color(red = 255, green = 213, blue = 0)

val green = Color(red = 138, green = 206, blue = 0)
val red = Color(red = 238, green = 28, blue = 37)
