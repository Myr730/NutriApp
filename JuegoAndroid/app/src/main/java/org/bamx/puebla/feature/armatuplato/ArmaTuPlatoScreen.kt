// PlatoScreen.kt
package org.bamx.puebla.feature.armatuplato

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import java.util.*
import kotlin.collections.get
import android.content.Context

// Sistema de audio para plato
class PlatoAudioManager(private val context: Context) {
    fun playButtonSound() {
        // Implementar sonido de botones
    }

    fun startBackgroundMusic() {
        // Implementar música de fondo del plato
    }

    fun stopBackgroundMusic() {
        // Detener música
    }

    fun stopAllSounds() {
        // Detener todos los sonidos
    }
}

// Enums y modelos
enum class FoodCategory {
    CARBOHIDRATOS, FRUTAS, VERDURAS, ORIGEN_ANIMAL, BEBIDAS, POSTRES
}

data class FoodItem(
    val id: String = UUID.randomUUID().toString(),
    val category: FoodCategory,
    val name: String,
    val imageRes: Int,
    val feedback: String,
    var offset: Offset = Offset.Zero,
    var isDragging: Boolean = false
)

// Feedback database
private val carbohidratosFeedback = mapOf(
    "carbohidratos1" to "El pan da energía, evita combinarlo con muchas harinas.",
    "carbohidratos2" to "Los frijoles aportan proteína vegetal y fibra.",
    "carbohidratos3" to "El arroz blanco tiene poca fibra. Recuerda comerlo con verduras.",
    "carbohidratos4" to "Las papas cambray son una buena fuente de energía.",
    "carbohidratos5" to "La tortilla de maíz es natural y rica en fibra.",
    "carbohidratos6" to "El arroz rojo puede llevar aceite. Consúmelo con moderación.",
    "carbohidratos7" to "El espagueti aporta energía, pero evita porciones grandes.",
    "carbohidratos8" to "El pan integral tiene mucha fibra en porciones pequeñas.",
    "carbohidratos9" to "El camote aporta energía y fibra. Es muy buena opción.",
    "carbohidratos10" to "El elote es un cereal natural y saludable."
)

private val frutasFeedback = mapOf(
    "frutas1" to "La tuna es refrescante y rica en fibra.",
    "frutas2" to "El plátano tiene potasio. Excelente antes de actividad física.",
    "frutas3" to "La sandía es hidratante y ligera.",
    "frutas4" to "La pera es buena para la digestión.",
    "frutas5" to "Las uvas son dulces; ten moderación si agregaste postre.",
    "frutas6" to "El aguacate tiene grasas saludables.",
    "frutas7" to "La fresa es ligera y baja en azúcar.",
    "frutas8" to "La manzana es rica en fibra. ¡Buena elección!",
    "frutas9" to "El maracuyá es ácido y muy nutritivo.",
    "frutas10" to "La naranja aporta vitamina C; evítala si tienes otras azúcares."
)

private val verdurasFeedback = mapOf(
    "verduras1" to "Los champiñones son bajos en calorías y tienen muy buen sabor.",
    "verduras2" to "La calabaza es ligera y nutritiva.",
    "verduras3" to "La cebolla aporta sabor y antioxidantes.",
    "verduras4" to "El pimiento rojo aporta mucha vitamina C.",
    "verduras5" to "El brócoli es excelente: fibra, vitamins y minerales.",
    "verduras6" to "El tomate es fresco y ligero.",
    "verduras7" to "El jalapeño da sabor, pero recuerda agregar más verduras.",
    "verduras8" to "La lechuga es fresca, pero ligera; combínala con más verduras.",
    "verduras9" to "El nopal es excelente para la digestión y la glucosa.",
    "verduras10" to "La zanahoria aporta vitamina A y fibra."
)

