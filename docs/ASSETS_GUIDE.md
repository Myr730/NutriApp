# Guía de Assets — BAMX Puebla

## Reglas de oro
1. **Fondos/ilustraciones grandes** → `res/drawable-nodpi/` (no escalar por densidad).
2. **Íconos simples** → **VectorDrawable** (XML en `res/drawable/`). Si es bitmap, preferir **WebP lossless**.
3. **Nombres**: `lowercase_underscore` (ej. `home_illustration.webp`).
4. **Color profile**: exportar en **sRGB** (evitar CMYK/Display-P3).
5. **Transparencias**: solo si son necesarias. Quitar alpha en fondos *full screen* cuando se pueda.
6. **Tamaño mínimo táctil**: 48dp (si el asset es “clickable”).

## Carpetas destino (Android)
app/src/main/res/
├─ drawable-nodpi/ # ej. bg_home.webp, bg_market.webp, title_aventuras.webp
├─ drawable/ # ej. btn_pause.webp, feedback_panel_success.webp (si no son vectores)
├─ mipmap-anydpi-v26/ # adaptive launcher icon (Image Asset Studio)
└─ values/strings.xml # contentDescription y textos

bash
Copiar código

## Listado de assets por pantalla

### HOME
- `drawable-nodpi/bg_home.webp` — **Fondo** pantalla Home.
- `drawable-nodpi/title_aventuras.webp` — Banner “Aventuras con Nutri”.
- `drawable-nodpi/home_illustration.webp` — Mascota Nutri.
- `drawable/logo_bamx.webp` (o **vector XML** si disponible).

### PADRES
- (Sin fondo de imagen) — se usa **degradado** en código.
- `drawable/ic_parents_progress.webp` — Progreso.
- `drawable/ic_parents_weight.webp` — Peso/Talla.
- `drawable/ic_parents_tips.webp` — Consejos.
- `drawable/ic_parents_time.webp` — Tiempo de juego.
> **Mejor** si estos 4 íconos vienen como **vector** (`.xml`).

### JUEGO (Clasificar)
- `drawable-nodpi/bg_market.webp` — Fondo del mercado.
- `drawable-nodpi/sign_wood.webp` — Cartel título.
- `drawable/btn_pause.webp` — Botón pausa (si no es vector).
- `drawable/feedback_panel_success.webp` — Panel verde feedback.
- Canastas:
    - `drawable/basket_daily.webp`
    - `drawable/basket_sometimes.webp`
    - `drawable/basket_rare.webp`
- Frutas (muestras):
    - `drawable/food_apple.webp`
    - `drawable/food_banana.webp`

## Strings de accesibilidad (contentDescription)
Asegúrate de tener en `res/values/strings.xml`:
```xml
<string name="cd_logo_bamx">Logotipo de BAMX Puebla</string>
<string name="cd_title_aventuras">Título Aventuras con Nutri</string>
<string name="cd_mascot_nutri">Nutri, la mascota</string>

<string name="cd_back">Atrás</string>
<string name="cd_icon_progress">Icono Progreso</string>
<string name="cd_icon_weight">Icono Peso y Talla</string>
<string name="cd_icon_tips">Icono Consejos</string>
<string name="cd_icon_time">Icono Tiempo de juego</string>

<string name="cd_pause">Botón de pausa</string>
<string name="cd_sign">Cartel del título</string>
<string name="cd_food">Alimento a clasificar</string>
<string name="cd_basket_daily">Canasta Diario</string>
<string name="cd_basket_sometimes">Canasta A veces</string>
<string name="cd_basket_rare">Canasta Ocasional</string>
Formatos recomendados
Fondos/ilustraciones: WebP (lossy) calidad 75–85. Colocar en drawable-nodpi/.

Si tienen texto fino, subir a 85–90 o usar WebP lossless.

Gráficos planos (íconos/figuras simples): VectorDrawable (XML).

Alternativa: WebP lossless o PNG si no hay vector.

Evitar JPG salvo fotos; preferir WebP.

Tamaños sugeridos (referencias)
Con drawable-nodpi/, Android no reescala por densidad. Exporta pensando en el ancho real que ocupará.

Fondos a pantalla completa (portrait): 1080×1920 px o 1242×2208 px.

Banners a lo ancho: ~1080 px de ancho.

Ilustración Nutri: si ocupa ~60–95% del ancho, exporta 900–1200 px de ancho.

Botón pausa / íconos: 96×96 px (o vector).

Canastas: entre 600–800 px de ancho cada una (según detalle).

Cómo convertir a WebP en Android Studio
En Project (vista Android), ubica la imagen PNG/JPG.

Right click → Convert to WebP…

Elige:

Lossless para íconos/flat graphics.

Lossy (quality 80–85) para fondos/fotos.

Marca Skip files if the encoded result is larger.

Finish. Verifica visualmente el resultado.

Colocación de assets (checklist)
 Nombres en lowercase_underscore, sin espacios.

 Fondos grandes en drawable-nodpi/.

 Iconografía preferentemente vector.

 contentDescription en strings.xml.

 Revisión en @Preview (411×891 y 360×640).

 Tamaño táctil ≥ 48dp si el asset es “clickable”.

Problemas comunes
Bordes blancos/letterboxing: fondo mal escalado → usa ContentScale.Crop y exporta mayor tamaño.

Imagen borrosa: asset pequeño en nodpi → reexporta más grande.

Colores “lavados”: exportado en P3/CMYK → reexporta sRGB.

Peso alto: baja calidad WebP o quita transparencia.

Nombres con mayúsculas/espacios: Android no compila; renombrar a lowercase_underscore.

markdown
Copiar código

---

### Mini-pasos por si quieres el “clic-path”
1) `Project` (vista Android) → **Right click** en la **raíz del proyecto** → **New > Directory** → `docs` → **OK**.  
2) **Right click** en `docs` → **New > File** → `README.md` → pega el contenido de arriba → **Save**.  
3) **Right click** en `docs` → **New > File** → `ASSETS_GUIDE.md` → pega el contenido de arriba → **Save**.
