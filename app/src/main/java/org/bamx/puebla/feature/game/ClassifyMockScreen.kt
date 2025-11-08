package org.bamx.puebla.feature.game

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.bamx.puebla.R
import org.bamx.puebla.ui.responsive.rememberUiMetrics
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens
import androidx.compose.foundation.layout.WindowInsets

// Data class para representar un alimento
data class Food(
    @DrawableRes val imageRes: Int,
    val name: String,
    val correctBasket: BasketType
)

enum class BasketType {
    DAILY, SOMETIMES, RARE
}

// Estado del juego
class ClassificationGameState {
    var currentFoodIndex = mutableStateOf(0)
    var score = mutableStateOf(0)
    var totalFoods = mutableStateOf(8)
    var draggedOffset = mutableStateOf(Offset.Zero)
    var isDragging = mutableStateOf(false)
    var showFeedback = mutableStateOf(false)
    var feedbackMessage = mutableStateOf("")
    var feedbackIsSuccess = mutableStateOf(false)
    var gameCompleted = mutableStateOf(false)
    var selectedFoods = mutableStateListOf<Food>()
}

// Lista completa de alimentos con sus categorías correctas
val foodList = listOf(
    // DIARIO - Frutas y verduras frescas (consumo diario)
    Food(R.drawable.food_apple, "Manzana", BasketType.DAILY),
    Food(R.drawable.food_banana, "Plátano", BasketType.DAILY),
    Food(R.drawable.food_broccoli, "Brócoli", BasketType.DAILY),
    Food(R.drawable.food_carrot, "Zanahoria", BasketType.DAILY),
    Food(R.drawable.food_grapes, "Uvas", BasketType.DAILY),
    Food(R.drawable.food_lettuce, "Lechuga", BasketType.DAILY),
    Food(R.drawable.food_orange, "Naranja", BasketType.DAILY),
    Food(R.drawable.food_strawberry, "Fresa", BasketType.DAILY),
    Food(R.drawable.food_tomato, "Tomate", BasketType.DAILY),
    Food(R.drawable.food_corn, "Maíz", BasketType.DAILY),
    Food(R.drawable.food_chicken, "Pollo", BasketType.DAILY),



    // A VECES - Proteínas y granos (consumo moderado)
    Food(R.drawable.food_cheese, "Queso", BasketType.SOMETIMES),
    Food(R.drawable.food_chicken, "Pollo", BasketType.SOMETIMES),
    Food(R.drawable.food_meat, "Carne", BasketType.SOMETIMES),
    Food(R.drawable.food_meatballs, "Albóndigas", BasketType.SOMETIMES),
    Food(R.drawable.food_corn, "Maíz", BasketType.SOMETIMES),
    Food(R.drawable.food_rawmeat, "Filete de Carne", BasketType.SOMETIMES),


    // OCASIONAL - Alimentos procesados y dulces (consumo limitado)
    Food(R.drawable.food_cake, "Pastel", BasketType.RARE),
    Food(R.drawable.food_chocolate, "Chocolate", BasketType.RARE),
    Food(R.drawable.food_cookie, "Galleta", BasketType.RARE),
    Food(R.drawable.food_frenchfries, "Papas fritas", BasketType.RARE),
    Food(R.drawable.food_soda, "Refresco", BasketType.RARE)
)

