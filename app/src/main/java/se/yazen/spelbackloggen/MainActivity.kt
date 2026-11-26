
package se.yazen.spelbackloggen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.yazen.spelbackloggen.ui.theme.SpelbackloggenTheme

class MainActivity : ComponentActivity() {
    private val vm by viewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpelbackloggenTheme {
                GameScreen(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(vm: GameViewModel) {
    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("PC") }
    var editId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Spelbackloggen") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Spelnamn") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("PC", "PS5", "Xbox").forEach {
                    FilterChip(
                        selected = platform == it,
                        onClick = { platform = it },
                        label = { Text(it) }
                    )
                }
            }

            Button(onClick = {
                if (title.isNotBlank()) {
                    val id = editId
                    if (id == null) vm.addGame(title, platform)
                    else vm.editGame(id, title, platform)
                    title = ""
                    editId = null
                }
            }) {
                Text(if (editId == null) "Lägg till" else "Spara")
            }

            Text("${vm.games.count { it.completed }} av ${vm.games.size} klara")

            if (vm.games.isEmpty()) {
                Text("Inga spel ännu. Lägg till ditt första spel!")
            } else {
                LazyColumn {
                    items(vm.games) { game ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = game.completed,
                                onCheckedChange = { vm.toggleCompleted(game.id) }
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(game.title)
                                Text(game.platform)
                            }

                            TextButton(onClick = {
                                title = game.title
                                platform = game.platform
                                editId = game.id
                            }) {
                                Text("Ändra")
                            }

                            TextButton(onClick = {
                                vm.deleteGame(game.id)
                                if (editId == game.id) {
                                    editId = null
                                    title = ""
                                }
                            }) {
                                Text("Ta bort")
                            }
                        }
                    }
                }
            }
        }
    }
}
