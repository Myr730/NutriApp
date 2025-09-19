package org.bamx.puebla.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.components.ParentsButton
import org.bamx.puebla.ui.components.PlayButton
import org.bamx.puebla.ui.components.SettingsButton
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun HomeScreen() {
    AppScaffold(
        backgroundResId = R.drawable.bg_home,
        darkenBackground = 0f
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        // Nutri más grande (sin límites): subimos las fracciones
        val fracSmall = 0.75f   // teléfonos pequeños (≤360dp)
        val fracNormal = 0.85f  // teléfonos “normales” (>360dp)
        val mascotWidth = if (screenWidth <= 360.dp)
            (screenWidth.value * fracSmall).dp
        else
            (screenWidth.value * fracNormal).dp

        Box(Modifier.fillMaxSize()) {

            // 1) Logo + “BAMX Puebla” (arriba-izquierda)
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = Dimens.space16, start = Dimens.space16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_bamx),
                    contentDescription = stringResource(id = R.string.cd_logo_bamx),
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.brand_bamx),
                        style = MaterialTheme.typography.titleLarge.merge(
                            TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 18.sp,
                                lineHeight = 20.sp
                            )
                        )
                    )
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.brand_puebla),
                        style = MaterialTheme.typography.labelLarge.merge(
                            TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                lineHeight = 14.sp
                            )
                        )
                    )
                }
            }

            // 2) Banner “Aventuras con Nutri” (centrado arriba)
            Image(
                painter = painterResource(id = R.drawable.title_aventuras),
                contentDescription = stringResource(id = R.string.cd_title_aventuras),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 72.dp)
                    .fillMaxWidth(0.86f),
                contentScale = ContentScale.Fit
            )

            // 3) Nutri a la derecha (MISMA posición, solo más grande)
            Image(
                painter = painterResource(id = R.drawable.home_illustration),
                contentDescription = stringResource(id = R.string.cd_mascot_nutri),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 1.dp, bottom = 0.dp)
                    .width(mascotWidth),     // sin límites extra
                contentScale = ContentScale.Fit
            )

            // 4) Botones apilados (izquierda, parte baja)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.dp, bottom = 48.dp),
                horizontalAlignment = Alignment.Start
            ) {
                PlayButton()
                Spacer(Modifier.height(16.dp))
                ParentsButton()
                Spacer(Modifier.height(16.dp))
                SettingsButton()
            }
        }
    }
}

/* ---------- PREVIEWS ---------- */

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
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewHomeDarkSmall() {
    AppTheme(darkTheme = true) { HomeScreen() }
}
