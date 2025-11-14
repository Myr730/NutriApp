package org.bamx.puebla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.bamx.puebla.feature.ConsejosScreen
import kotlinx.coroutines.flow.asStateFlow
import org.bamx.puebla.feature.armatuplato.ArmaTuPlatoScreen
import org.bamx.puebla.feature.game.ClassifyGameScreen
import org.bamx.puebla.feature.guess.GuessTheVegetableScreen
import org.bamx.puebla.feature.home.HomeScreen
import org.bamx.puebla.feature.levelselection.LevelSelectionScreen
import org.bamx.puebla.feature.memorama.MemoramaScreen
import org.bamx.puebla.feature.parents.ParentsScreen
import org.bamx.puebla.feature.progress.ProgressScreen
import org.bamx.puebla.feature.smoothie.SmoothieMakerScreen
import org.bamx.puebla.feature.timeout.AdjustTimeScreen
import org.bamx.puebla.feature.timeout.TimeoutScreen
import org.bamx.puebla.ui.theme.AppTheme
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {

    // Estados para controlar el timeout
    private val _shouldShowTimeout = MutableStateFlow(false)
    private val _remainingBreakTime = MutableStateFlow(0L)
    private val _isGameSessionActive = MutableStateFlow(false)

    // Coroutine scope para operaciones en background
    private val appScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Iniciar monitoreo del tiempo de juego
        startGameTimeMonitoring()

        setContent {
            AppTheme {
                AppNavGraph(
                    shouldShowTimeout = _shouldShowTimeout.asStateFlow(),
                    remainingBreakTime = _remainingBreakTime.asStateFlow(),
                    onStartGame = ::startGameSession,
                    onResetGame = ::resetGameSession,
                    onSaveTimeSettings = ::saveTimeSettings
                )
            }
        }
    }

    private fun startGameTimeMonitoring() {
        appScope.launch {
            while (true) {
                checkGameTime()
                delay(10000) // Verificar cada 10 segundos
            }
        }
    }

    private suspend fun checkGameTime() {
        try {
            val repository = AppDatabase.getTimeSettingsRepository(this@MainActivity)
            val settings = repository?.getCurrentTimeSettings()

            val isValidSession = settings?.let {
                it.gameTimeMinutes > 0 &&
                    it.breakTimeSeconds > 0 &&
                    it.lastGameStartTime != null &&
                    _isGameSessionActive.value
            } ?: false

            if (isValidSession && settings != null && settings.lastGameStartTime != null) {
                val currentTime = Date().time
                val gameStartTime = settings.lastGameStartTime.time
                val gameDuration = settings.gameTimeMinutes * 60 * 1000L

                val shouldShow = currentTime - gameStartTime >= gameDuration
                _shouldShowTimeout.value = shouldShow

                if (shouldShow) {
                    val breakStartTime = gameStartTime + gameDuration
                    val breakDuration = settings.breakTimeSeconds * 1000L
                    val elapsedBreakTime = currentTime - breakStartTime
                    _remainingBreakTime.value = maxOf(0, (breakDuration - elapsedBreakTime) / 1000)
                }
            } else {
                _shouldShowTimeout.value = false
                _remainingBreakTime.value = 0L
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error monitoreando tiempo: ${e.message}")
            _shouldShowTimeout.value = false
        }
    }

    private fun startGameSession() {
        appScope.launch {
            try {
                val repository = AppDatabase.getTimeSettingsRepository(this@MainActivity)
                val settings = repository?.getCurrentTimeSettings()

                // SOLUCIÓN: Solo iniciar sesión si hay configuración válida
                if (settings?.gameTimeMinutes ?: 0 > 0 && settings?.breakTimeSeconds ?: 0 > 0) {
                    repository?.updateLastGameStartTime(Date())
                    _isGameSessionActive.value = true
                    _shouldShowTimeout.value = false
                    Log.d("MainActivity", "Sesión de juego iniciada con configuración válida")
                } else {
                    Log.d("MainActivity", "No hay configuración de tiempo, timeout desactivado")
                    _isGameSessionActive.value = false
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error iniciando sesión: ${e.message}")
                _isGameSessionActive.value = false
            }
        }
    }

    private fun resetGameSession() {
        appScope.launch {
            try {
                val repository = AppDatabase.getTimeSettingsRepository(this@MainActivity)
                repository?.updateLastGameStartTime(null)
                _isGameSessionActive.value = false
                _shouldShowTimeout.value = false
                Log.d("MainActivity", "Sesión de juego reiniciada")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error reiniciando sesión: ${e.message}")
            }
        }
    }

    private fun saveTimeSettings(gameTimeMinutes: Int, breakTimeSeconds: Int) {
        appScope.launch {
            try {
                val repository = AppDatabase.getTimeSettingsRepository(this@MainActivity)
                repository?.saveTimeSettings(gameTimeMinutes, breakTimeSeconds)
                _isGameSessionActive.value = false
                _shouldShowTimeout.value = false
                Log.d("MainActivity", "Tiempos guardados: $gameTimeMinutes min, $breakTimeSeconds seg - Sesión resetada")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error guardando tiempos: ${e.message}")
            }
        }
    }

    // Función para obtener las configuraciones actuales (para AdjustTimeScreen)
    fun getCurrentTimeSettings(callback: (TimeSettings?) -> Unit) {
        appScope.launch {
            try {
                val repository = AppDatabase.getTimeSettingsRepository(this@MainActivity)
                val settings = repository?.getCurrentTimeSettings()
                callback(settings)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error obteniendo configuraciones: ${e.message}")
                callback(null)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object LevelSelection : Screen("level_selection")
    object Parents : Screen("parents")
    object Progress : Screen("progress")
    object Consejos : Screen("consejos")
    object AdjustTime : Screen("adjust_time")
    object Timeout : Screen("timeout")

    object GuessTheVegetable : Screen("guess_the_vegetable")
    object SmoothieMaker : Screen("smoothie_maker")
    object Memorama : Screen("memorama")
    object ClassifyGame : Screen("classify_game")
    object ArmaTuPlato : Screen("arma_tu_plato")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    shouldShowTimeout: StateFlow<Boolean>,
    remainingBreakTime: StateFlow<Long>,
    onStartGame: () -> Unit,
    onResetGame: () -> Unit,
    onSaveTimeSettings: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val mainActivity = context as MainActivity

    // Observar si debemos mostrar el timeout
    val showTimeout by shouldShowTimeout.collectAsState()

    // Navegar automáticamente al timeout cuando sea necesario
    LaunchedEffect(showTimeout) {
        if (showTimeout) {
            if (navController.currentDestination?.route != Screen.Timeout.route) {
                println("NAVIGATION: Mostrando TimeoutScreen automáticamente")
                navController.navigate(Screen.Timeout.route)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onPlayClick = {
                    println("NAVIGATION: Navegando a LevelSelection")
                    navController.navigate(Screen.LevelSelection.route)
                },
                onParentsClick = {
                    println("NAVIGATION: Navegando a Parents")
                    navController.navigate(Screen.Parents.route)
                }
            )
        }

        composable(Screen.LevelSelection.route) {
            LevelSelectionScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a Home")
                    navController.popBackStack()
                },
                onLevelSelected = { levelId ->
                    println("NAVIGATION: Nivel $levelId seleccionado")

                    // SOLUCIÓN: Permitir jugar siempre, sin verificación de configuración
                    // Solo iniciar sesión si el usuario configuró tiempos
                    onStartGame()

                    when (levelId) {
                        1 -> navController.navigate(Screen.GuessTheVegetable.route)
                        2 -> navController.navigate(Screen.SmoothieMaker.route)
                        3 -> navController.navigate(Screen.Memorama.route)
                        4 -> navController.navigate(Screen.ClassifyGame.route)
                        5 -> navController.navigate(Screen.ArmaTuPlato.route)
                    }
                }
            )
        }

        composable(Screen.Parents.route) {
            ParentsScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a Home desde Parents")
                    navController.popBackStack()
                },
                onProgressClick = {
                    println("NAVIGATION: Navegando a ProgressScreen")
                    navController.navigate(Screen.Progress.route)
                },
                onTipsClick = {
                    println("NAVIGATION: Navegando a ConsejosScreen")
                    navController.navigate(Screen.Consejos.route)
                },
                onTimeClick = {
                    println("NAVIGATION: Navegando a AdjustTimeScreen")
                    navController.navigate(Screen.AdjustTime.route)
                }
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a Parents desde Progress")
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AdjustTime.route) {
            AdjustTimeScreenWrapper(
                onBackClick = {
                    println("NAVIGATION: Regresando a Parents desde AdjustTime")
                    navController.popBackStack()
                },
                onSaveTimeSettings = onSaveTimeSettings,
                mainActivity = mainActivity
            )
        }

        composable(Screen.Timeout.route) {
            TimeoutScreenWrapper(
                onBreakCompleted = {
                    println("NAVIGATION: Break completado, regresando a Home")
                    onResetGame()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                remainingBreakTime = remainingBreakTime
            )
        }

        // PANTALLAS DE JUEGOS
        composable(Screen.GuessTheVegetable.route) {
            GuessTheVegetableScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a LevelSelection desde GuessTheVegetable")
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SmoothieMaker.route) {
            SmoothieMakerScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a LevelSelection desde SmoothieMaker")
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Memorama.route) {
            MemoramaScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a LevelSelection desde Memorama")
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ClassifyGame.route) {
            ClassifyGameScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a LevelSelection desde ClassifyGame")
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Consejos.route) {
            ConsejosScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a Parents desde Consejos")
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ArmaTuPlato.route) {
            ArmaTuPlatoScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a LevelSelection desde ArmaTuPlato")
                    navController.popBackStack()
                }
            )
        }
    }
}

