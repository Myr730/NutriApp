package org.bamx.puebla.feature.memorama

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens.memoramaCardCorner
import org.bamx.puebla.ui.theme.Dimens.memoramaGridHSpace
import org.bamx.puebla.ui.theme.Dimens.memoramaGridVSpace
import org.bamx.puebla.ui.theme.timeout_header_green

// Data class para representar una carta
data class Card(
    val id: Int,
    val imageRes: Int,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false,
    var isClickable: Boolean = true,
    var isHighlighted: Boolean = false // Nueva propiedad para resaltar
)

// Estado del juego
class GameState {
    var cards = mutableStateListOf<Card>()
    var flippedCards = mutableStateListOf<Int>()
    var moves = mutableStateOf(0)
    var matches = mutableStateOf(0)
    var isChecking = mutableStateOf(false)
    var showCongratulations = mutableStateOf(false)
    var totalPairs = 6 // 12 cartas = 6 pares
}

@Composable
fun MemoramaScreen(
    onBackClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isSmall = configuration.screenWidthDp <= 360

    // Inicializar el estado del juego
    val gameState = remember { GameState() }

    // Inicializar las cartas cuando se carga la pantalla
    LaunchedEffect(Unit) {
        initializeGame(gameState)
    }

    // Mostrar diálogo de felicitaciones cuando se completen todos los pares
    if (gameState.showCongratulations.value) {
        CongratulationsDialog(
            moves = gameState.moves.value,
            onPlayAgain = {
                initializeGame(gameState)
                gameState.showCongratulations.value = false
            },
            onClose = {
                gameState.showCongratulations.value = false
                onBackClick()
            }
        )
    }

    AppScaffold(
        backgroundResId = R.drawable.bg_memorama,
        darkenBackground = 0f,
        padTopBar = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarMemorama(
                onBackClick = onBackClick,
                isSmall = isSmall
            )

            Spacer(Modifier.height(20.dp))

            // Mostrar contador de movimientos y pares
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Movimientos: ${gameState.moves.value}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = "Pares: ${gameState.matches.value}/${gameState.totalPairs}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(10.dp))

            // ACTUALIZAR esta llamada pasando la función onCardClick
            MemoramaGridContainer(
                gameState = gameState,
                onCardClick = { index -> onCardClick(gameState, index) }
            )

            Spacer(Modifier.weight(1f))
        }
    }
}

// Función para inicializar el juego
private fun initializeGame(gameState: GameState) {
    val imageResources = listOf(
        R.drawable.granos,
        R.drawable.cereales,
        R.drawable.tuberculos,
        R.drawable.leguminosas,
        R.drawable.leguminosas2,
        R.drawable.tuber1
    )

    // Crear pares de cartas
    val cardPairs = imageResources.flatMap { imageRes ->
        listOf(
            Card(id = imageResources.indexOf(imageRes) * 2, imageRes = imageRes),
            Card(id = imageResources.indexOf(imageRes) * 2 + 1, imageRes = imageRes)
        )
    }

    // Mezclar las cartas
    gameState.cards.clear()
    gameState.cards.addAll(cardPairs.shuffled())
    gameState.flippedCards.clear()
    gameState.moves.value = 0
    gameState.matches.value = 0
    gameState.isChecking.value = false
    gameState.showCongratulations.value = false
}

