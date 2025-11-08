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
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    // Estados para los tiempos
    var gameTimeMinutes by remember { mutableIntStateOf(15) }
    var breakTimeSeconds by remember { mutableIntStateOf(30) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(parents_bg_top, parents_bg_bottom)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // BARRA SUPERIOR
            AdjustTimeTopBar(onBackClick = onBackClick)

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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.time_save_button),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AdjustTimeTopBar(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .height(56.dp)
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
                .align(Alignment.CenterStart)
                .size(52.dp)
                .clickable { onBackClick() },
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
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
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
    onMinusClick: () -> Unit = {},
    onPlusClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Controles de tiempo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeControlButton(
                    text = "–",
                    contentDescription = minusCd,
                    onClick = onMinusClick
                )

                Text(
                    text = valueText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                TimeControlButton(
                    text = "+",
                    contentDescription = plusCd,
                    onClick = onPlusClick
                )
            }
        }
    }
}

@Composable
private fun TimeControlButton(
    text: String,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "AdjustTime - Light",
    showBackground = true,
    widthDp = 411,
    heightDp = 891
)
@Composable
private fun PreviewAdjustTimeLight() {
    AppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = parents_bg_top
        ) {
            AdjustTimeScreen(
                onBackClick = {}
            )
        }
    }
}

@Preview(
    name = "AdjustTime - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun PreviewAdjustTimeDark() {
    AppTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = parents_bg_top
        ) {
            AdjustTimeScreen(
                onBackClick = {}
            )
        }
    }
}
