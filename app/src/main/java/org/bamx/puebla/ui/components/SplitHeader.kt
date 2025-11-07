package org.bamx.puebla.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.header_cream

/**
 * Header “partido” con título centrado y botón atrás a la izquierda.
 * - Título forzado a Fredoka (MaterialTheme.typography.headlineSmall modificado)
 * - Color del contenido: BLANCO en modo CLARO, NEGRO en modo OSCURO
 * - Altura fija cómoda + padding de status bar
 */
@Composable
fun SplitHeader(
    title: String,
    backgroundColor: Color = header_cream,
    leadingResId: Int = R.drawable.ic_back, // usa tu asset real
    leadingContentDescription: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onLeadingClick: () -> Unit
) {
    // En Previews ‘dark’ no siempre coincide con System, pero esto mantiene tu regla:
    // Claro -> blanco, Oscuro -> negro
    val isPreview = LocalInspectionMode.current
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f // robusto para preview
    val contentColor = if (isDark) Color.Black else Color.White

    Surface(color = backgroundColor) {
        Box(
            modifier
                .fillMaxWidth()
                .height(64.dp)
                .windowInsetsPadding(WindowInsets.statusBars) // evita notch
        ) {
            // Flecha back a la izquierda
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .size(32.dp)
            ) {
                Image(
                    painter = painterResource(id = leadingResId),
                    contentDescription = leadingContentDescription,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(contentColor),
                    modifier = Modifier.fillMaxSize()
                )
                // Si quieres que sea clicable, reemplaza este Box por IconButton y usa onBack?.
            }

            // Título CENTRADO con Fredoka bien marcado
            androidx.compose.material3.Text(
                text = title,
                color = contentColor,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.W700,
                    fontSize = 26.sp,      // súbelo/bájalo si quieres más “peso visual”
                    lineHeight = 30.sp
                )
            )
        }
    }
}
