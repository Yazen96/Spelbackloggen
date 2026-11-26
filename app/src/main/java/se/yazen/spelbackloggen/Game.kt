package se.yazen.spelbackloggen

data class Game(
    val id: Int,
    val title: String,
    val platform: String,
    val completed: Boolean = false
)