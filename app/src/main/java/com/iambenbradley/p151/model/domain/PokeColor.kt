package com.iambenbradley.p151.model.domain

import androidx.compose.ui.graphics.Color

enum class PokeColor(val serialName: String) {
    Black("black"),
    Blue("blue"),
    Brown("brown"),
    Gray("gray"),
    Green("green"),
    Pink("pink"),
    Purple("purple"),
    Red("red"),
    White("white"),
    Yellow("yellow"),
    Unknown("unknown"),
    ;

    val color: Color
        get() = when (this) {
            Black -> Color(0xFF000000)
            Blue -> Color(0xFF0000FF)
            Brown -> Color(0xFF964B00)
            Gray -> Color(0xFF808080)
            Green -> Color(0xFF00FF00)
            Pink -> Color(0xFFFFC0CB)
            Purple -> Color(0xFF800080)
            Red -> Color(0xFFFF0000)
            White -> Color(0xFFFFFFFF)
            Yellow -> Color(0xFFFFFF00)
            Unknown -> Color(0x80808080)
        }
}
