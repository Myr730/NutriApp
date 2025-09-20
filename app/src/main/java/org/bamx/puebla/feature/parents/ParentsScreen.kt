package org.bamx.puebla.feature.parents

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.components.FeatureCardLarge
import org.bamx.puebla.ui.components.SplitHeader
import org.bamx.puebla.ui.responsive.SizeClass
import org.bamx.puebla.ui.responsive.rememberUiMetrics
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens
import org.bamx.puebla.ui.theme.card_orange
import org.bamx.puebla.ui.theme.card_pink
import org.bamx.puebla.ui.theme.card_teal
import org.bamx.puebla.ui.theme.card_yellow
import org.bamx.puebla.ui.theme.header_cream
import org.bamx.puebla.ui.theme.parents_bg_bottom
import org.bamx.puebla.ui.theme.parents_bg_top

@Composable
fun ParentsScreen() {
    val m = rememberUiMetrics()
    // Espaciado vertical responsivo entre tarjetas
    val cardsSpacing = if (m.sizeClass == SizeClass.Small) Dimens.space16 else Dimens.space20

    AppScaffold(
        backgroundResId = null,
        darkenBackground = 0f,
        padTopBar = false,
        contentPadding = PaddingValues(0.dp), // sin padding: el degradado cubre TODA la pantalla
        topBar = {
            SplitHeader(
                title = stringResource(id = R.string.parents_title_big),
                backgroundColor = header_cream,
                leadingContentDescription = stringResource(id = R.string.cd_back)
            )
        }
    ) {
        // Fondo degradado a PANTALLA COMPLETA, con scroll y padding para barra de gestos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(colors = listOf(parents_bg_top, parents_bg_bottom))
                )
                .windowInsetsPadding(WindowInsets.navigationBars) // evita solape con gestos abajo
                .padding(Dimens.screenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // usa spacing uniforme entre tarjetas
            Column(verticalArrangement = Arrangement.spacedBy(cardsSpacing)) {
                FeatureCardLarge(
                    title = stringResource(id = R.string.parents_card_progress),
                    background = card_yellow,
                    leadingResId = R.drawable.ic_parents_progress,
                    leadingContentDescription = stringResource(id = R.string.cd_icon_progress),
                    modifier = Modifier
                        .semantics { role = Role.Button } // accesible como botón
                )

                FeatureCardLarge(
                    title = stringResource(id = R.string.parents_card_weight),
                    background = card_orange,
                    leadingResId = R.drawable.ic_parents_weight,
                    leadingContentDescription = stringResource(id = R.string.cd_icon_weight),
                    modifier = Modifier
                        .semantics { role = Role.Button }
                )

                FeatureCardLarge(
                    title = stringResource(id = R.string.parents_card_tips),
                    background = card_teal,
                    leadingResId = R.drawable.ic_parents_tips,
                    leadingContentDescription = stringResource(id = R.string.cd_icon_tips),
                    modifier = Modifier
                        .semantics { role = Role.Button }
                )

                FeatureCardLarge(
                    title = stringResource(id = R.string.parents_card_time),
                    background = card_pink,
                    leadingResId = R.drawable.ic_parents_time,
                    leadingContentDescription = stringResource(id = R.string.cd_icon_time),
                    modifier = Modifier
                        .semantics { role = Role.Button }
                )
            }
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
    AppTheme(darkTheme = false) { ParentsScreen() }
}

@Preview(
    name = "Parents - 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewParentsDarkSmall() {
    AppTheme(darkTheme = true) { ParentsScreen() }
}
