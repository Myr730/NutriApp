package org.bamx.puebla.feature.guess

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import androidx.compose.foundation.layout.WindowInsets
import kotlinx.coroutines.launch
import android.annotation.SuppressLint

val vegetableList = listOf(
    "ZANAHORIA",
    "TOMATE",
    "LECHUGA",
    "CEBOLLA",
    "REPOLLO",
    "BROCOLI",
    "ESPINACA",
    "PEPINO",
    "PIMIENTO",
    "CALABAZA",
    "COLIFLOR",
    "RABANO",
    "APIO",
    "BERENJENA",
    "AJO",
    "MAIZ",
    "CHICHARO",
    "ALCACHOFA",
    "ESPARRAGO",
    "CALABACIN"
)

class HangmanGameState {
    var currentVegetable = mutableStateOf("")
    var guessedLetters = mutableStateListOf<Char>()
    var wrongAttempts = mutableStateOf(0)
    var currentGame = mutableStateOf(1)
    var totalGames = 5
    var score = mutableStateOf(0)
    var gameStatus = mutableStateOf(GameStatus.PLAYING)
    var showGameOverDialog = mutableStateOf(false)
    var hintLetters = mutableStateListOf<Char>() // Nuevo: letras de pista
}

enum class GameStatus {
    PLAYING, WON, LOST
}

