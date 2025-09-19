package org.bamx.puebla.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.bamx.puebla.ui.theme.Dimens

/**
 * Contenedor base para pantallas:
 * - Coloca opcionalmente un fondo (BackgroundImage)
 * - Aplica padding consistente de pantalla
 */
@Composable
fun AppScaffold(
    @DrawableRes backgroundResId: Int? = null,
    darkenBackground: Float = 0f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundResId != null) {
            BackgroundImage(
                resId = backgroundResId,
                darken = darkenBackground,
                contentDescription = null // decorativa
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.screenPadding)
        ) {
            content()
        }
    }
}