// Wrapper para AdjustTimeScreen que maneja las configuraciones
@Composable
fun AdjustTimeScreenWrapper(
    onBackClick: () -> Unit,
    onSaveTimeSettings: (Int, Int) -> Unit,
    mainActivity: MainActivity
) {
    var gameTimeMinutes by remember { mutableIntStateOf(15) }
    var breakTimeSeconds by remember { mutableIntStateOf(30) }

    // Cargar configuraciones actuales
    LaunchedEffect(Unit) {
        mainActivity.getCurrentTimeSettings { settings ->
            settings?.let {
                gameTimeMinutes = it.gameTimeMinutes
                breakTimeSeconds = it.breakTimeSeconds
            }
        }
    }

    AdjustTimeScreen(
        onBackClick = onBackClick,
        gameTimeMinutes = gameTimeMinutes,
        breakTimeSeconds = breakTimeSeconds,
        onGameTimeChange = { gameTimeMinutes = it },
        onBreakTimeChange = { breakTimeSeconds = it },
        onSaveSettings = {
            onSaveTimeSettings(gameTimeMinutes, breakTimeSeconds)
            onBackClick()
        }
    )
}

// Wrapper para TimeoutScreen
@Composable
fun TimeoutScreenWrapper(
    onBreakCompleted: () -> Unit,
    remainingBreakTime: StateFlow<Long>
) {
    val remainingTime by remainingBreakTime.collectAsState()

    // Temporizador en tiempo real
    LaunchedEffect(remainingTime) {
        if (remainingTime <= 0) {
            onBreakCompleted()
        }
    }

    TimeoutScreen(
        onBreakCompleted = onBreakCompleted,
        remainingTime = remainingTime
    )
}
