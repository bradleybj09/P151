package com.iambenbradley.p151.model.domain

/**
 * Represents a pokemon for purposes of presentation in the list view
 * @property id a common identifier for this pokemon
 * @property name the display name for this pokemon
 */
interface PokemonSummary {
    val id: Long
    val name: String
}