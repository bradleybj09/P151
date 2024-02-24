package com.iambenbradley.p151.model.domain

import androidx.compose.ui.graphics.Color

enum class PokeVersion {
    Red,
    Blue,
    Yellow,
    Other,
    ;

    fun getTint(): Color {
        return when (this) {
            Red -> Color(0xFFFF0000)
            Blue -> Color(0xFF0000FF)
            Yellow -> Color(0xFFFFFF00)
            Other -> Color(0xFF808080)
        }
    }
}