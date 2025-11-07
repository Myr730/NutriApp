package org.bamx.puebla.feature.game

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.responsive.rememberUiMetrics
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens
import androidx.compose.foundation.layout.WindowInsets

@Composable
fun ClassifyMockScreen(
    onBackClick: () -> Unit = {}, // ← AGREGAR parámetro de navegación
    showSuccessPanel: Boolean = true,
    @DrawableRes currentFoodRes: Int = R.drawable.food_apple,
) {
    val m = rememberUiMetrics()

    // AGREGAR: Detectar si es pantalla pequeña
    val configuration = LocalConfiguration.current
    val isSmall = configuration.screenWidthDp <= 360

    val signWidthFraction = m.gameSignWidthFraction
    val titleFontSize = m.gameTitleFontSizeSp.sp
    val titleLineHeight = m.gameTitleLineHeightSp.sp
    val fruitWidth = (m.screenWidthDp * m.gameFruitWidthFraction).dp
    val successWidthFraction = m.gameSuccessWidthFraction

    val basketHeight: Dp = (m.screenWidthDp * m.gameBasketHeightFraction).dp
    val bottomPadding = if (m.sizeClass == org.bamx.puebla.ui.responsive.SizeClass.Small) 8.dp else 10.dp

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo
        Image(
            painter = painterResource(id = R.drawable.bg_market),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Botón de regresar - CORREGIDO
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars) // ← AGREGAR este padding
                .padding(start = 12.dp, top = 12.dp)
                .size(if (isSmall) 44.dp else 52.dp)
                .clickable {
                    println("DEBUG: Botón regresar ClassifyMockScreen clickeado")
                    onBackClick()
                }, // ← USAR el parámetro
            contentScale = ContentScale.Fit
        )

        // Cartel centrado + título (2 líneas)
        Box(
            modifier = Modifier
                .fillMaxWidth(signWidthFraction)
                .align(Alignment.TopCenter)
                .padding(top = Dimens.classifySignTop + Dimens.classifySignExtraTop)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sign_wood),
                contentDescription = stringResource(id = R.string.cd_sign),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.classify_title_multiline),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = titleFontSize,
                        lineHeight = titleLineHeight,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.35f),
                            offset = Offset(0f, 2f),
                            blurRadius = 6f
                        )
                    )
                )
            }
        }

        // Fruta al centro
        Image(
            painter = painterResource(id = currentFoodRes),
            contentDescription = stringResource(id = R.string.cd_food),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .width(fruitWidth)
        )

        // Panel verde (centrado)
        if (showSuccessPanel) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = fruitWidth * 0.85f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.feedback_panel_success),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(successWidthFraction)
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillWidth
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 4.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.classify_feedback_title_text),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.35f),
                                    offset = Offset(0f, 2f),
                                    blurRadius = 6f
                                )
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.classify_feedback_desc_text),
                        color = Color(0xFFEEF088),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // Canastas
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = Dimens.classifyScreenPadding, vertical = bottomPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.classifyBasketsSpacing)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Basket(
                    label = stringResource(id = R.string.classify_daily),
                    resId = R.drawable.basket_daily,
                    height = basketHeight
                )
                Basket(
                    label = stringResource(id = R.string.classify_sometimes),
                    resId = R.drawable.basket_sometimes,
                    height = basketHeight
                )
                Basket(
                    label = stringResource(id = R.string.classify_rare),
                    resId = R.drawable.basket_rare,
                    height = basketHeight
                )
            }
        }
    }
}

/** RowScope para poder usar .weight(1f) en cada canasta */
@Composable
private fun RowScope.Basket(
    label: String,
    resId: Int,
    height: Dp,
    onClick: () -> Unit = {} // ← OPCIONAL: Hacer las canastas clickeables
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(height)
            .semantics {
                role = Role.Button
                stateDescription = label
            }
            .clickable { onClick() } // ← OPCIONAL: Hacer clickeable
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = when (resId) {
                R.drawable.basket_daily -> stringResource(id = R.string.cd_basket_daily)
                R.drawable.basket_sometimes -> stringResource(id = R.string.cd_basket_sometimes)
                else -> stringResource(id = R.string.cd_basket_rare)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Text(
            text = label,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.35f),
                    offset = Offset(0f, 2f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = Dimens.classifyBasketLabelTop)
        )
    }
}


/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Classify - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewClassifyLight() {
    AppTheme(darkTheme = false) {
        ClassifyMockScreen(showSuccessPanel = true, currentFoodRes = R.drawable.food_apple)
    }
}

@Preview(
    name = "Classify - 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewClassifyDark() {
    AppTheme(darkTheme = true) {
        ClassifyMockScreen(showSuccessPanel = true, currentFoodRes = R.drawable.food_banana)
    }
}
