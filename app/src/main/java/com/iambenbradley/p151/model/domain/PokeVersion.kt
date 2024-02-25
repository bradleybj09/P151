package com.iambenbradley.p151.model.domain

enum class PokeVersion {
    Red,
    Blue,
    Yellow,
    Other,
    ;

    fun next(): PokeVersion {
        return when (this) {
            Red -> Blue
            Blue -> Yellow
            Yellow -> Red
            Other -> Red
        }
    }
}