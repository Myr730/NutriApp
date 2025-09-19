// BAMXPuebla/app/src/main/java/org/bamx/puebla/ui/theme/Theme.kt
package org.bamx.puebla.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Paletas por defecto (provisionales). Se reemplazarán en Bloque 5.
private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        // Tipografías/Formas provisionales; se definirán en Bloque 5.
        content = content
    )
}
