package org.bamx.puebla.feature.smoothie

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bamx.puebla.R
import java.util.*
import kotlin.math.roundToInt

// Sistema de audio
class AudioManager(private val context: android.content.Context) {
    fun playButtonSound() {
        // Implementar reproducción de sonido de botones
    }

    fun playBlenderSound() {
        // Implementar sonido de licuadora
    }

    fun playMilkSound() {
        // Implementar sonido de leche
    }

    fun playShineSound() {
        // Implementar sonido de brillo
    }

    fun playPoingSound() {
        // Implementar sonido POING
    }

    fun startBackgroundMusic() {
        // Implementar música de fondo
    }

    fun stopBackgroundMusic() {
        // Detener música de fondo
    }

    fun stopAllSounds() {
        // Detener todos los sonidos
    }
}

data class DraggableFruit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val imageRes: Int,
    var offset: Offset = Offset.Zero,
    var isDragging: Boolean = false,
    var isCopy: Boolean = false
)

// FUNCIÓN AUXILIAR PARA OBTENER IMAGEN DE FRUTA
private fun getFruitImageResource(fruitName: String): Int {
    return when (fruitName) {
        "fresa" -> R.drawable.fresa
        "uva" -> R.drawable.uva
        "manzana" -> R.drawable.manzana
        "banana" -> R.drawable.banana
        "naranja" -> R.drawable.naranja
        else -> R.drawable.fresa
    }
}

// FUNCIÓN AUXILIAR PARA FONDOS - ACTUALIZADA
private fun getBackgroundImage(
    gameState: GameState,
    milkAdded: Boolean,
    hasLost: Boolean,
    licuadoFinished: Boolean,
    isTransitioningAfterMilk: Boolean
): Int {
    return when {
        hasLost || gameState == GameState.LOST -> R.drawable.perdiste
        gameState == GameState.FINISHED -> R.drawable.fondolicuado4
        milkAdded && licuadoFinished -> R.drawable.fondolicuado3
        milkAdded && isTransitioningAfterMilk -> R.drawable.alicuar
        milkAdded -> R.drawable.fondolicuado3
        else -> R.drawable.fondolicuado2
    }
}

// FUNCIÓN AUXILIAR PARA ESTRELLAS
private fun getStarsImageResource(stars: Int): Int {
    return when (stars) {
        0 -> R.drawable.stars0
        1 -> R.drawable.stars1
        2 -> R.drawable.stars2
        3 -> R.drawable.stars3
        4 -> R.drawable.stars4
        5 -> R.drawable.stars5
        else -> R.drawable.stars0
    }
}

enum class GameState {
    PLAYING, FINISHED, LOST
}

