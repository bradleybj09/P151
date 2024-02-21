package com.iambenbradley.p151.model.domain

/**
 * Represents a pokemon for purposes of presentation in the list view
 * @property id a common identifier for this pokemon
 * @property name the display name for this pokemon
 * @property sprite a url to a classic pixel image for this pokemon
 */
interface PokemonSummary {
    val id: Long
    val name: String
    val sprite: String
}