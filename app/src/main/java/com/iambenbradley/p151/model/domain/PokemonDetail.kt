package com.iambenbradley.p151.model.domain

/**
 * A detailed representation of a pokemon, for the detail view.
 * @property id a common identifier for this pokemon
 * @property name the display name for this pokemon
 * @property sprite a url to a classic pixel image for this pokemon
 * @property color the nearest main color category that this pokemon falls into
 * @property relatedPokemon a list of pokemon who are related by evolution to this pokemon
 * @property evolvesFrom the pokemon, if any, that this pokemon evolves from
 * @property flavorText a key-value pair, where the value is a description of the pokemon, keyed
 * on the version where that description was seen
 * @property habitat a simple string to describe the habitat of the pokemon
 * @property isLegendary true if this is a Legendary pokemon
 * @property types a set of the types of the pokemon (e.g. fire, fighting, ghost, etc.)
 */
interface PokemonDetail {
    val id: Long
    val name: String
    val sprite: String
    val color: PokeColor
    val relatedPokemon: List<PokemonSummary>
    val evolvesFrom: PokemonSummary?
    val flavorText: Map<PokeVersion, String>
    val habitat: String?
    val isLegendary: Boolean
    val types: Set<Type>
}
