package org.bamx.puebla.feature.parents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.components.FeatureCardLarge
import org.bamx.puebla.ui.responsive.SizeClass
import org.bamx.puebla.ui.responsive.rememberUiMetrics
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens
import org.bamx.puebla.ui.theme.card_green
import org.bamx.puebla.ui.theme.card_orange
import org.bamx.puebla.ui.theme.card_pink
import org.bamx.puebla.ui.theme.card_teal
import org.bamx.puebla.ui.theme.card_yellow
import org.bamx.puebla.ui.theme.parents_bg_bottom
import org.bamx.puebla.ui.theme.parents_bg_top

@Composable
fun ParentsScreen(
    onBackClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},    // Nueva: navegación a ProgressScreen
    onTipsClick: () -> Unit = {},        // Nueva: navegación a ConsejosScreen
    onTimeClick: () -> Unit = {}         // Nueva: navegación a AdjustTimeScreen
) {
    val m = rememberUiMetrics()
    val isSmall = m.sizeClass == SizeClass.Small
    val cardsSpacing = if (isSmall) Dimens.space16 else Dimens.space20

    AppScaffold(
        backgroundResId = null,
        darkenBackground = 0f,
        padTopBar = false,
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo degradado con scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(colors = listOf(parents_bg_top, parents_bg_bottom))
                    )
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(Dimens.screenPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Espacio para la flecha en la parte superior
                Spacer(modifier = Modifier.height(140.dp))

                // Contenido de las tarjetas
                Column(verticalArrangement = Arrangement.spacedBy(cardsSpacing)) {
                    // Tarjeta PROGRESO
                    FeatureCardLarge(
                        title = stringResource(id = R.string.parents_card_progress),
                        background = card_yellow,
                        leadingResId = R.drawable.ic_parents_progress,
                        leadingContentDescription = stringResource(id = R.string.cd_icon_progress),
                        modifier = Modifier
                            .semantics { role = Role.Button }
                            .clickable {
                                println("NAVIGATION: Navegando a ProgressScreen")
                                onProgressClick()
                            }
                    )
                    Spacer(modifier = Modifier.height(60.dp))


                    // Tarjeta CONSEJOS
                    FeatureCardLarge(
                        title = stringResource(id = R.string.parents_card_tips),
                        background = card_teal,
                        leadingResId = R.drawable.ic_parents_tips,
                        leadingContentDescription = stringResource(id = R.string.cd_icon_tips),
                        modifier = Modifier
                            .semantics { role = Role.Button }
                            .clickable {
                                println("NAVIGATION: Navegando a ConsejosScreen")
                                onTipsClick()
                            }
                    )

                    Spacer(modifier = Modifier.height(60.dp))


                    // Tarjeta TIEMPO
                    FeatureCardLarge(
                        title = stringResource(id = R.string.parents_card_time),
                        background = card_pink,
                        leadingResId = R.drawable.ic_parents_time,
                        leadingContentDescription = stringResource(id = R.string.cd_icon_time),
                        modifier = Modifier
                            .semantics { role = Role.Button }
                            .clickable {
                                println("NAVIGATION: Navegando a AdjustTimeScreen")
                                onTimeClick()
                            }
                    )
                }
            }

            // Flecha de regreso POSICIONADA SOBRE el contenido
            Image(
                painter = painterResource(id = R.drawable.ic_back2),
                contentDescription = stringResource(id = R.string.cd_back),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 24.dp)
                    .size(if (isSmall) 44.dp else 52.dp)
                    .clickable {
                        println("DEBUG: Botón regresar ParentsScreen clickeado")
                        onBackClick()
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Parents - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewParentsLight() {
    AppTheme(darkTheme = false) {
        ParentsScreen(
            onBackClick = {},
            onProgressClick = {},
            onTipsClick = {},
            onTimeClick = {}
        )
    }
}

@Preview(
    name = "Parents - 360x640 Dark",
    showBackground = true,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewParentsDarkSmall() {
    AppTheme(darkTheme = true) {
        ParentsScreen(
            onBackClick = {},
            onProgressClick = {},
            onTipsClick = {},
            onTimeClick = {}
        )
    }
}
