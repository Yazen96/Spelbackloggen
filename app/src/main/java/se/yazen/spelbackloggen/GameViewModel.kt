
package se.yazen.spelbackloggen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    var games by mutableStateOf(emptyList<Game>())
        private set

    private var nextId = 1

    fun addGame(title: String, platform: String) {
        val gameTitle = title.trim()

        if (gameTitle.isEmpty()) {
            return
        }

        val newGame = Game(
            id = nextId,
            title = gameTitle,
            platform = platform
        )

        games = games + newGame
        nextId++
    }

    fun toggleCompleted(id: Int) {
        games = games.map {
            if (it.id == id) it.copy(completed = !it.completed)
            else it
        }
    }

    fun deleteGame(id: Int) {
        games = games.filter { it.id != id }
    }

    fun editGame(id: Int, title: String, platform: String) {
        if (title.isBlank()) return

        games = games.map {
            if (it.id == id) it.copy(title = title.trim(), platform = platform)
            else it
        }
    }

}
