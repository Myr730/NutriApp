package org.bamx.puebla.feature.timeout

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import org.bamx.puebla.ui.theme.parents_bg_bottom
import org.bamx.puebla.ui.theme.parents_bg_top

@Composable
fun AdjustTimeScreen(
    onBackClick: () -> Unit = {}, // ← AGREGAR este parámetro
    modifier: Modifier = Modifier
) {
    // Estados para los tiempos
    var gameTimeMinutes by remember { mutableIntStateOf(15) }
    var breakTimeSeconds by remember { mutableIntStateOf(30) }

    val screenW = LocalConfiguration.current.screenWidthDp
    val isSmall = screenW <= 360


    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(parents_bg_top, parents_bg_bottom)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. BARRA SUPERIOR ESTRUCTURADA
            AdjustTimeTopBar(
                onBackClick = onBackClick // ← Pasar el parámetro aquí
            )

            Spacer(Modifier.height(24.dp))

            // Tarjeta de tiempo de juego
            TimeCard(
                title = stringResource(R.string.time_game_title),
                subtitle = stringResource(R.string.time_game_subtitle),
                valueText = "$gameTimeMinutes min",
                minusCd = stringResource(R.string.cd_decrease_minutes),
                plusCd = stringResource(R.string.cd_increase_minutes),
                modifier = Modifier.fillMaxWidth(),
                onMinusClick = {
                    if (gameTimeMinutes > 5) gameTimeMinutes -= 5
                },
                onPlusClick = {
                    if (gameTimeMinutes < 60) gameTimeMinutes += 5
                }
            )

            Spacer(Modifier.height(16.dp))

            // Tarjeta de pausa de estiramiento
            TimeCard(
                title = stringResource(R.string.time_break_title),
                subtitle = stringResource(R.string.time_break_subtitle),
                valueText = "$breakTimeSeconds seg",
                minusCd = stringResource(R.string.cd_decrease_seconds),
                plusCd = stringResource(R.string.cd_increase_seconds),
                modifier = Modifier.fillMaxWidth(),
                onMinusClick = {
                    if (breakTimeSeconds > 10) breakTimeSeconds -= 10
                },
                onPlusClick = {
                    if (breakTimeSeconds < 120) breakTimeSeconds += 10
                }
            )

            Spacer(Modifier.weight(1f))

            // Botón GUARDAR
            Button(
                onClick = {
                    // Aquí guardarías las configuraciones
                    println("Tiempo de juego guardado: $gameTimeMinutes minutos")
                    println("Tiempo de pausa guardado: $breakTimeSeconds segundos")
                    // Opcional: regresar automáticamente
                    // onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.time_save_button),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

/**
 * Barra superior que contiene el botón de regreso y el título
 */
@Composable
private fun AdjustTimeTopBar(
    onBackClick: () -> Unit // ← Recibir el parámetro
) {
    val screenW = LocalConfiguration.current.screenWidthDp
    val isSmall = screenW <= 360

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .heightIn(min = 56.dp)
    ) {
        // Título centrado
        TitleBanner(
            text = stringResource(R.string.time_screen_title),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
        )
        // Botón de regreso a la izquierda
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 0.dp, top = 24.dp)
                .size(if (isSmall) 44.dp else 52.dp)
                .clickable {
                    println("DEBUG: Botón regresar ParentsScreen clickeado")
                    onBackClick()
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun TitleBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(90.dp),
        color = MaterialTheme.colorScheme.error.copy(alpha = 0.95f) // ← Usar el color del tema
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                lineHeight = 24.sp
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun TimeCard(
    title: String,
    subtitle: String,
    valueText: String,
    minusCd: String,
    plusCd: String,
    modifier: Modifier = Modifier,
    onMinusClick: () -> Unit = {}, // ← Nuevos parámetros para los clicks
    onPlusClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            )
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PillIcon(
                    text = "–",
                    cd = minusCd,
                    onClick = onMinusClick // ← Pasar el click
                )

                Text(
                    text = valueText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                PillIcon(
                    text = "+",
                    cd = plusCd,
                    onClick = onPlusClick // ← Pasar el click
                )
            }
        }
    }
}

@Composable
private fun PillIcon(
    text: String,
    cd: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // ← Nuevo parámetro para el click
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }, // ← Usar el parámetro aquí
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "AdjustTime - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewAdjustTimeLight() {
    AppTheme(darkTheme = false) {
        AdjustTimeScreen(
            onBackClick = {} // ← Agregar para el preview
        )
    }
}

@Preview(
    name = "AdjustTime - 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewAdjustTimeDark() {
    AppTheme(darkTheme = true) {
        AdjustTimeScreen(
            onBackClick = {} // ← Agregar para el preview
        )
    }
}