@Composable
fun GuessTheVegetableScreen(
    onBackClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.guess_title),
    @DrawableRes backgroundRes: Int = R.drawable.bg_guess,
) {

    val gameState = remember { HangmanGameState() }

    LaunchedEffect(Unit) {
        initializeGame(gameState)
    }

    // Mostrar diálogo de fin de juego
    if (gameState.showGameOverDialog.value) {
        GameOverDialog(
            score = gameState.score.value,
            totalGames = gameState.totalGames,
            onPlayAgain = {
                initializeGame(gameState)
                gameState.showGameOverDialog.value = false
            },
            onClose = {
                gameState.showGameOverDialog.value = false
                onBackClick()
            }
        )
    }

    // Determinar la imagen del personaje basado en los intentos fallidos
    val characterRes = when (gameState.wrongAttempts.value) {
        0 -> R.drawable.nutri_guess
        1 -> R.drawable.nutri_guess
        2 -> R.drawable.nutri_guess
        3 -> R.drawable.nutri_guess
        4 -> R.drawable.nutri_guess
        5 -> R.drawable.nutri_guess
        else -> R.drawable.nutri_guess
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = stringResource(id = R.string.cd_background),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TopBar: back + cartel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Botón de regresar
                Image(
                    painter = painterResource(id = R.drawable.ic_back2),
                    contentDescription = stringResource(id = R.string.cd_back),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp)
                        .clickable { onBackClick() }
                )

                TitleBanner(
                    text = title,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.66f)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Información del juego
            GameInfo(
                currentGame = gameState.currentGame.value,
                totalGames = gameState.totalGames,
                score = gameState.score.value,
                wrongAttempts = gameState.wrongAttempts.value
            )

            Spacer(Modifier.height(16.dp))

            // Palabra oculta con guiones Y letras de pista
            HiddenWord(
                word = gameState.currentVegetable.value,
                guessedLetters = gameState.guessedLetters,
                hintLetters = gameState.hintLetters, // Pasar las letras de pista
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Indicador de pistas
            HintIndicator(
                hintLetters = gameState.hintLetters,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Personaje al centro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = characterRes),
                    contentDescription = stringResource(id = R.string.cd_character),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth(0.50f)
                        .padding(top = 8.dp)
                )
            }

            // Teclado (A–Z)
            KeyboardGrid(
                letters = ('A'..'Z').map { it.toString() },
                guessedLetters = gameState.guessedLetters,
                hintLetters = gameState.hintLetters,
                currentVegetable = gameState.currentVegetable.value, // NUEVO: pasar la palabra actual
                onLetterClick = { letter ->
                    onLetterClicked(letter, gameState)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

// Función para inicializar el juego CON PISTAS
private fun initializeGame(gameState: HangmanGameState) {
    // Seleccionar una verdura aleatoria
    val vegetable = vegetableList.random()
    gameState.currentVegetable.value = vegetable
    gameState.guessedLetters.clear()
    gameState.hintLetters.clear() // Limpiar pistas anteriores
    gameState.wrongAttempts.value = 0
    gameState.gameStatus.value = GameStatus.PLAYING

    // Seleccionar 2 letras únicas como pista
    selectHintLetters(vegetable, gameState)
}

// Función para seleccionar 2 letras únicas como pista
private fun selectHintLetters(vegetable: String, gameState: HangmanGameState) {
    val uniqueLetters = vegetable.toCharArray().distinct().filter { it != ' ' }

    if (uniqueLetters.size >= 2) {
        // Seleccionar 2 letras aleatorias únicas
        val selectedHints = uniqueLetters.shuffled().take(2)
        gameState.hintLetters.addAll(selectedHints)

        // Agregar las letras de pista a las letras adivinadas automáticamente
        gameState.guessedLetters.addAll(selectedHints)
    } else if (uniqueLetters.size == 1) {
        // Si solo hay una letra única, usar esa y una adicional
        gameState.hintLetters.addAll(uniqueLetters)
        // Buscar otra letra que no sea la única
        val otherLetter = vegetable.first { it != uniqueLetters[0] && it != ' ' }
        gameState.hintLetters.add(otherLetter)
        gameState.guessedLetters.addAll(gameState.hintLetters)
    }
}

// Función para manejar el click en una letra
private fun onLetterClicked(letter: String, gameState: HangmanGameState) {
    if (gameState.gameStatus.value != GameStatus.PLAYING) return

    val char = letter[0]

    // Si la letra ya fue adivinada (incluyendo pistas), no hacer nada
    if (gameState.guessedLetters.contains(char)) return

    // Agregar la letra a las letras adivinadas
    gameState.guessedLetters.add(char)

    // Verificar si la letra está en la palabra
    if (!gameState.currentVegetable.value.contains(char, ignoreCase = true)) {
        gameState.wrongAttempts.value++
    }

    // Verificar si el jugador ganó
    if (gameState.currentVegetable.value.all { it == ' ' || gameState.guessedLetters.contains(it) }) {
        gameState.gameStatus.value = GameStatus.WON
        // Calcular puntos: base + bonus por menos errores - penalización por usar pistas
        val basePoints = 50
        val errorBonus = (6 - gameState.wrongAttempts.value) * 5
        val hintPenalty = gameState.hintLetters.size * 5 // Penalización por usar pistas
        val totalPoints = basePoints + errorBonus - hintPenalty
        gameState.score.value += maxOf(totalPoints, 10) // Mínimo 10 puntos

        // Pasar a la siguiente verdura después de un delay
        kotlinx.coroutines.GlobalScope.launch {
            kotlinx.coroutines.delay(1500L)
            if (gameState.currentGame.value < gameState.totalGames) {
                gameState.currentGame.value++
                initializeGame(gameState)
            } else {
                gameState.showGameOverDialog.value = true
            }
        }
    }
    // Verificar si el jugador perdió
    else if (gameState.wrongAttempts.value >= 6) {
        gameState.gameStatus.value = GameStatus.LOST

        // Pasar a la siguiente verdura después de un delay
        kotlinx.coroutines.GlobalScope.launch {
            kotlinx.coroutines.delay(1500L)
            if (gameState.currentGame.value < gameState.totalGames) {
                gameState.currentGame.value++
                initializeGame(gameState)
            } else {
                gameState.showGameOverDialog.value = true
            }
        }
    }
}

/* ---------- COMPONENTES ---------- */

@Composable
private fun GameInfo(
    currentGame: Int,
    totalGames: Int,
    score: Int,
    wrongAttempts: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Juego: $currentGame/$totalGames",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Puntos: $score",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Errores: $wrongAttempts/6",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HiddenWord(
    word: String,
    guessedLetters: List<Char>,
    hintLetters: List<Char>, // Nuevo parámetro para pistas
    modifier: Modifier = Modifier
) {
    val displayWord = word.map { char ->
        if (guessedLetters.contains(char)) {
            char.toString() // Mostrar la letra normal, no el círculo
        } else {
            "_"
        }
    }.joinToString("  ")

    Text(
        text = displayWord,
        textAlign = TextAlign.Center,
        color = Color.White,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            lineHeight = 32.sp,
            letterSpacing = 4.sp
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun HintIndicator(
    hintLetters: List<Char>,
    modifier: Modifier = Modifier
) {
    if (hintLetters.isNotEmpty()) {
        Text(
            text = "Pistas: ${hintLetters.joinToString(" ")}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Yellow,
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier
        )
    }
}

@Composable
private fun KeyboardGrid(
    letters: List<String>,
    guessedLetters: List<Char>,
    hintLetters: List<Char>,
    currentVegetable: String, // NUEVO: agregar este parámetro
    onLetterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(bottom = 6.dp)
    ) {
        items(letters) { key ->
            val char = key[0]
            val isGuessed = guessedLetters.contains(char)
            val isHint = hintLetters.contains(char)
            val isInWord = guessedLetters.contains(char) &&
                currentVegetable.contains(char, ignoreCase = true) // CORREGIDO: usar currentVegetable

            val backgroundColor = when {
                isHint -> Color(0xFFFFA000) // Naranja - letra de pista
                !isGuessed -> Color(0xFF74B86A) // Verde - disponible
                isInWord -> Color(0xFF4CAF50)   // Verde oscuro - correcta
                else -> Color(0xFFF44336)       // Rojo - incorrecta
            }
            Surface(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .clickable(
                        enabled = !isGuessed, // Las letras de pista no son clickeables
                        onClick = { onLetterClick(key) }
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.0f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = key,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )

                    // Indicador visual para letras de pista
                    if (isHint) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(8.dp)
                                .background(Color.Yellow, RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameOverDialog(
    score: Int,
    totalGames: Int,
    onPlayAgain: () -> Unit,
    onClose: () -> Unit
) {
    Dialog(
        onDismissRequest = onClose
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF4CAF50)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🎉 ¡Juego Terminado! 🎉",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Completaste $totalGames verduras",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Puntuación Final: $score",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow
                    )
                )

                // Mensaje basado en la puntuación
                val message = when {
                    score >= 200 -> "¡Excelente! Eres un experto en verduras 🌟"
                    score >= 150 -> "¡Muy bien! Conoces bien tus verduras 👍"
                    score >= 100 -> "¡Buen trabajo! Sigue practicando 💪"
                    else -> "¡Sigue intentándolo! 🍅"
                }

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                // Botones
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onPlayAgain,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF4CAF50)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Jugar otra vez", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Salir", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleBanner(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color(0xFFE75B2A),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 8.dp
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
        )
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Guess – 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewGuessLight() {
    AppTheme(darkTheme = false) {
        GuessTheVegetableScreen(
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Guess – 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewGuessDark() {
    AppTheme(darkTheme = true) {
        GuessTheVegetableScreen(
            onBackClick = {}
        )
    }
}
