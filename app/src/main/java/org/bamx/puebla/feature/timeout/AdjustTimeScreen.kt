package org.bamx.puebla.feature.timeout

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    backIconResId: Int = R.drawable.ic_back
) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(parents_bg_top, parents_bg_bottom)
    )

    // El Box ahora solo se encarga del fondo
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp), // Padding general
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. BARRA SUPERIOR ESTRUCTURADA
            // Contiene el botón y el título, evitando el empalme.
            AdjustTimeTopBar(backIconResId = backIconResId)

            Spacer(Modifier.height(24.dp))

            TimeCard(
                title = "Tiempo de juego",
                subtitle = "¿Cuánto quieres jugar antes del descanso?",
                valueText = "15 min",
                minusCd = "Disminuir minutos",
                plusCd = "Aumentar minutos",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            TimeCard(
                title = "Pausa de estiramiento",
                subtitle = "Duración de la pausa para moverse",
                valueText = "30 seg",
                minusCd = "Disminuir segundos",
                plusCd = "Aumentar segundos",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { /* NO-OP */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars), // Padding para la nav bar
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "GUARDAR",
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
 * Barra superior que contiene el botón de regreso y el título,
 * gestionando su alineación y el padding de la status bar.
 */
@Composable
private fun AdjustTimeTopBar(backIconResId: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .heightIn(min = 56.dp) // Altura mínima para asegurar espacio
    ) {
        // Título centrado
        TitleBanner(
            text = "AJUSTAR TIEMPO",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f) // Reducido para no chocar con el botón
        )
        // Botón de regreso a la izquierda
        Image(
            painter = painterResource(id = backIconResId),
            contentDescription = "Regresar",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp)
                .clickable { /* NO-OP (UI) */ }
        )
    }
}


/* ---------- Pieces (sin cambios) ---------- */

@Composable
private fun TitleBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFE45858)
    ) {
        Text(
            text = text,
            color = Color.White,
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
    modifier: Modifier = Modifier
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
                PillIcon(text = "–", cd = minusCd)

                Text(
                    text = valueText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                PillIcon(text = "+", cd = plusCd)
            }
        }
    }
}

@Composable
private fun PillIcon(
    text: String,
    cd: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { /* NO-OP */ },
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
    name = "AdjustTime - 411x8R91 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewAdjustTimeLight() {
    AppTheme(darkTheme = false) {
        AdjustTimeScreen()
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
        AdjustTimeScreen()
    }
}
