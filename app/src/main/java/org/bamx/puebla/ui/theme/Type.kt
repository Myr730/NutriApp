package org.bamx.puebla.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R

// Asegúrate de tener estos archivos EXACTOS en res/font:
// - fredoka_light.ttf
// - fredoka_regular.ttf
// - fredoka_medium.ttf
// - fredoka_semibold.ttf
// - fredoka_bold.ttf
// Si tus nombres son distintos, cambia los R.font.* de abajo.
val Fredoka = FontFamily(
    Font(R.font.fredoka_light,    weight = FontWeight.W300),
    Font(R.font.fredoka_regular,  weight = FontWeight.W400),
    Font(R.font.fredoka_medium,   weight = FontWeight.W500),
    Font(R.font.fredoka_semibold, weight = FontWeight.W600),
    Font(R.font.fredoka_bold,     weight = FontWeight.W700)
)

// Tipografía global M3 forzando Fredoka en TODOS los estilos relevantes.
// Evitamos @Immutable aquí porque es un val top-level.
val AppTypography: Typography = Typography().let { base ->
    base.copy(
        // Displays
        displayLarge  = base.displayLarge.copy(fontFamily = Fredoka),
        displayMedium = base.displayMedium.copy(fontFamily = Fredoka),
        displaySmall  = base.displaySmall.copy(fontFamily = Fredoka),

        // Headlines
        headlineLarge  = base.headlineLarge.copy(fontFamily = Fredoka),
        headlineMedium = base.headlineMedium.copy(fontFamily = Fredoka),
        headlineSmall  = base.headlineSmall.copy(
            fontFamily = Fredoka,
            fontWeight = FontWeight.W700,
            fontSize   = 32.sp,
            lineHeight = 36.sp
        ),

        // Titles
        titleLarge = base.titleLarge.copy(
            fontFamily = Fredoka,
            fontWeight = FontWeight.W600,
            fontSize   = 22.sp,
            lineHeight = 26.sp
        ),
        titleMedium = base.titleMedium.copy(
            fontFamily = Fredoka,
            fontWeight = FontWeight.W600,
            fontSize   = 18.sp,
            lineHeight = 22.sp
        ),
        titleSmall = base.titleSmall.copy(
            fontFamily = Fredoka,
            fontWeight = FontWeight.W500,
            fontSize   = 16.sp,
            lineHeight = 20.sp
        ),

        // Body
        bodyLarge  = base.bodyLarge.copy(fontFamily = Fredoka),
        bodyMedium = base.bodyMedium.copy(
            fontFamily = Fredoka,
            fontWeight = FontWeight.W400,
            fontSize   = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall  = base.bodySmall.copy(fontFamily = Fredoka),

        // Labels
        labelLarge  = base.labelLarge.copy(
            fontFamily = Fredoka,
            fontWeight = FontWeight.W400,
            fontSize   = 12.sp,
            lineHeight = 14.sp
        ),
        labelMedium = base.labelMedium.copy(fontFamily = Fredoka),
        labelSmall  = base.labelSmall.copy(fontFamily = Fredoka),
    )
}
