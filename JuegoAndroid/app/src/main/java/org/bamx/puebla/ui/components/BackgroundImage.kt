package org.bamx.puebla.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize // Se cambió la importación
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

/**
 * Muestra una imagen de fondo a pantalla completa.
 * @param resId Recurso drawable del fondo (WebP/PNG). Usa drawable-nodpi para fondos.
 * @param darken 0f..1f para oscurecer (scrim). Útil para contraste con el contenido.
 */
@Composable
fun BackgroundImage(
    @DrawableRes resId: Int,
    darken: Float = 0f,
    contentDescription: String? = null, // null si es puramente decorativa
) {
    // Se usa fillMaxSize() para que el Box ocupe todo el espacio padre
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription, // null si solo decorativa
            // La imagen también debe llenar el Box
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // recorte agradable a pantalla
        )
        if (darken > 0f) {
            Box(
                modifier = Modifier
                    // La capa de oscurecimiento también debe llenar el espacio
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = darken.coerceIn(0f, 1f)))
            )
        }
    }
}
