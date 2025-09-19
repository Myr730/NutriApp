package org.bamx.puebla.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- LIGHT ---
val md_primary_light = Color(0xFFD32F2F) // Red 700
val md_onPrimary_light = Color(0xFFFFFFFF)
val md_primaryContainer_light = Color(0xFFFFCDD2) // red100
val md_onPrimaryContainer_light = Color(0xFF410002)

val md_secondary_light = Color(0xFFF57C00) // Orange 700
val md_onSecondary_light = Color(0xFFFFFFFF)
val md_secondaryContainer_light = Color(0xFFFFE0B2) // orange100
val md_onSecondaryContainer_light = Color(0xFF301400)

val md_tertiary_light = Color(0xFF2E7D32) // Green 700
val md_onTertiary_light = Color(0xFFFFFFFF)
val md_tertiaryContainer_light = Color(0xFFC8E6C9) // green100
val md_onTertiaryContainer_light = Color(0xFF00210B)

val md_error_light = Color(0xFFB3261E)
val md_onError_light = Color(0xFFFFFFFF)
val md_errorContainer_light = Color(0xFFF9DEDC)
val md_onErrorContainer_light = Color(0xFF410E0B)

val md_background_light = Color(0xFFFFFBFE)
val md_onBackground_light = Color(0xFF1B1B1F)
val md_surface_light = Color(0xFFFFFBFE)
val md_onSurface_light = Color(0xFF1B1B1F)
val md_surfaceVariant_light = Color(0xFFE7E0EC)
val md_onSurfaceVariant_light = Color(0xFF49454F)
val md_outline_light = Color(0xFF79747E)
val md_outlineVariant_light = Color(0xFFCAC4D0)
val md_surfaceDim_light = Color(0xFFECE6F0)

// --- DARK ---
val md_primary_dark = Color(0xFFFF8983) // tono claro de rojo para dark
val md_onPrimary_dark = Color(0xFF5F1312)
val md_primaryContainer_dark = Color(0xFF8C1C1A)
val md_onPrimaryContainer_dark = Color(0xFFFFDAD6)

val md_secondary_dark = Color(0xFFFFB86C)
val md_onSecondary_dark = Color(0xFF4C2800)
val md_secondaryContainer_dark = Color(0xFF7A4400)
val md_onSecondaryContainer_dark = Color(0xFFFFDCBE)

val md_tertiary_dark = Color(0xFF8DD39A)
val md_onTertiary_dark = Color(0xFF0A3817)
val md_tertiaryContainer_dark = Color(0xFF145122)
val md_onTertiaryContainer_dark = Color(0xFFBDF1C4)

val md_error_dark = Color(0xFFF2B8B5)
val md_onError_dark = Color(0xFF601410)
val md_errorContainer_dark = Color(0xFF8C1D18)
val md_onErrorContainer_dark = Color(0xFFF9DEDC)

val md_background_dark = Color(0xFF1B1B1F)
val md_onBackground_dark = Color(0xFFE3E2E6)
val md_surface_dark = Color(0xFF1B1B1F)
val md_onSurface_dark = Color(0xFFE3E2E6)
val md_surfaceVariant_dark = Color(0xFF49454F)
val md_onSurfaceVariant_dark = Color(0xFFCAC4D0)
val md_outline_dark = Color(0xFF948F99)
val md_outlineVariant_dark = Color(0xFF49454F)
val md_surfaceDim_dark = Color(0xFF121316)

// Esquemas Material3 listos:
val LightColors = lightColorScheme(
    primary = md_primary_light,
    onPrimary = md_onPrimary_light,
    primaryContainer = md_primaryContainer_light,
    onPrimaryContainer = md_onPrimaryContainer_light,
    secondary = md_secondary_light,
    onSecondary = md_onSecondary_light,
    secondaryContainer = md_secondaryContainer_light,
    onSecondaryContainer = md_onSecondaryContainer_light,
    tertiary = md_tertiary_light,
    onTertiary = md_onTertiary_light,
    tertiaryContainer = md_tertiaryContainer_light,
    onTertiaryContainer = md_onTertiaryContainer_light,
    error = md_error_light,
    onError = md_onError_light,
    errorContainer = md_errorContainer_light,
    onErrorContainer = md_onErrorContainer_light,
    background = md_background_light,
    onBackground = md_onBackground_light,
    surface = md_surface_light,
    onSurface = md_onSurface_light,
    surfaceVariant = md_surfaceVariant_light,
    onSurfaceVariant = md_onSurfaceVariant_light,
    outline = md_outline_light,
    outlineVariant = md_outlineVariant_light,
)

val DarkColors = darkColorScheme(
    primary = md_primary_dark,
    onPrimary = md_onPrimary_dark,
    primaryContainer = md_primaryContainer_dark,
    onPrimaryContainer = md_onPrimaryContainer_dark,
    secondary = md_secondary_dark,
    onSecondary = md_onSecondary_dark,
    secondaryContainer = md_secondaryContainer_dark,
    onSecondaryContainer = md_onSecondaryContainer_dark,
    tertiary = md_tertiary_dark,
    onTertiary = md_onTertiary_dark,
    tertiaryContainer = md_tertiaryContainer_dark,
    onTertiaryContainer = md_onTertiaryContainer_dark,
    error = md_error_dark,
    onError = md_onError_dark,
    errorContainer = md_errorContainer_dark,
    onErrorContainer = md_onErrorContainer_dark,
    background = md_background_dark,
    onBackground = md_onBackground_dark,
    surface = md_surface_dark,
    onSurface = md_onSurface_dark,
    surfaceVariant = md_surfaceVariant_dark,
    onSurfaceVariant = md_onSurfaceVariant_dark,
    outline = md_outline_dark,
    outlineVariant = md_outlineVariant_dark,
)
