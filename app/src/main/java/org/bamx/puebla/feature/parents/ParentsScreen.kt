package org.bamx.puebla.feature.parents

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.components.FeatureCardLarge
import org.bamx.puebla.ui.components.SplitHeader
import org.bamx.puebla.ui.theme.*

@Composable
fun ParentsScreen() {
    AppScaffold(
        backgroundResId = null,
        darkenBackground = 0f,
        padTopBar = false,
        contentPadding = PaddingValues(0.dp), // <- SIN padding para que el degradado cubra todo
        topBar = {
            SplitHeader(
                title = stringResource(id = R.string.parents_title_big),
                backgroundColor = header_cream,
                leadingContentDescription = stringResource(id = R.string.cd_back)
            )
        }
    ) {
        // Fondo degradado a PANTALLA COMPLETA (sin bordes blancos)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(parents_bg_top, parents_bg_bottom)
                    )
                )
                .padding(Dimens.screenPadding) // padding interno, no afecta el degradado
        ) {
            FeatureCardLarge(
                title = stringResource(id = R.string.parents_card_progress),
                background = card_yellow,
                // Usa tus imágenes si ya las integraste:
                // leadingResId = R.drawable.ic_progress,
                leadingResId = R.drawable.ic_parents_progress,
                leadingContentDescription = stringResource(id = R.string.cd_icon_progress),
                modifier = Modifier.padding(bottom = Dimens.space20)
            )
            FeatureCardLarge(
                title = stringResource(id = R.string.parents_card_weight),
                background = card_orange,
                // leadingResId = R.drawable.ic_weight,
                leadingResId = R.drawable.ic_parents_weight,
                leadingContentDescription = stringResource(id = R.string.cd_icon_weight),
                modifier = Modifier.padding(bottom = Dimens.space20)
            )
            FeatureCardLarge(
                title = stringResource(id = R.string.parents_card_tips),
                background = card_teal,
                // leadingResId = R.drawable.ic_tips,
                leadingResId = R.drawable.ic_parents_tips,
                leadingContentDescription = stringResource(id = R.string.cd_icon_tips),
                modifier = Modifier.padding(bottom = Dimens.space20)
            )
            FeatureCardLarge(
                title = stringResource(id = R.string.parents_card_time),
                background = card_pink,
                // leadingResId = R.drawable.ic_time,
                leadingResId = R.drawable.ic_parents_time,
                leadingContentDescription = stringResource(id = R.string.cd_icon_time)
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
    AppTheme(darkTheme = false) { ParentsScreen() }
}

@Preview(
    name = "Parents - 360x640 Dark",
    showBackground = true,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewParentsDarkSmall() {
    AppTheme(darkTheme = true) { ParentsScreen() }
}
