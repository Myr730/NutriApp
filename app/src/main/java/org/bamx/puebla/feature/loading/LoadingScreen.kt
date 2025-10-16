package org.bamx.puebla.feature.loading

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme

/**
 * Pantalla de carga (solo UI).
 *
 * Reemplaza los drawables por los nombres que tengas en tu proyecto si difieren:
 * - Fondo    -> R.drawable.bg_loading  (WebP en drawable-nodpi)
 * - Logo     -> R.drawable.title_aventuras (o el logo que uses ya en Home)
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    progress: Float = 0.35f, // UI estática; conecta tu progreso real cuando exista lógica
) {
    val clamped = progress.coerceIn(0f, 1f)

    Box(modifier = modifier.fillMaxSize()) {
        // Fondo ilustrado
        Image(
            painter = painterResource(id = R.drawable.bg_loading),
            contentDescription = stringResource(id = R.string.cd_background),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo centrado
        Image(
            painter = painterResource(id = R.drawable.title_aventuras),
            contentDescription = stringResource(id = R.string.cd_logo_app),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.86f),
            contentScale = ContentScale.Fit
        )

        // Sección inferior: etiqueta "CARGANDO" + barra
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.loading_label),
                color = Color(0xFF1C2018),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            )

            Spacer(Modifier.height(12.dp))

            LoadingBar(
                progress = clamped,
                modifier = Modifier.fillMaxWidth(0.88f)
            )
        }
    }
}

/** Barra de progreso con estilo “píldora”. UI pura. */
@Composable
private fun LoadingBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Int = 24
) {
    val outerShape = RoundedCornerShape(50)
    val outer = Color(0xFF2D7A3A)    // verde borde
    val track = Color(0xFFE1F6E6)    // pista
    val fill  = Color(0xFF45C06B)    // relleno

    // Contenedor con borde
    Surface(
        modifier = modifier
            .height(height.dp),
        shape = outerShape,
        color = track,
        tonalElevation = 0.dp,
        shadowElevation = 8.dp
    ) {
        // Borde oscuro “marco” (simulado con Box + padding)
        Box(
            modifier = Modifier
                .background(outer, shape = outerShape)
                .padding(4.dp)
        ) {
            // Relleno según progreso
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(outerShape)
                    .background(fill)
            )
        }
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Loading – 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewLoadingLight() {
    AppTheme(darkTheme = false) { LoadingScreen(progress = 0.4f) }
}

@Preview(
    name = "Loading – 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewLoadingDark() {
    AppTheme(darkTheme = true) { LoadingScreen(progress = 0.75f) }
}