@Composable
fun ClassifyGameScreen(
    onBackClick: () -> Unit = {},
) {
    val m = rememberUiMetrics()
    val configuration = LocalConfiguration.current
    val isSmall = configuration.screenWidthDp <= 360

    // Estado del juego
    val gameState = remember { ClassificationGameState() }

    // Posiciones de las canastas
    val basketPositions = remember { mutableStateMapOf<BasketType, Offset>() }

    // Inicializar el juego
    LaunchedEffect(Unit) {
        initializeGame(gameState)
    }

    val signWidthFraction = m.gameSignWidthFraction
    val titleFontSize = m.gameTitleFontSizeSp.sp
    val titleLineHeight = m.gameTitleLineHeightSp.sp
    val fruitWidth = (m.screenWidthDp * m.gameFruitWidthFraction).dp

    val basketHeight: Dp = (m.screenWidthDp * m.gameBasketHeightFraction * 1.3f).dp
    val bottomPadding = if (m.sizeClass == org.bamx.puebla.ui.responsive.SizeClass.Small) 12.dp else 16.dp

    // Ocultar feedback después de 2 segundos
    LaunchedEffect(gameState.showFeedback.value) {
        if (gameState.showFeedback.value) {
            delay(2000L)
            gameState.showFeedback.value = false

            if (!gameState.gameCompleted.value) {
                nextFood(gameState)
            }
        }
    }

    // ELIMINAR: No definir currentFood aquí, dejar que DraggableFood lo maneje internamente

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo
        Image(
            painter = painterResource(id = R.drawable.bg_market),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Botón de regresar
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 12.dp, top = 12.dp)
                .size(if (isSmall) 44.dp else 52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )

        // Información del juego
        GameInfo(
            score = gameState.score.value,
            currentFood = gameState.currentFoodIndex.value + 1,
            totalFoods = gameState.totalFoods.value,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 12.dp, end = 16.dp)
        )

        // Cartel centrado + título
        Box(
            modifier = Modifier
                .fillMaxWidth(signWidthFraction)
                .align(Alignment.TopCenter)
                .padding(top = Dimens.classifySignTop + Dimens.classifySignExtraTop)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sign_wood),
                contentDescription = stringResource(id = R.string.cd_sign),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.classify_title_multiline),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = titleFontSize,
                        lineHeight = titleLineHeight,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.35f),
                            offset = Offset(0f, 2f),
                            blurRadius = 6f
                        )
                    )
                )
            }
        }

        // Alimento arrastrable - SIMPLIFICADO: Siempre mostrar DraggableFood
        DraggableFood(
            gameState = gameState,
            basketPositions = basketPositions,
            fruitWidth = fruitWidth,
            modifier = Modifier.align(Alignment.Center)
        )

        // Feedback message
        if (gameState.showFeedback.value) {
            FeedbackMessage(
                message = gameState.feedbackMessage.value,
                isSuccess = gameState.feedbackIsSuccess.value,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Panel de juego completado
        if (gameState.gameCompleted.value) {
            GameCompletedDialog(
                score = gameState.score.value,
                totalFoods = gameState.totalFoods.value,
                onPlayAgain = { initializeGame(gameState) },
                onClose = onBackClick,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Canastas
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = Dimens.classifyScreenPadding, vertical = bottomPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Basket(
                    label = stringResource(id = R.string.classify_daily),
                    resId = R.drawable.basket_daily,
                    height = basketHeight,
                    basketType = BasketType.DAILY,
                    onPositionDetected = { position ->
                        basketPositions[BasketType.DAILY] = position
                    },
                    modifier = Modifier.weight(1f)
                )
                Basket(
                    label = stringResource(id = R.string.classify_sometimes),
                    resId = R.drawable.basket_sometimes,
                    height = basketHeight,
                    basketType = BasketType.SOMETIMES,
                    onPositionDetected = { position ->
                        basketPositions[BasketType.SOMETIMES] = position
                    },
                    modifier = Modifier.weight(1f)
                )
                Basket(
                    label = stringResource(id = R.string.classify_rare),
                    resId = R.drawable.basket_rare,
                    height = basketHeight,
                    basketType = BasketType.RARE,
                    onPositionDetected = { position ->
                        basketPositions[BasketType.RARE] = position
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DraggableFood(
    gameState: ClassificationGameState,
    basketPositions: Map<BasketType, Offset>,
    fruitWidth: Dp,
    modifier: Modifier = Modifier
) {
    val foodSize = fruitWidth * 0.6f
    var startPosition by remember { mutableStateOf(Offset.Zero) }

    // Obtener el alimento actual de forma REACTIVA usando derivedStateOf
    val currentFood by remember(gameState.currentFoodIndex.value, gameState.selectedFoods) {
        derivedStateOf {
            if (gameState.currentFoodIndex.value < gameState.selectedFoods.size) {
                gameState.selectedFoods[gameState.currentFoodIndex.value]
            } else {
                null
            }
        }
    }

    Box(
        modifier = modifier
            .offset(
                x = gameState.draggedOffset.value.x.dp,
                y = gameState.draggedOffset.value.y.dp
            )
            .size(foodSize)
            .onGloballyPositioned { coordinates ->
                if (startPosition == Offset.Zero) {
                    startPosition = coordinates.localToRoot(Offset.Zero)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        gameState.isDragging.value = true
                        gameState.draggedOffset.value = Offset.Zero
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        gameState.draggedOffset.value += dragAmount
                    },
                    onDragEnd = {
                        gameState.isDragging.value = false

                        val finalPosition = startPosition + gameState.draggedOffset.value

                        var collisionDetected = false
                        for ((basketType, basketPosition) in basketPositions) {
                            val basketArea = Rect(
                                left = basketPosition.x - 150f,
                                top = basketPosition.y - 100f,
                                right = basketPosition.x + 150f,
                                bottom = basketPosition.y + 200f
                            )

                            if (finalPosition.x >= basketArea.left &&
                                finalPosition.x <= basketArea.right &&
                                finalPosition.y >= basketArea.top &&
                                finalPosition.y <= basketArea.bottom) {

                                collisionDetected = true

                                // CORREGIDO: Usar currentFood con safe call
                                currentFood?.let { food ->
                                    val isCorrect = food.correctBasket == basketType

                                    if (isCorrect) {
                                        gameState.score.value++
                                        gameState.feedbackMessage.value = "¡Correcto! 🎉\n${food.name} es para consumo ${getBasketText(basketType).lowercase()}"
                                        gameState.feedbackIsSuccess.value = true
                                        println("DEBUG: CORRECTO - ${food.name} en ${getBasketText(basketType)}")
                                    } else {
                                        gameState.feedbackMessage.value = "¡Ups! ❌\n${food.name} debería ir en ${getBasketText(food.correctBasket).lowercase()}"
                                        gameState.feedbackIsSuccess.value = false
                                        println("DEBUG: INCORRECTO - ${food.name} debería ir en ${getBasketText(food.correctBasket)}")
                                    }

                                    gameState.showFeedback.value = true
                                }
                                break
                            }
                        }

                        if (!collisionDetected) {
                            gameState.draggedOffset.value = Offset.Zero
                            println("DEBUG: No se detectó colisión con ninguna canasta")
                        } else {
                            gameState.draggedOffset.value = Offset.Zero
                        }
                    }
                )
            }
    ) {
        // CORREGIDO: Usar currentFood directamente con el operador safe call
        if (currentFood != null) {
            val food = currentFood!! // Safe porque ya verificamos que no es null

            Image(
                painter = painterResource(id = food.imageRes),
                contentDescription = "Arrastrar ${food.name}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (gameState.isDragging.value) 0.9f else 1f)
                    .shadow(
                        elevation = if (gameState.isDragging.value) 16.dp else 8.dp,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
            )

            // Debug visual temporal
            Text(
                text = food.name,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(4.dp)
            )
        } else {
            // Mensaje si no hay alimento
            Text(
                text = "Esperando alimento... Índice: ${gameState.currentFoodIndex.value}",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
data class Rect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

@Composable
private fun Basket(
    label: String,
    resId: Int,
    height: Dp,
    basketType: BasketType,
    onPositionDetected: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(height)
            .onGloballyPositioned { coordinates ->
                val position = coordinates.localToRoot(Offset.Zero)
                onPositionDetected(position)
            }
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = when (resId) {
                R.drawable.basket_daily -> stringResource(id = R.string.cd_basket_daily)
                R.drawable.basket_sometimes -> stringResource(id = R.string.cd_basket_sometimes)
                else -> stringResource(id = R.string.cd_basket_rare)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = label,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.35f),
                    offset = Offset(0f, 2f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        )
    }
}

@Composable
private fun GameInfo(
    score: Int,
    currentFood: Int,
    totalFoods: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF4CAF50).copy(alpha = 0.9f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Puntos: $score",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "$currentFood/$totalFoods",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun FeedbackMessage(
    message: String,
    isSuccess: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .shadow(16.dp, RoundedCornerShape(20.dp)),
        color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFF44336),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isSuccess) "🎉" else "💡",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun GameCompletedDialog(
    score: Int,
    totalFoods: Int,
    onPlayAgain: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .shadow(24.dp, RoundedCornerShape(24.dp)),
        color = Color(0xFFFFD700),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏆 ¡Felicidades! 🏆",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513)
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Completaste $totalFoods clasificaciones",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF8B4513)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Puntuación final: $score/$totalFoods",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            )

            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                androidx.compose.material3.Button(
                    onClick = onPlayAgain,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Jugar otra vez", fontWeight = FontWeight.Bold)
                }

                androidx.compose.material3.Button(
                    onClick = onClose,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    Text("Salir", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Funciones del juego

private fun initializeGame(gameState: ClassificationGameState) {
    // Limpiar completamente
    gameState.selectedFoods.clear()

    // Seleccionar 8 alimentos aleatorios
    val shuffledFoods = foodList.shuffled()
    gameState.selectedFoods.addAll(shuffledFoods.take(8))

    // Debug: mostrar los alimentos seleccionados - CORREGIDO: dentro de la función
    println("DEBUG: Alimentos seleccionados para esta partida:")
    gameState.selectedFoods.forEachIndexed { index, food ->
        println("  $index: ${food.name} -> ${getBasketText(food.correctBasket)}")
    }
    println("DEBUG: Primer alimento: ${gameState.selectedFoods.getOrNull(0)?.name}")

    // Reiniciar estado completamente
    gameState.currentFoodIndex.value = 0
    gameState.score.value = 0
    gameState.totalFoods.value = 8
    gameState.draggedOffset.value = Offset.Zero
    gameState.isDragging.value = false
    gameState.showFeedback.value = false
    gameState.feedbackMessage.value = ""
    gameState.feedbackIsSuccess.value = false
    gameState.gameCompleted.value = false
}


private fun nextFood(gameState: ClassificationGameState) {
    if (gameState.currentFoodIndex.value < gameState.selectedFoods.size - 1) {
        gameState.currentFoodIndex.value++
        gameState.draggedOffset.value = Offset.Zero

        // Debug: mostrar el siguiente alimento - CORREGIDO: dentro de la función
        val nextFood = gameState.selectedFoods[gameState.currentFoodIndex.value]
        println("DEBUG: Siguiente alimento (índice ${gameState.currentFoodIndex.value}): ${nextFood.name} -> ${getBasketText(nextFood.correctBasket)}")
    } else {
        gameState.gameCompleted.value = true
        println("DEBUG: Juego completado!")
    }
}

private fun getBasketText(basketType: BasketType): String {
    return when (basketType) {
        BasketType.DAILY -> "Diario"
        BasketType.SOMETIMES -> "A veces"
        BasketType.RARE -> "Ocasional"
    }
}

// O si prefieres tenerla como extensión, puedes hacerlo así:
private fun BasketType.displayText(): String {
    return when (this) {
        BasketType.DAILY -> "Diario"
        BasketType.SOMETIMES -> "A veces"
        BasketType.RARE -> "Ocasional"
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Classify - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewClassifyLight() {
    AppTheme(darkTheme = false) {
        ClassifyGameScreen()
    }
}

@Preview(
    name = "Classify - 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewClassifyDark() {
    AppTheme(darkTheme = true) {
        ClassifyGameScreen()
    }
}
