package org.bamx.puebla.feature.progress

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import org.bamx.puebla.ui.theme.Dimens
import org.bamx.puebla.ui.theme.header_cream
import org.bamx.puebla.ui.theme.parents_bg_bottom
import org.bamx.puebla.ui.theme.parents_bg_top
import androidx.compose.foundation.layout.WindowInsets

@Composable
fun ProgressScreen(
    onBackClick: () -> Unit = {}
) {
    val screenW = LocalConfiguration.current.screenWidthDp
    val isSmall = screenW <= 360

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(parents_bg_top, parents_bg_bottom)))
    ) {
        // PRIMERO: El contenido principal (atrás)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = Dimens.screenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Espacio para el botón de regresar
            Spacer(Modifier.height(60.dp)) // ← AUMENTAR este espacio

            // Título principal centrado — más grande
            Text(
                text = "Mi Progreso",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B16),
                    fontSize = 32.sp,
                    lineHeight = 36.sp
                )
            )

            // Tarjeta PERFIL — contenidos centrados
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Perfil",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color(0xFFFF8A3D),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "María García Lopez",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFF3A3A3A),
                            fontSize = 16.sp
                        )
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // Tarjeta HISTORIAL DE PESO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Historial de Peso",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color(0xFFFF8A3D),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    // Encabezado tipo tabla — más grande
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Fecha",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF2F2F2F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "Peso (kg)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF2F2F2F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Filas estáticas (ejemplo) — más altas
                    WeightRow(date = "15/09/2025", weight = "58.0", bg = Color(0xFFE8F7EA))
                    Spacer(Modifier.height(8.dp))
                    WeightRow(date = "16/09/2025", weight = "58.5", bg = Color(0xFFE8F7EA))
                    Spacer(Modifier.height(8.dp))
                    WeightRow(date = "17/09/2025", weight = "58.9", bg = Color(0xFFFFEDED))
                    Spacer(Modifier.height(8.dp))
                    WeightRow(date = "18/09/2025", weight = "59.5", bg = Color(0xFFFFEDED))
                }
            }

            Spacer(Modifier.height(110.dp))
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }

        // SEGUNDO: Botón de regresar (encima del contenido)
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars) // ← AGREGAR esto
                .padding(start = 12.dp, top = 12.dp)
                .size(if (isSmall) 44.dp else 52.dp)
                .clickable {
                    println("DEBUG: Botón regresar ProgressScreen clickeado")
                    onBackClick()
                },
            contentScale = ContentScale.Fit
        )

        // TERCERO: FAB inferior derecha — más notorio
        FloatingActionButton(
            onClick = { /* TODO: abrir formulario de nuevo peso */ },
            shape = RoundedCornerShape(30.dp),
            containerColor = Color(0xFFFF8A3D),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = Dimens.screenPadding, bottom = Dimens.screenPadding + 10.dp)
                .size(64.dp)
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp
                )
            )
        }

        // Superficie superior (coherencia visual con header suave)
        Surface(
            color = header_cream.copy(alpha = 0f),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {}
    }
}

@Composable
private fun WeightRow(
    date: String,
    weight: String,
    bg: Color,
) {
    Surface(
        color = bg,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF3A3A3A),
                    fontSize = 16.sp
                )
            )
            Text(
                text = weight,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF3A3A3A),
                    fontSize = 16.sp
                )
            )
        }
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Progreso (ajustado) - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewProgressLight() {
    AppTheme(darkTheme = false) {
        ProgressScreen(
            onBackClick = { println("Preview: Regresando") }
        )
    }
}

@Preview(
    name = "Progreso (ajustado) - 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewProgressDark() {
    AppTheme(darkTheme = true) {
        ProgressScreen(
            onBackClick = { println("Preview: Regresando") }
        )
    }
}