// Función para manejar el click en una carta (hacerla top-level)
private fun onCardClick(gameState: GameState, cardIndex: Int) {
    val card = gameState.cards[cardIndex]

    // Validar si la carta puede ser clickeada
    if (!card.isClickable || card.isFaceUp || card.isMatched || gameState.isChecking.value) {
        return
    }

    // Resaltar la carta individual
    card.isHighlighted = true

    // Quitar el resaltado después de un tiempo
    kotlinx.coroutines.GlobalScope.launch {
        delay(500L)
        card.isHighlighted = false
    }

    // Voltear la carta
    card.isFaceUp = true
    gameState.flippedCards.add(cardIndex)

    // Verificar si tenemos dos cartas volteadas
    if (gameState.flippedCards.size == 2) {
        gameState.moves.value++
        gameState.isChecking.value = true

        // Verificar si las cartas coinciden
        val firstCard = gameState.cards[gameState.flippedCards[0]]
        val secondCard = gameState.cards[gameState.flippedCards[1]]

        if (firstCard.imageRes == secondCard.imageRes) {
            // Cartas coinciden - resaltar el par
            firstCard.isHighlighted = true
            secondCard.isHighlighted = true

            kotlinx.coroutines.GlobalScope.launch {
                delay(1000L) // Mantener el resaltado por 1 segundo

                firstCard.isMatched = true
                secondCard.isMatched = true
                firstCard.isHighlighted = false
                secondCard.isHighlighted = false
                gameState.matches.value++
                gameState.flippedCards.clear()
                gameState.isChecking.value = false

                // Verificar si se completó el juego
                if (gameState.matches.value == gameState.totalPairs) {
                    delay(500L) // Pequeño delay antes de mostrar felicitaciones
                    gameState.showCongratulations.value = true
                }
            }
        } else {
            // Cartas no coinciden - voltear de nuevo después de un delay
            kotlinx.coroutines.GlobalScope.launch {
                delay(1000L) // 1 segundo de delay

                firstCard.isFaceUp = false
                secondCard.isFaceUp = false
                gameState.flippedCards.clear()
                gameState.isChecking.value = false
            }
        }
    }
}
@Composable
private fun MemoramaCard(
    card: Card,
    modifier: Modifier = Modifier
) {
    // Animación de alpha para el flip
    val alpha = animateFloatAsState(
        targetValue = if (card.isFaceUp || card.isMatched) 1f else 0f,
        animationSpec = tween(300),
        label = "cardAlpha"
    )

    // Animación de escala para match y resaltado
    val scale = animateFloatAsState(
        targetValue = when {
            card.isHighlighted -> 1.15f
            card.isMatched -> 1.1f
            else -> 1f
        },
        animationSpec = tween(300),
        label = "cardScale"
    )

    // Animación de sombra para resaltado
    val elevation = animateFloatAsState(
        targetValue = if (card.isHighlighted) 16f else 4f,
        animationSpec = tween(300),
        label = "cardElevation"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .shadow(
                elevation = elevation.value.dp,
                shape = RoundedCornerShape(memoramaCardCorner),
                clip = true
            ),
        contentAlignment = Alignment.Center
    ) {
        // REVERSO (visible cuando alpha es 0)
        if (alpha.value < 0.5f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(memoramaCardCorner))
                    .background(if (card.isHighlighted) Color.Yellow else Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.card_back),
                    contentDescription = "Reverso de carta",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_card_placeholder),
                    contentDescription = "Placeholder de carta",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth(0.45f)
                )
            }
        }

        // FRENTE (visible cuando alpha es 1)
        if (alpha.value >= 0.5f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(memoramaCardCorner))
                    .background(
                        when {
                            card.isHighlighted -> Color.Yellow.copy(alpha = 0.3f)
                            card.isMatched -> Color.Green.copy(alpha = 0.2f)
                            else -> Color.White
                        }
                    )
            ) {
                Image(
                    painter = painterResource(id = card.imageRes),
                    contentDescription = "Carta volteada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Efecto de brillo adicional para cartas resaltadas
                if (card.isHighlighted) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                    colors = listOf(
                                        Color.Yellow.copy(alpha = 0.4f),
                                        Color.Transparent
                                    ),
                                    center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                                    radius = 0.8f
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun CongratulationsDialog(
    moves: Int,
    onPlayAgain: () -> Unit,
    onClose: () -> Unit
) {
    Dialog(
        onDismissRequest = onClose
    ) {
        var showFireworks by remember { mutableStateOf(false) }

        // Iniciar efectos especiales después de que el diálogo se muestre
        LaunchedEffect(Unit) {
            delay(100)
            showFireworks = true
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFD700),
                            Color(0xFFFFA500),
                            Color(0xFFFF8C00)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icono de celebración
                Image(
                    painter = painterResource(id = R.drawable.ic_card_placeholder), // Puedes cambiar por un icono de celebración
                    contentDescription = "Celebración",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )

                // Título
                Text(
                    text = "¡Felicidades! 🎉",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                // Mensaje
                Text(
                    text = "¡Completaste el memorama!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                // Contador de movimientos
                Text(
                    text = "Movimientos: $moves",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
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
                            contentColor = Color(0xFFFF8C00)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Jugar otra vez", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE65100),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Salir", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Efectos de fuegos artificiales (simulados con composición)
            if (showFireworks) {
                FireworksEffect()
            }
        }
    }
}

@Composable
private fun FireworksEffect() {
    // Simulación simple de efectos de fuegos artificiales
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Puntos brillantes alrededor del diálogo
        for (i in 0 until 8) {
            val angle = (i * 45).toFloat()
            val distance = 120f
            val x = (distance * kotlin.math.cos(Math.toRadians(angle.toDouble()))).toFloat()
            val y = (distance * kotlin.math.sin(Math.toRadians(angle.toDouble()))).toFloat()

            // Animación con delay corregida
            val scale = animateFloatAsState(
                targetValue = if (i % 2 == 0) 1.5f else 1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    delayMillis = i * 100 // CORREGIDO: delayMillis en lugar de delay
                )
            )

            Box(
                modifier = Modifier
                    .offset(x = x.dp, y = y.dp)
                    .size(16.dp)
                    .background(
                        color = when (i % 3) {
                            0 -> Color.Yellow
                            1 -> Color.Red
                            else -> Color.Blue
                        }.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(50)
                    )
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
            )
        }
    }
}

// El resto del código permanece igual (TopBarMemorama, MemoramaGridContainer, TitleBanner)...

@Composable
private fun TopBarMemorama(
    onBackClick: () -> Unit,
    isSmall: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .heightIn(min = 68.dp)
            .padding(top = 8.dp)
    ) {
        TitleBanner(
            text = stringResource(id = R.string.memorama_title),
            modifier = Modifier.align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp)
                .size(if (isSmall) 44.dp else 52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
@Suppress("UnusedBoxWithConstraintsScope")
private fun MemoramaGridContainer(
    gameState: GameState,
    onCardClick: (Int) -> Unit // AGREGAR este parámetro
) {
    val cardAspectRatio = 0.68f
    val columns = 3
    val rows = 4
    val contentPaddingHorizontal = 12.dp
    val contentPaddingVerticalTop = 12.dp
    val contentPaddingVerticalBottom = 16.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(0.82f),
        contentAlignment = Alignment.Center
    ) {
        val hPaddingPx = with(LocalDensity.current) { contentPaddingHorizontal.toPx() * 2 }
        val hSpacingPx = with(LocalDensity.current) { memoramaGridHSpace.toPx() * (columns - 1) }
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }

        val cardWidthPx = (maxWidthPx - hPaddingPx - hSpacingPx) / columns
        val cardWidthDp = with(LocalDensity.current) { cardWidthPx.toDp() }

        val cardHeightDp = cardWidthDp / cardAspectRatio

        val totalGridHeight = (cardHeightDp * rows) +
            (memoramaGridVSpace * (rows - 1)) +
            contentPaddingVerticalTop +
            contentPaddingVerticalBottom

        Box(
            modifier = Modifier.height(totalGridHeight)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(memoramaGridHSpace),
                verticalArrangement = Arrangement.spacedBy(memoramaGridVSpace),
                contentPadding = PaddingValues(
                    start = contentPaddingHorizontal,
                    end = contentPaddingHorizontal,
                    top = contentPaddingVerticalTop,
                    bottom = contentPaddingVerticalBottom
                ),
                userScrollEnabled = false
            ) {
                items(gameState.cards.size) { index ->
                    MemoramaCard(
                        card = gameState.cards[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(cardAspectRatio)
                            .clickable {
                                onCardClick(index) // USAR el parámetro
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.88f)
            .clip(RoundedCornerShape(20.dp))
            .background(timeout_header_green)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                lineHeight = 26.sp
            )
        )
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Memorama - 411x891 Light",
    showBackground = true,
    backgroundColor = 0xFFFDEFD9,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewMemoramaLight() {
    AppTheme(darkTheme = false) {
        MemoramaScreen(
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Memorama - 360x640 Dark",
    showBackground = true,
    backgroundColor = 0xFFFDEFD9,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewMemoramaDark() {
    AppTheme(darkTheme = true) {
        MemoramaScreen(
            onBackClick = {}
        )
    }
}
