package org.bamx.puebla.feature.levelselection

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme

// Data class para representar cada nivel
data class Level(
    val id: Int,
    val iconRes: Int,
    val titleRes: Int,
    val descriptionRes: Int
)

@Composable
fun LevelSelectionScreen(
    onBackClick: () -> Unit = {},
    onLevelSelected: (Int) -> Unit = {} // Nuevo parámetro para cuando se selecciona un nivel
) {
    // Lista de niveles disponibles
    val levels = listOf(
        Level(1, R.drawable.icon_level_veggies, R.string.level1_title, R.string.level1_description),
        Level(2, R.drawable.nivel2, R.string.level2_title, R.string.level2_description),
        Level(3, R.drawable.nivel3, R.string.level3_title, R.string.level3_description),
        Level(4, R.drawable.nivel4, R.string.level4_title, R.string.level4_description),
        Level(5, R.drawable.nivel5, R.string.level5_title, R.string.level5_description)
    )

    // Estado para el nivel actualmente visible
    var currentLevelIndex by remember { mutableIntStateOf(0) }

    val screenW = LocalConfiguration.current.screenWidthDp
    val isSmall = screenW <= 360

    val bannerHorizontalPadding = if (isSmall) 16.dp else 20.dp
    val bannerVerticalPadding = if (isSmall) 8.dp else 10.dp
    val bannerCorner = 18.dp

    val arrowsSize = if (isSmall) 40.dp else 48.dp
    val circleSize = if (isSmall) 132.dp else 156.dp
    val circleInnerIconFraction = if (isSmall) 0.55f else 0.6f

    val mascotWidth = if (isSmall) 150.dp else 200.dp

    // Nivel actual
    val currentLevel = levels[currentLevelIndex]

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo ilustrado
        Image(
            painter = painterResource(id = R.drawable.bg_level_selection),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botón de REGRESAR con clickable
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

        // Cartel superior "¡ELIGE TU NIVEL!"
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = if (isSmall) 56.dp else 64.dp)
                .clip(RoundedCornerShape(bannerCorner))
                .background(color = levelBannerRed()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.level_select_title),
                modifier = Modifier.padding(
                    horizontal = bannerHorizontalPadding,
                    vertical = bannerVerticalPadding
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (isSmall) 18.sp else 20.sp,
                    lineHeight = if (isSmall) 20.sp else 22.sp
                )
            )
        }

        // Centro: flecha – círculo con ícono – flecha
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Flecha izquierda - navegar al nivel anterior
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = R.string.cd_arrow_left),
                modifier = Modifier
                    .size(arrowsSize)
                    .clickable {
                        // Navegar al nivel anterior (circular)
                        currentLevelIndex = if (currentLevelIndex - 1 >= 0) {
                            currentLevelIndex - 1
                        } else {
                            levels.size - 1 // Ir al último nivel
                        }
                    },
                contentScale = ContentScale.Fit
            )

            // Círculo central con el nivel actual - HACER CLICK PARA SELECCIONAR
            Surface(
                modifier = Modifier
                    .size(circleSize)
                    .clickable {
                        onLevelSelected(currentLevel.id)
                    },
                shape = CircleShape,
                color = levelCircleBg(),
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = currentLevel.iconRes),
                        contentDescription = stringResource(id = R.string.cd_level_icon),
                        modifier = Modifier.fillMaxSize(circleInnerIconFraction),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Flecha derecha - navegar al siguiente nivel
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = stringResource(id = R.string.cd_arrow_right),
                modifier = Modifier
                    .size(arrowsSize)
                    .clickable {
                        // Navegar al siguiente nivel (circular)
                        currentLevelIndex = (currentLevelIndex + 1) % levels.size
                    },
                contentScale = ContentScale.Fit
            )
        }

        // Indicador de nivel actual (ej: "Nivel 1 de 5")
        Text(
            text = stringResource(R.string.level_indicator, currentLevelIndex + 1, levels.size),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = circleSize + 40.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )

        // Nutri abajo (señalando)
        Image(
            painter = painterResource(id = R.drawable.nutri_level),
            contentDescription = stringResource(id = R.string.cd_mascot),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isSmall) 52.dp else 50.dp)
                .width(mascotWidth),
            contentScale = ContentScale.FillWidth
        )

        // Texto al pie "DESLIZA PARA VER MÁS NIVELES"
        Text(
            text = stringResource(id = R.string.level_tip_swipe),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        )
    }
}

/** Color helper del cartel rojo */
@Composable
private fun levelBannerRed() =
    MaterialTheme.colorScheme.error.copy(alpha = 0.95f)

/** Fondo para el círculo central */
@Composable
private fun levelCircleBg() =
    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.95f)

/* ---------------- PREVIEWS ---------------- */

@Preview(
    name = "Level Selection – 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewLevelSelectionLight() {
    AppTheme(darkTheme = false) {
        LevelSelectionScreen(
            onBackClick = {},
            onLevelSelected = { levelId ->
                println("Nivel seleccionado: $levelId")
            }
        )
    }
}

@Preview(
    name = "Level Selection – 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewLevelSelectionDark() {
    AppTheme(darkTheme = true) {
        LevelSelectionScreen(
            onBackClick = {},
            onLevelSelected = { levelId ->
                println("Nivel seleccionado: $levelId")
            }
        )
    }
}
