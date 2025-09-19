package org.bamx.puebla.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.bamx.puebla.ui.theme.Dimens

/**
 * Contenedor base:
 * - Fondo opcional con scrim
 * - topBar opcional (otra composable)
 * - Área de contenido con padding consistente
 */
@Composable
fun AppScaffold(
    @DrawableRes backgroundResId: Int? = null,
    darkenBackground: Float = 0f,
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundResId != null) {
            BackgroundImage(
                resId = backgroundResId,
                darken = darkenBackground,
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.screenPadding)
        ) {
            if (topBar != null) {
                topBar()
            }
            // El contenido vive en un Box para permitir overlays internos si se requiere.
            Box(modifier = Modifier.fillMaxSize(), content = content)
        }
    }
}
