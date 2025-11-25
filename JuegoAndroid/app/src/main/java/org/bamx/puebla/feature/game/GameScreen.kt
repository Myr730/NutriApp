package org.bamx.puebla.feature.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.components.GameStatChip
import org.bamx.puebla.ui.components.PrimaryButton
import org.bamx.puebla.ui.components.SecondaryButton
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens
import org.bamx.puebla.ui.theme.board_bg_cream
import org.bamx.puebla.ui.theme.board_border_tan

@Composable
fun GameScreen() {
    // Activa tu fondo cuando lo tengas:
    AppScaffold(
        backgroundResId = R.drawable.bg_game,
        darkenBackground = 0.08f
    ) {
        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val isWide = screenWidthDp >= 600 // tablets o landscape ancho

        if (isWide) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.gameGutter)
            ) {
                GameBoard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                GameSidePanel(
                    modifier = Modifier
                        .widthIn(min = Dimens.gamePanelMinWidth)
                        .fillMaxHeight()
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.gameGutter)
            ) {
                GameBoard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                GameSidePanel(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/* ====== Sub-secciones UI estáticas ====== */

@Composable
private fun GameBoard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = board_bg_cream, // <- crema, en lugar de blanco puro
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        border = BorderStroke(2.dp, board_border_tan)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.cd_game_board),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GameSidePanel(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GameStatChip(
                    icon = Icons.Filled.Star,
                    label = stringResource(R.string.game_score),
                    value = "120"
                )
                Spacer(Modifier.width(8.dp))
                GameStatChip(
                    icon = Icons.Filled.Favorite,
                    label = stringResource(R.string.game_lives),
                    value = "3"
                )
                Spacer(Modifier.width(8.dp))
                GameStatChip(
                    icon = Icons.Filled.Schedule,
                    label = stringResource(R.string.game_time),
                    value = "01:20"
                )
            }

            Text(
                text = stringResource(R.string.game_instructions_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = stringResource(R.string.game_instructions_body),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(8.dp))

            PrimaryButton(
                text = stringResource(R.string.game_btn_start),
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                text = stringResource(R.string.game_btn_reset),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/* ---------- PREVIEWS ---------- */

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
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewGameDarkSmall() {
    AppTheme(darkTheme = true) { GameScreen() }
}
