package org.bamx.puebla.feature.parents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens
import org.bamx.puebla.ui.components.AppScaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

@Composable
fun ParentsScreen() {
    AppScaffold(
        backgroundResId = R.drawable.bg_parents,
        darkenBackground = 0.12f
    ) {
        Text(
            text = stringResource(id = R.string.parents_title),
            style = MaterialTheme.typography.headlineMedium
        )
        // Tarjetas y placeholders (Bloque 8)
    }
}

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
