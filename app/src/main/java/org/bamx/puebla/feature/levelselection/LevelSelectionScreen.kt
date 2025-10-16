package org.bamx.puebla.feature.level

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun LevelSelectionScreen() {
    val screenW = LocalConfiguration.current.screenWidthDp
    val isSmall = screenW <= 360

    val bannerHorizontalPadding = if (isSmall) 16.dp else 20.dp
    val bannerVerticalPadding = if (isSmall) 8.dp else 10.dp
    val bannerCorner = 18.dp

    val arrowsSize = if (isSmall) 40.dp else 48.dp
    val circleSize = if (isSmall) 132.dp else 156.dp
    val circleInnerIconFraction = if (isSmall) 0.55f else 0.6f

    // --- CORRECCIÓN AQUÍ ---
    // Reducimos el tamaño en pantallas pequeñas y lo aumentamos en las grandes.
    val mascotWidth = if (isSmall) 150.dp else 200.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo ilustrado
        Image(
            painter = painterResource(id = R.drawable.bg_level_selection),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botón de REGRESAR (imagen, arriba-izquierda)
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 12.dp, top = 12.dp)
                .size(if (isSmall) 44.dp else 52.dp),
            contentScale = ContentScale.Fit
        )

        // Cartel superior “¡ELIGE TU NIVEL!”
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = if (isSmall) 56.dp else 64.dp)
                .clip(RoundedCornerShape(bannerCorner))
                .background(color = LevelBannerRed()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.level_select_title),
                modifier = Modifier.padding(
                    horizontal = bannerHorizontalPadding,
                    vertical = bannerVerticalPadding
                ),
                color = MaterialTheme.colorScheme.onPrimary, // usa tu onPrimary o quedará blanco por el tema
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
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = R.string.cd_arrow_left),
                modifier = Modifier.size(arrowsSize),
                contentScale = ContentScale.Fit
            )

            Surface(
                modifier = Modifier.size(circleSize),
                shape = CircleShape,
                color = LevelCircleBg(),
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_level_veggies),
                        contentDescription = stringResource(id = R.string.cd_level_icon),
                        modifier = Modifier.fillMaxSize(circleInnerIconFraction),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = stringResource(id = R.string.cd_arrow_right),
                modifier = Modifier.size(arrowsSize),
                contentScale = ContentScale.Fit
            )
        }

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

        // Texto al pie “DESLIZA PARA VER MÁS NIVELES”
        Text(
            text = stringResource(id = R.string.level_tip_swipe),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
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

/** Color helper del cartel rojo (ajústalo a tus tokens si los tienes). */
@Composable
private fun LevelBannerRed() =
    // Si ya tienes un token (p. ej., banner_red), úsalo aquí.
    MaterialTheme.colorScheme.error.copy(alpha = 0.95f)

/** Fondo para el círculo central (ícono de nivel). */
@Composable
private fun LevelCircleBg() =
    // Tono vainilla/crema para contrastar con el ícono
    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.95f)

/* ---------------- PREVIEWS ---------------- */

@Preview(
    name = "Level Selection – 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewLevelSelectionLight() {
    AppTheme(darkTheme = false) { LevelSelectionScreen() }
}

@Preview(
    name = "Level Selection – 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewLevelSelectionDark() {
    AppTheme(darkTheme = true) { LevelSelectionScreen() }
}