@Composable
fun SmoothieMakerScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val audioManager = remember { AudioManager(context) }

    // Estados del juego
    var gameState by remember { mutableStateOf(GameState.PLAYING) }
    var timeRemaining by remember { mutableIntStateOf(30) }
    var trianglePosition by remember { mutableFloatStateOf(0f) }
    var isTriangleMoving by remember { mutableStateOf(false) }
    var blenderEnabled by remember { mutableStateOf(false) }
    var milkAdded by remember { mutableStateOf(false) }
    var licuadoFinished by remember { mutableStateOf(false) }
    var hasLost by remember { mutableStateOf(false) }
    var showResults by remember { mutableStateOf(false) }
    var showStars by remember { mutableStateOf(false) }
    var showDarkOverlay by remember { mutableStateOf(false) }
    var isTransitioningAfterMilk by remember { mutableStateOf(false) }

    // Frutas
    val targetFruits = remember { mutableStateListOf<String>() }
    val remainingTargets = remember { mutableStateListOf<String>() }
    val pickedCorrect = remember { mutableStateListOf<String>() }
    val pickedIncorrect = remember { mutableStateListOf<String>() }
    val fruitsInsideBlender = remember { mutableStateListOf<DraggableFruit>() }
    val trayFruits = remember { mutableStateListOf<DraggableFruit>() }

    // Control de arrastre
    var draggedFruit by remember { mutableStateOf<DraggableFruit?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var blenderPosition by remember { mutableStateOf(Offset.Zero) }
    var blenderSize by remember { mutableStateOf(Offset(160f, 160f)) }
    var trayPosition by remember { mutableStateOf(Offset.Zero) }
    var traySize by remember { mutableStateOf(Offset.Zero) }

    // Timer de bandeja
    var trayTimerActive by remember { mutableStateOf(false) }

    // 1. Función para inicializar frutas en bandeja
    fun initializeTrayFruits() {
        val trayFruitNames = listOf("fresa", "uva", "manzana", "banana", "naranja")
        trayFruits.clear()
        repeat(5) {
            val newFruitName = trayFruitNames.random()
            trayFruits.add(DraggableFruit(
                name = newFruitName,
                imageRes = getFruitImageResource(newFruitName)
            ))
        }
    }

    // 2. Función para refrescar frutas en bandeja
    fun refreshTrayFruits() {
        if (gameState != GameState.PLAYING || licuadoFinished || !trayTimerActive) return
        if (draggedFruit != null) return

        val trayFruitNames = listOf("fresa", "uva", "manzana", "banana", "naranja")
        val newFruits = mutableListOf<DraggableFruit>()

        trayFruits.forEach { fruit ->
            if (fruitsInsideBlender.any { it.id == fruit.id }) {
                newFruits.add(fruit)
            } else {
                val newFruitName = trayFruitNames.random()
                newFruits.add(DraggableFruit(
                    name = newFruitName,
                    imageRes = getFruitImageResource(newFruitName)
                ))
            }
        }

        trayFruits.clear()
        trayFruits.addAll(newFruits)
    }

    // Timer de bandeja de frutas
    LaunchedEffect(trayTimerActive, gameState, licuadoFinished, draggedFruit) {
        while (trayTimerActive && gameState == GameState.PLAYING && !licuadoFinished) {
            delay(3000)

            val shouldRefresh = trayTimerActive &&
                gameState == GameState.PLAYING &&
                !licuadoFinished &&
                draggedFruit == null &&
                !milkAdded

            if (shouldRefresh) {
                refreshTrayFruits()
            }
        }
    }

    // 3. Función para inicializar el juego completo
    fun initializeGame() {
        targetFruits.clear()
        remainingTargets.clear()
        pickedCorrect.clear()
        pickedIncorrect.clear()
        fruitsInsideBlender.clear()
        trayFruits.clear()

        // Frutas objetivo (3 aleatorias)
        val bannerFruitNames = listOf("fresa", "uva", "manzana", "banana", "naranja")
        targetFruits.addAll((0..2).map { bannerFruitNames.random() })
        remainingTargets.addAll(targetFruits)

        // Frutas en la bandeja (5 aleatorias)
        initializeTrayFruits()

        gameState = GameState.PLAYING
        timeRemaining = 30
        milkAdded = false
        blenderEnabled = false
        isTransitioningAfterMilk = false
        isTriangleMoving = false
        trianglePosition = 0f
        licuadoFinished = false
        hasLost = false
        showResults = false
        showStars = false
        showDarkOverlay = false
        trayTimerActive = true

        draggedFruit = null
        dragOffset = Offset.Zero

        audioManager.startBackgroundMusic()
    }

    // 4. Animación del triángulo
    fun animateTriangle() {
        scope.launch {
            isTriangleMoving = true
            var direction = 1f
            var currentPosition = 0f
            val animationDuration = 1000L
            val frameDelay = 16L

            while (isTriangleMoving && gameState == GameState.PLAYING) {
                val frames = animationDuration / frameDelay
                val increment = 1f / frames

                currentPosition += increment * direction

                if (currentPosition >= 1f) {
                    currentPosition = 1f
                    direction = -1f
                } else if (currentPosition <= 0f) {
                    currentPosition = 0f
                    direction = 1f
                }

                trianglePosition = currentPosition
                delay(frameDelay)
            }
        }
    }

    // 5. Calcular resultados finales
    fun computeFinalFruitResults() {
        pickedCorrect.clear()
        pickedIncorrect.clear()

        val tempNeeded = targetFruits.toMutableList()

        fruitsInsideBlender.forEach { fruit ->
            if (tempNeeded.contains(fruit.name)) {
                pickedCorrect.add(fruit.name)
                tempNeeded.remove(fruit.name)
            } else {
                pickedIncorrect.add(fruit.name)
            }
        }

        remainingTargets.clear()
        remainingTargets.addAll(tempNeeded)
    }

    // 6. Calcular calidad de mezcla
    fun finalBlendQuality(): String {
        val percent = trianglePosition * 100
        return when {
            percent < 20 -> "Mala"
            percent < 40 -> "Media"
            percent < 60 -> "Buena"
            percent < 80 -> "Media"
            else -> "Mala"
        }
    }

    // 7. Calcular estrellas
    fun calculateStars(): Int {
        val correct = pickedCorrect.isNotEmpty()
        val incorrect = pickedIncorrect.isNotEmpty()
        val missing = remainingTargets.isNotEmpty()
        val calidad = finalBlendQuality()

        if (!correct) {
            if (incorrect && missing && calidad == "Buena") {
                return 1
            }
            return 0
        }

        var score = 0
        score += 1

        if (!incorrect && !missing) {
            score += 2
        } else if ((incorrect && !missing) || (!incorrect && missing)) {
            score += 1
        }

        when (calidad) {
            "Buena" -> score += 2
            "Media" -> score += 1
            "Mala" -> score += 0
        }

        return score.coerceAtMost(5)
    }

    // 8. Función cuando se pierde
    fun handleGameLost() {
        hasLost = true
        isTriangleMoving = false
        licuadoFinished = true
        trayTimerActive = false

        audioManager.playPoingSound()

        scope.launch {
            delay(1500)
            gameState = GameState.LOST
            showResults = true
        }
    }

    // 9. Función de reinicio
    fun resetGame() {
        audioManager.playButtonSound()

        licuadoFinished = false
        hasLost = false
        isTransitioningAfterMilk = false
        fruitsInsideBlender.clear()
        pickedCorrect.clear()
        pickedIncorrect.clear()
        remainingTargets.clear()
        targetFruits.clear()
        draggedFruit = null
        dragOffset = Offset.Zero

        gameState = GameState.PLAYING
        timeRemaining = 30
        milkAdded = false
        blenderEnabled = false
        isTriangleMoving = false
        trianglePosition = 0f
        showResults = false
        showStars = false
        showDarkOverlay = false
        trayTimerActive = true

        val bannerFruitNames = listOf("fresa", "uva", "manzana", "banana", "naranja")
        targetFruits.addAll((0..2).map { bannerFruitNames.random() })
        remainingTargets.addAll(targetFruits)

        initializeTrayFruits()
    }

    // 10. Transición primera fase - COMPLETAMENTE IMPLEMENTADA
    fun runFirstPhaseTransition() {
        audioManager.playMilkSound()

        milkAdded = true
        blenderEnabled = false
        trayTimerActive = false
        isTransitioningAfterMilk = true // Activar transición

        scope.launch {
            // Limpiar bandeja (como en iOS)
            trayFruits.clear()

            // Esperar 5 segundos como en iOS
            delay(5000)

            if (!hasLost) {
                // Terminar transición y habilitar licuadora
                isTransitioningAfterMilk = false
                blenderEnabled = true
                isTriangleMoving = true
                animateTriangle()
            }
        }
    }

    // 11. Encender licuadora
    fun encenderLicuadora() {
        if (isTriangleMoving) {
            audioManager.playBlenderSound()

            licuadoFinished = true
            isTriangleMoving = false
            blenderEnabled = false

            scope.launch {
                delay(3000)
                computeFinalFruitResults()
                gameState = GameState.FINISHED
                showResults = true
                showDarkOverlay = true

                delay(200)
                audioManager.playShineSound()

                delay(500)
                showStars = true
            }
        }
    }

    fun handleDrag(dragAmount: Offset) {
        dragOffset = Offset(
            x = dragOffset.x + dragAmount.x,
            y = dragOffset.y + dragAmount.y
        )
    }

    // 12. Manejador de arrastre
    fun handleDragStart(fruit: DraggableFruit, startPosition: Offset) {
        trayTimerActive = false
        draggedFruit = fruit
        dragOffset = startPosition + trayPosition
        audioManager.playButtonSound()
    }

    fun handleDragEnd() {
        draggedFruit?.let { fruit ->
            val fruitCenter = dragOffset
            val fruitWidth = 80f
            val fruitHeight = 80f

            val fruitLeft = fruitCenter.x - fruitWidth / 2
            val fruitTop = fruitCenter.y - fruitHeight / 2
            val fruitRight = fruitCenter.x + fruitWidth / 2
            val fruitBottom = fruitCenter.y + fruitHeight / 2

            val blenderLeft = blenderPosition.x - 30f
            val blenderTop = blenderPosition.y - 30f
            val blenderRight = blenderPosition.x + blenderSize.x + 30f
            val blenderBottom = blenderPosition.y + blenderSize.y + 30f

            val intersects = (fruitLeft < blenderRight &&
                fruitRight > blenderLeft &&
                fruitTop < blenderBottom &&
                fruitBottom > blenderTop)

            if (intersects) {
                val fruitCopy = fruit.copy(
                    id = UUID.randomUUID().toString(),
                    isCopy = true,
                    offset = Offset(blenderPosition.x + blenderSize.x / 2 - 40f,
                        blenderPosition.y + blenderSize.y / 2 - 40f)
                )
                fruitsInsideBlender.add(fruitCopy)
                trayFruits.removeAll { it.id == fruit.id }
                audioManager.playButtonSound()
            } else {
                audioManager.playPoingSound()
            }
        }

        draggedFruit = null
        dragOffset = Offset.Zero

        if (!milkAdded) {
            trayTimerActive = true
        }
    }

    // --- EFECTOS ---
    LaunchedEffect(timeRemaining, gameState, licuadoFinished) {
        if (gameState == GameState.PLAYING && !licuadoFinished && timeRemaining > 0) {
            delay(1000)
            if (gameState == GameState.PLAYING && !licuadoFinished) {
                timeRemaining--
                if (timeRemaining <= 0) {
                    handleGameLost()
                }
            }
        }
    }

    // Timer de bandeja de frutas
    LaunchedEffect(trayTimerActive, gameState, licuadoFinished) {
        while (trayTimerActive && gameState == GameState.PLAYING && !licuadoFinished) {
            delay(2000)
            if (trayTimerActive && gameState == GameState.PLAYING && !licuadoFinished && draggedFruit == null) {
                refreshTrayFruits()
            }
        }
    }

    // Inicializar juego
    LaunchedEffect(Unit) {
        initializeGame()
    }

    // Limpiar recursos al salir
    DisposableEffect(Unit) {
        onDispose {
            audioManager.stopAllSounds()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F4F8))
    ) {
        // FONDO CON TODOS LOS PARÁMETROS CORRECTOS
        Image(
            painter = painterResource(id = getBackgroundImage(
                gameState = gameState,
                milkAdded = milkAdded,
                hasLost = hasLost,
                licuadoFinished = licuadoFinished,
                isTransitioningAfterMilk = isTransitioningAfterMilk
            )),
            contentDescription = "Fondo del juego",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay oscuro para resultados
        if (showDarkOverlay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )
        }

        when (gameState) {
            GameState.PLAYING -> {
                PlayingStateUI(
                    onBackClick = {
                        audioManager.playButtonSound()
                        audioManager.stopAllSounds()
                        onBackClick()
                    },
                    timeRemaining = timeRemaining,
                    blenderSize = blenderSize,
                    targetFruits = targetFruits,
                    trayFruits = trayFruits,
                    draggedFruit = draggedFruit,
                    dragOffset = dragOffset,
                    milkAdded = milkAdded,
                    blenderEnabled = blenderEnabled,
                    trianglePosition = trianglePosition,
                    isTriangleMoving = isTriangleMoving,
                    fruitsInsideBlender = fruitsInsideBlender,
                    onMilkClick = { runFirstPhaseTransition() },
                    onBlenderClick = { encenderLicuadora() },
                    onFruitDragStart = { fruit, position -> handleDragStart(fruit, position) },
                    onFruitDrag = { dragAmount -> handleDrag(dragAmount) },
                    onFruitDragEnd = { handleDragEnd() },
                    onBlenderPositioned = { position, size ->
                        blenderPosition = position
                        blenderSize = size
                    },
                    onTrayPositioned = { position, size ->
                        trayPosition = position
                        traySize = size
                    },
                    showBanner = !milkAdded
                )
            }
            GameState.FINISHED -> {
                FinishedStateUI(
                    onBackClick = {
                        audioManager.playButtonSound()
                        audioManager.stopAllSounds()
                        onBackClick()
                    },
                    targetFruits = targetFruits,
                    pickedCorrect = pickedCorrect,
                    pickedIncorrect = pickedIncorrect,
                    remainingTargets = remainingTargets,
                    blendQuality = finalBlendQuality(),
                    stars = calculateStars(),
                    showStars = showStars,
                    onRestart = { resetGame() }
                )
            }
            GameState.LOST -> {
                LostStateUI(
                    onBackClick = {
                        audioManager.playButtonSound()
                        audioManager.stopAllSounds()
                        onBackClick()
                    },
                    onRestart = { resetGame() },
                    showResults = showResults
                )
            }
        }
    }
}

