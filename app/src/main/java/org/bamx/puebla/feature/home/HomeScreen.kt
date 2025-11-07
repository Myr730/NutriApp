package org.bamx.puebla.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import org.bamx.puebla.ui.responsive.SizeClass
import org.bamx.puebla.ui.responsive.rememberUiMetrics
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun HomeScreen(
    onPlayClick: () -> Unit = {},
    onParentsClick: () -> Unit = {}
) {
    val m = rememberUiMetrics()

    val mascotWidth = if (m.sizeClass == SizeClass.Small)
        (m.screenWidthDp * m.homeMascotFracSmall).dp
    else
        (m.screenWidthDp * m.homeMascotFracNormal*3     ).dp

    AppScaffold(
        backgroundResId = R.drawable.bg_home,
        darkenBackground = 0f
    ) {
        Box(Modifier.fillMaxSize()) {

            // 1) Logo + “BAMX Puebla” (arriba-izquierda)
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    // Evita solaparse con la barra de estado/notch
                    .windowInsetsPadding(WindowInsets.statusBars)
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
                    // Aire respecto a status bar
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 72.dp)
                    .fillMaxWidth(m.homeBannerWidthFraction),
                contentScale = ContentScale.Fit
            )

            // 3) Nutri a la derecha (misma posición, tamaño responsivo)
            Image(
                painter = painterResource(id = R.drawable.nutri_home),
                contentDescription = stringResource(id = R.string.cd_mascot_nutri),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 1.dp, bottom = 0.dp)
                    .width(mascotWidth),
                contentScale = ContentScale.Fit
            )

            // 4) Botones apilados (izquierda, parte baja)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(start = 24.dp, bottom = 48.dp),
                horizontalAlignment = Alignment.Start
            ) {
                PlayButton(
                    modifier = Modifier,
                    onClick = onPlayClick
                )
                Spacer(Modifier.height(16.dp))
                ParentsButton(
                    modifier = Modifier,
                    onClick = onParentsClick
                )
                Spacer(Modifier.height(16.dp))
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
