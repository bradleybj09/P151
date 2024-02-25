package com.iambenbradley.p151.util

fun String.getPokemonId() = substringBeforeLast('/')
    .substringAfterLast('/')
    .toLongOrNull()
