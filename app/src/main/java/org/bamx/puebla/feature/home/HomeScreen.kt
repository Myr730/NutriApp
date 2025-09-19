package org.bamx.puebla.feature.home

import org.bamx.puebla.ui.components.AppScaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun HomeScreen() {
    AppScaffold(
        // Cambia a R.drawable.bg_home cuando el asset esté listo:
        backgroundResId = R.drawable.bg_home,
        darkenBackground = 0.12f // leve scrim para contraste
    ) {
        Text(
            text = stringResource(id = R.string.home_title),
            style = MaterialTheme.typography.headlineMedium
        )
        // Aquí seguirá la maquetación (Bloque 7)
    }
}

@Preview(
    name = "Home - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewHomeLight() {
    AppTheme(darkTheme = false) { HomeScreen() }
}

@Preview(
    name = "Home - 360x640 Dark",
    showBackground = true,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewHomeDarkSmall() {
    AppTheme(darkTheme = true) { HomeScreen() }
}
