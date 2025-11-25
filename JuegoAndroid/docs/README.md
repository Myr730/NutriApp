# App BAMX Puebla — UI (Jetpack Compose)

## Resumen
- **Scope:** UI estática (sin navegación real ni lógica), 3 pantallas: **Home**, **Padres**, **Juego**.
- **Min SDK:** 24 | **Target/Compile:** 34 | **Kotlin:** 1.9.x | **Compose Compiler:** 1.5.x
- **Arquitectura por features:** `feature/home`, `feature/parents`, `feature/game`.
- **Theming centralizado:** `ui/theme` (Material 3 + tokens en `Dimens.kt`).
- **Responsivo:** helper `ui/responsive/UiMetrics.kt`.

## Estructura principal
app/src/main/java/org/bamx/puebla/
├─ feature/
│ ├─ home/HomeScreen.kt
│ ├─ parents/ParentsScreen.kt
│ └─ game/ClassifyMockScreen.kt
├─ ui/
│ ├─ components/ (botones, scaffold, etc.)
│ ├─ responsive/UiMetrics.kt
│ └─ theme/ (Theme.kt, Color.kt, Type.kt, Shape.kt, Dimens.kt)
└─ ...
app/src/main/res/
├─ drawable-nodpi/ # fondos y bitmaps no escalables por densidad
├─ drawable/ # íconos bitmap generales (si no son vectores)
├─ mipmap-anydpi-v26/ # adaptive launcher icon
├─ values/strings.xml # i18n
└─ ...

markdown
Copiar código

## Cómo correr
- **Previews:** abrir cada `@Preview` en Home/Parents/Game.
- **Build:** `Build > Rebuild Project`.
- **A11y:** imágenes decorativas con `contentDescription=null`; botones con `semantics { role = Role.Button }`.
- **Tamaños táctiles:** mínimo `48.dp`.

## Calidad (opcional pero recomendado)
- **ktlint:** `./gradlew ktlintFormat` y `./gradlew ktlintCheck`
- **detekt:** `./gradlew detekt` (baseline en `config/detekt/baseline.xml`)
- Config en `app/build.gradle.kts` y `config/detekt/detekt.yml`.

## Recursos / Assets (resumen)
- Fondos y grandes bitmaps: `res/drawable-nodpi/` (formato **WebP**).
- Íconos simples/flat: preferir **VectorDrawable** (`res/drawable/` XML). Si es bitmap, PNG/WebP.
- Nombres en `lowercase_underscore`, sin espacios.

## Pantallas (estado actual)
- **Home:** fondo `bg_home.webp`, banner `title_aventuras.webp`, mascota `home_illustration.webp`, botones: Jugar/Padres/Ajustes.
- **Padres:** header crema con `SplitHeader`, fondo degradado en código (sin imagen), 4 **FeatureCardLarge** con íconos.
- **Juego (Clasificar):** `bg_market.webp`, cartel `sign_wood.webp`, botón pausa `btn_pause.webp`, panel verde `feedback_panel_success.webp`, canastas (3), frutas.

> Cualquier asset faltante está documentado en `docs/ASSETS_GUIDE.md`.