private val origenAnimalFeedback = mapOf(
    "origenanimal1" to "El bistec aporta proteína, pero también grasa. Cómelo con moderación.",
    "origenanimal2" to "El pescado es una excelente proteína baja en grasa. ¡Muy bien!",
    "origenanimal3" to "El pollo es una buena fuente de proteína.",
    "origenanimal4" to "El camarón es ligero, pero recuerda tomar porciones pequeñas.",
    "origenanimal5" to "La salchicha es un procesado, intenta no usarla tan seguido.",
    "origenanimal6" to "El huevo estrellado es nutritivo, pero evita usar mucho aceite.",
    "origenanimal7" to "El yogurt natural es buena fuente de calcio.",
    "origenanimal8" to "El queso Oaxaca es sabroso, pero moderado en grasa.",
    "origenanimal9" to "El huevo hervido es excelente opción: proteína sin aceite.",
    "origenanimal10" to "El queso amarillo es un procesado. Mejor limitarlo."
)

@Composable
fun PlatoScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val audioManager = remember { PlatoAudioManager(context) }

    // Estados principales
    var currentCategory by remember { mutableStateOf<FoodCategory?>(null) }
    var instruccionesIndex by remember { mutableIntStateOf(1) }
    var bebidasAndPostresUnlocked by remember { mutableStateOf(false) }
    var interactionLocked by remember { mutableStateOf(false) }
    var showFinalResults by remember { mutableStateOf(false) }

    // Índices para cada categoría
    val indexes = remember {
        mutableStateMapOf(
            FoodCategory.CARBOHIDRATOS to 1,
            FoodCategory.FRUTAS to 1,
            FoodCategory.VERDURAS to 1,
            FoodCategory.ORIGEN_ANIMAL to 1,
            FoodCategory.BEBIDAS to 1,
            FoodCategory.POSTRES to 1
        )
    }

    // Items en el plato
    val droppedItems = remember { mutableStateListOf<FoodItem>() }
    var draggedItem by remember { mutableStateOf<FoodItem?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    // Estados de visualización
    var isShowingBebidas by remember { mutableStateOf(false) }
    var isShowingPostres by remember { mutableStateOf(false) }
    var detallesPorAlimento by remember { mutableStateOf("") }

    // Área del plato y basura para el arrastre
    var platoBounds by remember { mutableStateOf<Rect?>(null) }
    var basuraBounds by remember { mutableStateOf<Rect?>(null) }

    // Función para obtener imagen de alimento
    fun getFoodImageResource(category: FoodCategory, index: Int): Int {
        return when (category) {
            FoodCategory.CARBOHIDRATOS -> when (index) {
                1 -> R.drawable.carbohidratos1
                2 -> R.drawable.carbohidratos2
                3 -> R.drawable.carbohidratos3
                4 -> R.drawable.carbohidratos4
                5 -> R.drawable.carbohidratos5
                6 -> R.drawable.carbohidratos6
                7 -> R.drawable.carbohidratos7
                8 -> R.drawable.carbohidratos8
                9 -> R.drawable.carbohidratos9
                10 -> R.drawable.carbohidratos10
                else -> R.drawable.carbohidratos1
            }
            FoodCategory.FRUTAS -> when (index) {
                1 -> R.drawable.frutas1
                2 -> R.drawable.frutas2
                3 -> R.drawable.frutas3
                4 -> R.drawable.frutas4
                5 -> R.drawable.frutas5
                6 -> R.drawable.frutas6
                7 -> R.drawable.frutas7
                8 -> R.drawable.frutas8
                9 -> R.drawable.frutas9
                10 -> R.drawable.frutas10
                else -> R.drawable.frutas1
            }
            FoodCategory.VERDURAS -> when (index) {
                1 -> R.drawable.verduras1
                2 -> R.drawable.verduras2
                3 -> R.drawable.verduras3
                4 -> R.drawable.verduras4
                5 -> R.drawable.verduras5
                6 -> R.drawable.verduras6
                7 -> R.drawable.verduras7
                8 -> R.drawable.verduras8
                9 -> R.drawable.verduras9
                10 -> R.drawable.verduras10
                else -> R.drawable.verduras1
            }
            FoodCategory.ORIGEN_ANIMAL -> when (index) {
                1 -> R.drawable.origenanimal1
                2 -> R.drawable.origenanimal2
                3 -> R.drawable.origenanimal3
                4 -> R.drawable.origenanimal4
                5 -> R.drawable.origenanimal5
                6 -> R.drawable.origenanimal6
                7 -> R.drawable.origenanimal7
                8 -> R.drawable.origenanimal8
                9 -> R.drawable.origenanimal9
                10 -> R.drawable.origenanimal10
                else -> R.drawable.origenanimal1
            }
            FoodCategory.BEBIDAS -> when (index) {
                1 -> R.drawable.bebida1
                2 -> R.drawable.bebida2
                3 -> R.drawable.bebida3
                4 -> R.drawable.bebida4
                5 -> R.drawable.bebida5
                6 -> R.drawable.bebida6
                7 -> R.drawable.bebida7
                else -> R.drawable.bebida1
            }
            FoodCategory.POSTRES -> when (index) {
                1 -> R.drawable.food_cookie
                2 -> R.drawable.postre2
                3 -> R.drawable.postre3
                4 -> R.drawable.postre4
                5 -> R.drawable.postre5
                else -> R.drawable.postre1
            }
        }
    }

    // Función para obtener feedback
    fun getFoodFeedback(category: FoodCategory, index: Int): String {
        val key = "${category.name.lowercase()}$index"
        return when (category) {
            FoodCategory.CARBOHIDRATOS -> carbohidratosFeedback[key] ?: ""
            FoodCategory.FRUTAS -> frutasFeedback[key] ?: ""
            FoodCategory.VERDURAS -> verdurasFeedback[key] ?: ""
            FoodCategory.ORIGEN_ANIMAL -> origenAnimalFeedback[key] ?: ""
            else -> ""
        }
    }

    // Función para contar categorías en el plato
    fun countCategoriesInPlato(): Map<FoodCategory, Int> {
        val counts = mutableMapOf<FoodCategory, Int>()
        FoodCategory.values().forEach { category ->
            counts[category] = 0
        }

        droppedItems.forEach { item ->
            counts[item.category] = counts.getOrDefault(item.category, 0) + 1
        }

        return counts
    }

    // Función para evaluar proporciones del plato
    fun evaluatePlateProportions(counts: Map<FoodCategory, Int>): String {
        val total = droppedItems.size
        if (total == 0) return ""

        val frutasVerduras = (counts[FoodCategory.FRUTAS] ?: 0) + (counts[FoodCategory.VERDURAS] ?: 0)
        val carbohidratos = counts[FoodCategory.CARBOHIDRATOS] ?: 0
        val origenAnimal = counts[FoodCategory.ORIGEN_ANIMAL] ?: 0

        var feedback = ""

        // Evaluar frutas y verduras
        if (frutasVerduras.toDouble() / total < 0.4) {
            feedback += "• Las frutas y verduras deben ser aproximadamente la mitad del plato.\n"
        }

        // Evaluar carbohidratos
        if (carbohidratos.toDouble() / total > 0.3) {
            feedback += "• Intenta que tus carbohidratos no sean más de un cuarto del plato.\n"
        } else if (carbohidratos.toDouble() / total < 0.15) {
            feedback += "• Te faltan carbohidratos para tener suficiente energía.\n"
        }

        // Evaluar proteína animal
        if (origenAnimal.toDouble() / total > 0.3) {
            feedback += "• Tienes mucha proteína animal. Recuerda usar porciones pequeñas.\n"
        } else if (origenAnimal.toDouble() / total < 0.15) {
            feedback += "• Tienes poca proteína animal, podrías agregar más si lo necesitas.\n"
        }

        return feedback
    }

    // FUNCIÓN computeFinalResults
    fun computeFinalResults() {
        detallesPorAlimento = "Detalles de tus alimentos:\n\n"
        droppedItems.forEach { item ->
            detallesPorAlimento += "• ${item.feedback}\n\n"
        }

        // Evaluar proporciones del plato
        val counts = countCategoriesInPlato()
        val feedbackProporciones = evaluatePlateProportions(counts)
        if (feedbackProporciones.isNotEmpty()) {
            detallesPorAlimento += "Recomendaciones de proporción:\n$feedbackProporciones"
        }
    }

    // Función para mostrar categoría
    fun showCategory(category: FoodCategory) {
        currentCategory = category
        audioManager.playButtonSound()
    }

    // Función para cerrar categoría
    fun closeCategory() {
        currentCategory = null
        isShowingBebidas = false
        isShowingPostres = false
        audioManager.playButtonSound()
    }

    // Función para siguiente item
    fun nextItem() {
        audioManager.playButtonSound()
        currentCategory?.let { category ->
            val currentIndex = indexes[category] ?: 1
            val maxItems = when (category) {
                FoodCategory.BEBIDAS -> 7
                FoodCategory.POSTRES -> 5
                else -> 10
            }
            val newIndex = if (currentIndex >= maxItems) 1 else currentIndex + 1
            indexes[category] = newIndex
        }
    }

    // Función para item anterior
    fun previousItem() {
        audioManager.playButtonSound()
        currentCategory?.let { category ->
            val currentIndex = indexes[category] ?: 1
            val maxItems = when (category) {
                FoodCategory.BEBIDAS -> 7
                FoodCategory.POSTRES -> 5
                else -> 10
            }
            val newIndex = if (currentIndex <= 1) maxItems else currentIndex - 1
            indexes[category] = newIndex
        }
    }

    // Función para manejar arrastre - SIMPLIFICADA
    fun handleDragStart(item: FoodItem, startPosition: Offset) {
        // Solo permitir un arrastre a la vez
        if (draggedItem != null) return

        draggedItem = item
        dragOffset = startPosition
        audioManager.playButtonSound()
    }

    fun handleDrag(dragAmount: Offset) {
        dragOffset = Offset(
            x = dragOffset.x + dragAmount.x,
            y = dragOffset.y + dragAmount.y
        )
    }

    fun handleDragEnd() {
        draggedItem?.let { item ->
            // Verificar si se soltó sobre la basura
            val isOnTrash = basuraBounds?.contains(dragOffset) ?: false

            if (!isOnTrash) {
                // Si no está en la basura, agregar al plato
                val adjustedOffset = if (platoBounds?.contains(dragOffset) == true) {
                    // Si está dentro del plato, ajustar la posición
                    Offset(
                        x = dragOffset.x - 40f,
                        y = dragOffset.y - 40f
                    )
                } else {
                    // Si está fuera del plato, usar la posición exacta
                    dragOffset
                }

                val newItem = item.copy(
                    id = UUID.randomUUID().toString(),
                    offset = adjustedOffset
                )
                droppedItems.add(newItem)
            }
            // Si está en la basura, no hacer nada
        }

        draggedItem = null
        dragOffset = Offset.Zero
    }

    // Función para tocar siguiente
    fun didTapSiguiente() {
        audioManager.playButtonSound()
        instruccionesIndex = 2
        bebidasAndPostresUnlocked = true
    }

    // Función para tocar listo
    fun didTapListo() {
        audioManager.playButtonSound()
        interactionLocked = true
        showFinalResults = true
        computeFinalResults()
    }

    // Función para abrir bebidas
    fun openBebidas() {
        if (interactionLocked) return
        audioManager.playButtonSound()
        isShowingBebidas = true
        isShowingPostres = false
        currentCategory = FoodCategory.BEBIDAS
    }

    // Función para abrir postres
    fun openPostres() {
        if (interactionLocked) return
        audioManager.playButtonSound()
        isShowingPostres = true
        isShowingBebidas = false
        currentCategory = FoodCategory.POSTRES
    }

    // Efectos
    LaunchedEffect(Unit) {
        audioManager.startBackgroundMusic()
    }

    DisposableEffect(Unit) {
        onDispose {
            audioManager.stopAllSounds()
        }
    }

    // UI Principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Fondo
        Image(
            painter = painterResource(
                id = when {
                    showFinalResults -> R.drawable.fondoplato2
                    else -> R.drawable.fondoplato
                }
            ),
            contentDescription = "Fondo del plato",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        when {
            showFinalResults -> FinalResultsUI(
                detallesPorAlimento = detallesPorAlimento,
                onBackClick = onBackClick
            )
            else -> MainGameUI(
                onBackClick = onBackClick,
                currentCategory = currentCategory,
                instruccionesIndex = instruccionesIndex,
                bebidasAndPostresUnlocked = bebidasAndPostresUnlocked,
                interactionLocked = interactionLocked,
                indexes = indexes,
                droppedItems = droppedItems,
                draggedItem = draggedItem,
                dragOffset = dragOffset,
                isShowingBebidas = isShowingBebidas,
                isShowingPostres = isShowingPostres,
                onCategoryClick = ::showCategory,
                onCloseCategory = ::closeCategory,
                onNextItem = ::nextItem,
                onPreviousItem = ::previousItem,
                onSiguienteClick = ::didTapSiguiente,
                onListoClick = ::didTapListo,
                onBebidasClick = ::openBebidas,
                onPostresClick = ::openPostres,
                onDragStart = ::handleDragStart,
                onDrag = ::handleDrag,
                onDragEnd = ::handleDragEnd,
                getFoodImageResource = ::getFoodImageResource,
                getFoodFeedback = ::getFoodFeedback,
                onPlatoBoundsUpdate = { bounds -> platoBounds = bounds },
                onBasuraBoundsUpdate = { bounds -> basuraBounds = bounds }
            )
        }
    }
}

