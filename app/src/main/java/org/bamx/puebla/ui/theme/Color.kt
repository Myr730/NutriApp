package org.bamx.puebla.ui.theme

import androidx.compose.ui.graphics.Color

// ============================
// Esquema base (Material 3)
// ============================
val Primary       = Color(0xFFB71C1C)
val OnPrimary     = Color(0xFFFFFFFF)
val Secondary     = Color(0xFF00695C)
val OnSecondary   = Color(0xFFFFFFFF)
val Tertiary      = Color(0xFF33691E)
val OnTertiary    = Color(0xFFFFFFFF)

// Superficies
val SurfaceLight   = Color(0xFFFDF7EF) // crema claro general
val OnSurfaceLight = Color(0xFF1E1E1E)
val SurfaceDark    = Color(0xFF101010)
val OnSurfaceDark  = Color(0xFFECECEC)

// ============================
// TOKENS usados por pantallas
// ============================

// Header crema SplitHeader (Padres)
val header_cream = Color(0xFFF6EBDC)

// Degradado Padres
val parents_bg_top    = Color(0xFFFFF7ED)
val parents_bg_bottom = Color(0xFFF9EFE6)

// Tarjetas (Padres)
val card_yellow = Color(0xFFFFE082)
val card_orange = Color(0xFFFFB74D)

val card_green = Color(0xFFF0DBAA)
val card_teal   = Color(0xFF4DB6AC)
val card_pink   = Color(0xFFF48FB1)

// Juego: tablero/board (para GameScreen)
val board_bg_cream   = Color(0xFFFFF3E0) // crema suave del área de juego
val board_border_tan = Color(0xFFD8C2A4) // borde "tan" (ajústalo si tu asset pide otro tono)

// Overlays / scrims
val ScrimDark20 = Color(0x33000000)   // 20% negro
val ScrimDark40 = Color(0x66000000)   // 40% negro
// ============================
// Tokens extra usados en GameScreen y HomeButtons
// ============================


// Botones (HomeButtons)
val brand_green_button  = Color(0xFF2E7D32)     // verde “acción”
val brand_orange_button = Color(0xFFEF6C00)     // naranja destacado
val brand_purple_button = Color(0xFF8E24AA)     // púrpura acento

// (Opcional) Colores de texto sobre esos botones si los necesitas en tus componentes
val on_brand_green_button  = Color(0xFFFFFFFF)
val on_brand_orange_button = Color(0xFFFFFFFF)
val on_brand_purple_button = Color(0xFFFFFFFF)
// ==== Timeout screen tokens ====
val timeout_header_green = Color(0xFF2E9C5F)      // verde encabezado
val timeout_panel_beige  = Color(0xFFF7EEDC)      // panel de texto
val timeout_panel_shadow = Color(0x33000000)      // sombra sutil (20% negro)
// ==== Memorama tokens (opcionales) ====
val memorama_title_shadow = Color(0x33000000)   // 20% negro para sombra sutil
val memorama_card_border = Color(0x33000000)    // borde tenue
val memorama_scrim = Color(0x00000000)          // por si quisieras scrim (0% por ahora)
val bannerRed = Color(0xFFEA5D5D)
// Cartel rojo para banners/títulos en juegos
val timeout_header_red = Color(0xFFE85D5D) // puedes ajustar el tono si lo deseas
// Banner rojo (cartel superior)
val brandRedBanner = Color(0xFFFF6B6B)

// Botón verde “¡LISTO!”
val brandGreenButton = Color(0xFF38B24D)

// Placeholders (slots)
val slotBlue   = Color(0xFF5EC8FF)
val slotOrange = Color(0xFFFFA74D)

// (Opcional) contorno suave para los slots
val slotStroke = Color(0x1A000000) // negro con 10% aprox.