@Composable
private fun PlayingStateUI(
    onBackClick: () -> Unit,
    timeRemaining: Int,
    blenderSize: Offset,
    targetFruits: List<String>,
    trayFruits: List<DraggableFruit>,
    draggedFruit: DraggableFruit?,
    dragOffset: Offset,
    milkAdded: Boolean,
    blenderEnabled: Boolean,
    trianglePosition: Float,
    isTriangleMoving: Boolean,
    fruitsInsideBlender: List<DraggableFruit>,
    onMilkClick: () -> Unit,
    onBlenderClick: () -> Unit,
    onFruitDragStart: (DraggableFruit, Offset) -> Unit,
    onFruitDrag: (Offset) -> Unit,
    onFruitDragEnd: () -> Unit,
    onBlenderPositioned: (Offset, Offset) -> Unit,
    onTrayPositioned: (Offset, Offset) -> Unit,
    showBanner: Boolean = true
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )

        TimerDisplay(
            timeRemaining = timeRemaining,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 90.dp, start = 50.dp)
        )

        if (showBanner) {
            TargetFruitsBanner(
                targetFruits = targetFruits,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 30.dp)
            )
        }

        // Licuadora
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-110).dp)
                .onGloballyPositioned { coordinates ->
                    val position = coordinates.localToWindow(Offset.Zero)
                    val size = Offset(coordinates.size.width.toFloat(), coordinates.size.height.toFloat())
                    onBlenderPositioned(position, size)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.licuadora),
                contentDescription = "Licuadora",
                modifier = Modifier
                    .size(160.dp)
                    .offset(x = (7).dp, y = 30.dp)
                    .clickable(enabled = blenderEnabled) { onBlenderClick() },
                contentScale = ContentScale.Fit,
                alpha = if (blenderEnabled) 1f else 0.3f
            )

            // Frutas dentro de la licuadora
            fruitsInsideBlender.forEachIndexed { index, fruit ->
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .offset {
                            val angle = (index * 72).toDouble()
                            val radius = 30f
                            val x = (blenderSize.x / 2 - 30f) + (radius * kotlin.math.cos(Math.toRadians(angle))).toFloat()
                            val y = (blenderSize.y / 2 - 30f) + (radius * kotlin.math.sin(Math.toRadians(angle))).toFloat()
                            IntOffset(x.roundToInt(), y.roundToInt())
                        }
                ) {
                    Image(
                        painter = painterResource(id = fruit.imageRes),
                        contentDescription = "Fruta en licuadora: ${fruit.name}",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                rotationZ = (index * 15).toFloat()
                                scaleX = 0.8f
                                scaleY = 0.8f
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        // Leche - solo visible antes de agregarla
        if (!milkAdded) {
            Image(
                painter = painterResource(id = R.drawable.leche),
                contentDescription = "Leche",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 160.dp)
                    .size(140.dp)
                    .clickable { onMilkClick() },
                contentScale = ContentScale.Fit
            )
        }

        // Barra de progreso y triángulo
        if (milkAdded && isTriangleMoving) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 200.dp)
                    .width(300.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.barralicuado),
                    contentDescription = "Barra de progreso",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                Image(
                    painter = painterResource(id = R.drawable.triangulo),
                    contentDescription = "Triángulo",
                    modifier = Modifier
                        .size(30.dp)
                        .offset(x = (trianglePosition * 270).dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Botón licuadora
        Image(
            painter = painterResource(id = R.drawable.botonlicuadora),
            contentDescription = "Botón licuadora",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 140.dp, bottom = 150.dp)
                .size(100.dp, 25.dp)
                .clickable(enabled = blenderEnabled) { onBlenderClick() },
            contentScale = ContentScale.FillBounds,
            alpha = if (blenderEnabled) 1f else 0.5f
        )

        // Bandeja de frutas - solo visible antes de agregar leche
        if (!milkAdded) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFF994F15))
                    .onGloballyPositioned { coordinates ->
                        val position = coordinates.localToWindow(Offset.Zero)
                        val size = Offset(coordinates.size.width.toFloat(), coordinates.size.height.toFloat())
                        onTrayPositioned(position, size)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    trayFruits.forEach { fruit ->
                        StableDraggableFruitItem(
                            fruit = fruit,
                            onDragStart = { startPosition -> onFruitDragStart(fruit, startPosition) },
                            onDrag = onFruitDrag,
                            onDragEnd = onFruitDragEnd
                        )
                    }
                }
            }
        }

        // Fruta arrastrada
        draggedFruit?.let { fruit ->
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        translationX = dragOffset.x - 40f
                        translationY = dragOffset.y - 40f
                    }
            ) {
                Image(
                    painter = painterResource(id = fruit.imageRes),
                    contentDescription = "Arrastrando ${fruit.name}",
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(16.dp, shape = RoundedCornerShape(20.dp))
                        .graphicsLayer {
                            rotationZ = 8f
                            scaleX = 1.15f
                            scaleY = 1.15f
                            alpha = 0.9f
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun StableDraggableFruitItem(
    fruit: DraggableFruit,
    onDragStart: (Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    val currentFruit = remember(fruit.id) { fruit }

    Box(
        modifier = Modifier
            .size(80.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        onDragStart(offset)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                    onDragEnd = {
                        isDragging = false
                        onDragEnd()
                    },
                    onDragCancel = {
                        isDragging = false
                        onDragEnd()
                    }
                )
            }
    ) {
        Image(
            painter = painterResource(id = currentFruit.imageRes),
            contentDescription = currentFruit.name,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = if (isDragging) 1.1f else 1f
                    scaleY = if (isDragging) 1.1f else 1f
                    alpha = if (isDragging) 0.7f else 1f
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun FinishedStateUI(
    onBackClick: () -> Unit,
    targetFruits: List<String>,
    pickedCorrect: List<String>,
    pickedIncorrect: List<String>,
    remainingTargets: List<String>,
    blendQuality: String,
    stars: Int,
    showStars: Boolean,
    onRestart: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondolicuado4),
            contentDescription = "Fondo resultados",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        )

        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(40.dp)
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Frutas necesarias: ${targetFruits.joinToString(", ")}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Correctas: ${pickedCorrect.joinToString(", ")}",
                color = Color.Green,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Incorrectas: ${pickedIncorrect.joinToString(", ")}",
                color = Color.Red,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Faltantes: ${remainingTargets.joinToString(", ")}",
                color = Color.Yellow,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Calidad de la mezcla: $blendQuality",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (showStars) {
                Image(
                    painter = painterResource(id = getStarsImageResource(stars)),
                    contentDescription = "Estrellas: $stars",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(200.dp, 100.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                StarsDisplay(
                    stars = stars,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A3D))
            ) {
                Text("Jugar de Nuevo", color = Color.White)
            }
        }
    }
}

@Composable
private fun LostStateUI(
    onBackClick: () -> Unit,
    onRestart: () -> Unit,
    showResults: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.perdiste),
            contentDescription = "Game Over",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (showResults) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Tiempo Agotado!",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No lograste completar el licuado a tiempo",
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A3D))
            ) {
                Text("Reintentar", color = Color.White, fontSize = 18.sp)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )

        // Botón siguiente (como en iOS)
        if (showResults) {
            Image(
                painter = painterResource(id = R.drawable.siguientelicuado),
                contentDescription = "Siguiente",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 30.dp)
                    .size(150.dp, 45.dp)
                    .clickable { onRestart() },
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun TimerDisplay(timeRemaining: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(80.dp, 60.dp)
            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
    ) {
        Text(
            text = "$timeRemaining",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun TargetFruitsBanner(targetFruits: List<String>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "Banner frutas objetivo",
            modifier = Modifier.size(200.dp, 100.dp),
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            targetFruits.forEach { fruitName ->
                Image(
                    painter = painterResource(id = getFruitImageResource(fruitName)),
                    contentDescription = fruitName,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun StarsDisplay(stars: Int, modifier: Modifier = Modifier) {
    Text(
        text = "⭐".repeat(stars) + "☆".repeat(5 - stars),
        fontSize = 32.sp,
        color = Color.Yellow,
        modifier = modifier
    )
}

// PREVIEWS
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SmoothieMakerScreenPreview() {
    SmoothieMakerScreen()
}