@Composable
private fun MainGameUI(
    onBackClick: () -> Unit,
    currentCategory: FoodCategory?,
    instruccionesIndex: Int,
    bebidasAndPostresUnlocked: Boolean,
    interactionLocked: Boolean,
    indexes: Map<FoodCategory, Int>,
    droppedItems: List<FoodItem>,
    draggedItem: FoodItem?,
    dragOffset: Offset,
    isShowingBebidas: Boolean,
    isShowingPostres: Boolean,
    onCategoryClick: (FoodCategory) -> Unit,
    onCloseCategory: () -> Unit,
    onNextItem: () -> Unit,
    onPreviousItem: () -> Unit,
    onSiguienteClick: () -> Unit,
    onListoClick: () -> Unit,
    onBebidasClick: () -> Unit,
    onPostresClick: () -> Unit,
    onDragStart: (FoodItem, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    getFoodImageResource: (FoodCategory, Int) -> Int,
    getFoodFeedback: (FoodCategory, Int) -> String,
    onPlatoBoundsUpdate: (Rect?) -> Unit,
    onBasuraBoundsUpdate: (Rect?) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Botón de volver
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 16.dp, y = 16.dp)
                .size(52.dp)
                .clickable { onBackClick() }
        )

        // Label "Arma tu plato"
        Image(
            painter = painterResource(id = R.drawable.armaplato),
            contentDescription = "Arma tu plato",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-16).dp, y = 16.dp)
                .size(250.dp, 80.dp)
        )

        // Instrucciones
        Image(
            painter = painterResource(
                id = when (instruccionesIndex) {
                    1 -> R.drawable.instrucciones1
                    2 -> R.drawable.instrucciones2
                    3 -> R.drawable.instrucciones3
                    else -> R.drawable.instrucciones1
                }
            ),
            contentDescription = "Instrucciones",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-16).dp, y = 100.dp)
                .size(250.dp, 150.dp)
        )

        // Plato principal
        Image(
            painter = painterResource(id = R.drawable.plato),
            contentDescription = "Plato",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (50).dp)
                .size(350.dp, 200.dp)
                .onGloballyPositioned { coordinates ->
                    onPlatoBoundsUpdate(coordinates.boundsInWindow())
                }
        )

        // Basura
        Image(
            painter = painterResource(id = R.drawable.basura),
            contentDescription = "Basura",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-50).dp, y = 250.dp)
                .size(110.dp, 140.dp)
                .onGloballyPositioned { coordinates ->
                    onBasuraBoundsUpdate(coordinates.boundsInWindow())
                }
        )

        // Items en el plato
        droppedItems.forEach { item ->
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        translationX = item.offset.x
                        translationY = item.offset.y
                    }
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Item siendo arrastrado
        draggedItem?.let { item ->
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        translationX = dragOffset.x
                        translationY = dragOffset.y
                    }
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = "Arrastrando ${item.name}",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                            alpha = 0.8f
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }

        // BANDEJA INFERIOR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
        ) {
            // Manta como fondo
            Image(
                painter = painterResource(id = R.drawable.blanket),
                contentDescription = "Manta",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-50).dp)
                    .align(Alignment.BottomCenter)
            )
            Image(
                painter = painterResource(id = R.drawable.blanket),
                contentDescription = "Manta",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (28).dp)
                    .align(Alignment.BottomCenter)
            )

            // CONTENIDO DE LA BANDEJA
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                when {
                    // Si hay una categoría seleccionada, mostrar la interfaz de selección
                    currentCategory != null -> {
                        CategorySelectionUI(
                            currentCategory = currentCategory!!,
                            index = indexes[currentCategory] ?: 1,
                            isShowingBebidas = isShowingBebidas,
                            isShowingPostres = isShowingPostres,
                            onCloseCategory = onCloseCategory,
                            onNextItem = onNextItem,
                            onPreviousItem = onPreviousItem,
                            onDragStart = onDragStart,
                            onDrag = onDrag,
                            onDragEnd = onDragEnd,
                            getFoodImageResource = getFoodImageResource,
                            getFoodFeedback = getFoodFeedback
                        )
                    }
                    // Si no hay categoría seleccionada pero están desbloqueadas bebidas/postres
                    bebidasAndPostresUnlocked -> {
                        BebidasPostresUI(
                            onBebidasClick = onBebidasClick,
                            onPostresClick = onPostresClick,
                            onListoClick = onListoClick
                        )
                    }
                    // Estado inicial - mostrar categorías principales
                    else -> {
                        FoodCategoriesGrid(onCategoryClick = onCategoryClick)
                    }
                }

                // Botón siguiente (solo en estado inicial)
                if (!bebidasAndPostresUnlocked && currentCategory == null) {
                    Image(
                        painter = painterResource(id = R.drawable.siguiente),
                        contentDescription = "Siguiente",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = 1.dp, y = (-200).dp)
                            .size(200.dp, 70.dp)
                            .clickable { onSiguienteClick() }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySelectionUI(
    currentCategory: FoodCategory,
    index: Int,
    isShowingBebidas: Boolean,
    isShowingPostres: Boolean,
    onCloseCategory: () -> Unit,
    onNextItem: () -> Unit,
    onPreviousItem: () -> Unit,
    onDragStart: (FoodItem, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    getFoodImageResource: (FoodCategory, Int) -> Int,
    getFoodFeedback: (FoodCategory, Int) -> String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        // Plato pequeño centrado
        Image(
            painter = painterResource(id = R.drawable.platito),
            contentDescription = "Plato pequeño",
            modifier = Modifier
                .size(220.dp, 130.dp)
                .align(Alignment.Center)
        )

        // Imagen actual de la categoría - MEJORADO para postres
        val foodItem = FoodItem(
            category = currentCategory,
            name = "${currentCategory.name}$index",
            imageRes = getFoodImageResource(currentCategory, index),
            feedback = getFoodFeedback(currentCategory, index)
        )

        // AJUSTES MEJORADOS PARA CENTRADO
        val imageSize = when (currentCategory) {
            FoodCategory.BEBIDAS -> 150.dp
            FoodCategory.POSTRES -> 140.dp
            else -> 140.dp
        }

        // OFFSET VERTICAL
        val verticalOffset = when (currentCategory) {
            FoodCategory.BEBIDAS -> (-8).dp
            FoodCategory.POSTRES -> (-25).dp
            else -> (-10).dp
        }

        DraggableFoodItem(
            foodItem = foodItem,
            onDragStart = onDragStart,
            onDrag = onDrag,
            onDragEnd = onDragEnd,
            modifier = Modifier
                .size(imageSize)
                .align(Alignment.Center)
                .offset(y = verticalOffset)
        )

        // Flecha izquierda
        Image(
            painter = painterResource(id = R.drawable.arrowl),
            contentDescription = "Anterior",
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-15).dp)
                .clickable { onPreviousItem() }
        )

        // Flecha derecha
        Image(
            painter = painterResource(id = R.drawable.arrowr),
            contentDescription = "Siguiente",
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 15.dp)
                .clickable { onNextItem() }
        )

        // Botón cerrar
        Image(
            painter = painterResource(id = R.drawable.close),
            contentDescription = "Cerrar",
            modifier = Modifier
                .size(45.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-10).dp, y = 10.dp)
                .clickable { onCloseCategory() }
        )
    }
}

