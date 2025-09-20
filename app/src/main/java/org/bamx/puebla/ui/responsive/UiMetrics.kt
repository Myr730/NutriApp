package org.bamx.puebla.ui.responsive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

enum class SizeClass { Small, Normal, Large }

/**
 * Métricas centralizadas por clase de pantalla.
 * Ajusta aquí fracciones/tamaños para Small (≤360dp), Normal (361–411dp), Large (≥412dp).
 */
@Immutable
data class UiMetrics(
    val sizeClass: SizeClass,
    val screenWidthDp: Int,

    // HOME
    val homeMascotFracSmall: Float,
    val homeMascotFracNormal: Float,
    val homeBannerWidthFraction: Float,

    // GAME
    val gameSignWidthFraction: Float,
    val gameTitleFontSizeSp: Int,
    val gameTitleLineHeightSp: Int,
    val gameFruitWidthFraction: Float,
    val gameSuccessWidthFraction: Float,
    val gameBasketHeightFraction: Float
)

@Stable
@Composable
fun rememberUiMetrics(): UiMetrics {
    val w = LocalConfiguration.current.screenWidthDp
    val sc = when {
        w <= 360 -> SizeClass.Small
        w <= 411 -> SizeClass.Normal
        else -> SizeClass.Large
    }

    return remember(w) {
        when (sc) {
            SizeClass.Small -> UiMetrics(
                sizeClass = sc,
                screenWidthDp = w,

                // HOME
                homeMascotFracSmall = 0.72f,
                homeMascotFracNormal = 0.60f,
                homeBannerWidthFraction = 0.86f,

                // GAME
                gameSignWidthFraction = 0.96f,
                gameTitleFontSizeSp = 28,
                gameTitleLineHeightSp = 32,
                gameFruitWidthFraction = 0.60f,
                gameSuccessWidthFraction = 0.84f,
                gameBasketHeightFraction = 0.38f
            )

            SizeClass.Normal -> UiMetrics(
                sizeClass = sc,
                screenWidthDp = w,

                // HOME
                homeMascotFracSmall = 0.72f,  // referencia
                homeMascotFracNormal = 0.60f,
                homeBannerWidthFraction = 0.86f,

                // GAME
                gameSignWidthFraction = 0.92f,
                gameTitleFontSizeSp = 32,
                gameTitleLineHeightSp = 36,
                gameFruitWidthFraction = 0.58f,
                gameSuccessWidthFraction = 0.78f,
                gameBasketHeightFraction = 0.36f
            )

            SizeClass.Large -> UiMetrics(
                sizeClass = sc,
                screenWidthDp = w,

                // HOME
                homeMascotFracSmall = 0.72f,
                homeMascotFracNormal = 0.58f,
                homeBannerWidthFraction = 0.80f,

                // GAME (ligeramente más compacto para dejar aire)
                gameSignWidthFraction = 0.88f,
                gameTitleFontSizeSp = 34,
                gameTitleLineHeightSp = 40,
                gameFruitWidthFraction = 0.54f,
                gameSuccessWidthFraction = 0.74f,
                gameBasketHeightFraction = 0.34f
            )
        }
    }
}
