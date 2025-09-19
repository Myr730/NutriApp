package org.bamx.puebla.feature.game

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
fun GameScreen() {
    AppScaffold(
        backgroundResId =  R.drawable.bg_game ,
        darkenBackground = 0.18f // un poco más oscuro para tablero
    ) {
        Text(
            text = stringResource(id = R.string.game_title),
            style = MaterialTheme.typography.headlineMedium
        )
        // Tablero + panel lateral (Bloque 9)
    }
}


@Preview(
    name = "Game - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewGameLight() {
    AppTheme(darkTheme = false) { GameScreen() }
}

@Preview(
    name = "Game - 360x640 Dark",
    showBackground = true,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewGameDarkSmall() {
    AppTheme(darkTheme = true) { GameScreen() }
}