@Composable
private fun FoodCategoriesGrid(onCategoryClick: (FoodCategory) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fila 1: Carbohidratos y Frutas (tamaño normal)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FoodCategoryButton(
                category = FoodCategory.CARBOHIDRATOS,
                imageRes = R.drawable.carbohidratos,
                size = 130.dp,
                onClick = onCategoryClick
            )
            FoodCategoryButton(
                category = FoodCategory.FRUTAS,
                imageRes = R.drawable.frutas,
                size = 130.dp,
                onClick = onCategoryClick
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila 2: Origen Animal y Verduras
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FoodCategoryButton(
                category = FoodCategory.ORIGEN_ANIMAL,
                imageRes = R.drawable.origenanimal,
                size = 130.dp,
                onClick = onCategoryClick
            )
            FoodCategoryButton(
                category = FoodCategory.VERDURAS,
                imageRes = R.drawable.verduras,
                size = 130.dp,
                onClick = onCategoryClick
            )
        }
    }
}

// FoodCategoryButton modificado para aceptar tamaño personalizado
@Composable
private fun FoodCategoryButton(
    category: FoodCategory,
    imageRes: Int,
    size: Dp = 150.dp, // Tamaño por defecto
    onClick: (FoodCategory) -> Unit
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = category.name,
        modifier = Modifier
            .size(size)
            .offset(y = (-10).dp)
            .clickable { onClick(category) }
    )
}

