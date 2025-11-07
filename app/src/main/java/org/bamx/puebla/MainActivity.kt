package org.bamx.puebla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.bamx.puebla.feature.armatuplato.ArmaTuPlatoScreen
import org.bamx.puebla.feature.game.ClassifyMockScreen
import org.bamx.puebla.feature.guess.GuessTheVegetableScreen
import org.bamx.puebla.feature.home.HomeScreen
import org.bamx.puebla.feature.levelselection.LevelSelectionScreen
import org.bamx.puebla.feature.memorama.MemoramaScreen
import org.bamx.puebla.feature.parents.ParentsScreen
import org.bamx.puebla.feature.progress.ProgressScreen
import org.bamx.puebla.feature.smoothie.SmoothieMakerScreen
import org.bamx.puebla.feature.timeout.AdjustTimeScreen
import org.bamx.puebla.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavGraph()
            }
        }
    }
}

// CORREGIR: Eliminar el duplicado
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object LevelSelection : Screen("level_selection")
    object Parents : Screen("parents")
    object Progress : Screen("progress")
    object Consejos : Screen("consejos")
    object AdjustTime : Screen("adjust_time")

    // NUEVAS PANTALLAS DE NIVELES
    object GuessTheVegetable : Screen("guess_the_vegetable")
    object SmoothieMaker : Screen("smoothie_maker")
    object Memorama : Screen("memorama")
    object ClassifyMock : Screen("classify_mock")
    object ArmaTuPlato : Screen("arma_tu_plato")
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
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
                    // Navegar a la pantalla correspondiente según el nivel
                    when (levelId) {
                        1 -> navController.navigate(Screen.GuessTheVegetable.route)
                        2 -> navController.navigate(Screen.SmoothieMaker.route)
                        3 -> navController.navigate(Screen.Memorama.route)
                        4 -> navController.navigate(Screen.ClassifyMock.route)
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
            AdjustTimeScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a Parents desde AdjustTime")
                    navController.popBackStack()
                }
            )
        }

        // NUEVAS PANTALLAS DE JUEGOS
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

        composable(Screen.ClassifyMock.route) {
            ClassifyMockScreen(
                onBackClick = {
                    println("NAVIGATION: Regresando a LevelSelection desde ClassifyMock")
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