@Composable
private fun BebidasPostresUI(
    onBebidasClick: () -> Unit,
    onPostresClick: () -> Unit,
    onListoClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Botones de Bebidas y Postres
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bebidas),
                    contentDescription = "Bebidas",
                    modifier = Modifier
                        .size(180.dp, 120.dp)
                        .clickable { onBebidasClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.postres),
                    contentDescription = "Postres",
                    modifier = Modifier
                        .size(180.dp, 120.dp)
                        .clickable { onPostresClick() }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Botón Listo
            Image(
                painter = painterResource(id = R.drawable.listo),
                contentDescription = "Listo",
                modifier = Modifier
                    .size(100.dp, 35.dp)
                    .clickable { onListoClick() }
            )
        }
    }
}

@Composable
private fun DraggableFoodItem(
    foodItem: FoodItem,
    onDragStart: (FoodItem, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        // Solo permitir arrastre si no hay otro item siendo arrastrado
                        if (!isDragging) {
                            isDragging = true
                            onDragStart(foodItem, offset)
                        }
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
            painter = painterResource(id = foodItem.imageRes),
            contentDescription = foodItem.name,
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
private fun FinalResultsUI(
    detallesPorAlimento: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Plato Completado!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar feedback nutricional
            Text(
                text = detallesPorAlimento,
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A3D))
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun PlatoScreenPreview() {
    PlatoScreen()
}
