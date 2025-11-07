package org.bamx.puebla.feature.armatuplato

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalConfiguration


@Composable
fun ArmaTuPlatoScreen(
    modifier: Modifier = Modifier,
    centerIngredientRes: Int = R.drawable.food_broccoli,
    onBackClick: () -> Unit = {} // ← AGREGAR ESTE PARÁMETRO
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_arma_plato),
            contentDescription = stringResource(id = R.string.cd_background),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ArmaPlatoTopBar(onBackClick) // ← Pasar el callback al TopBar

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f),
                contentAlignment = Alignment.Center
            ) {
                PlateSection(centerIngredientRes)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ButtonSection()
            }
        }
    }
}


@Composable
private fun ArmaPlatoTopBar(onBackClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isSmall = configuration.screenWidthDp <= 360
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            .height(IntrinsicSize.Min)
    ) {
        TitleBanner(
            text = stringResource(id = R.string.arma_tu_plato_title),
            modifier = Modifier.align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp)
                .size(if (isSmall) 44.dp else 52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun PlateSection(centerIngredientRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.plate_empty),
            contentDescription = stringResource(id = R.string.cd_plate),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = centerIngredientRes),
            contentDescription = stringResource(id = R.string.cd_food_on_plate),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(0.30f)
        )
    }
}

@Composable
private fun ButtonSection() {
    GreenButton(label = stringResource(id = R.string.arma_tu_plato_ready))

    Spacer(Modifier.height(16.dp))

    FoodGrid2x2(
        items = listOf(
            FoodItem(R.drawable.food_broccoli, R.string.cd_food_broccoli),
            FoodItem(R.drawable.food_meat, R.string.cd_food_meat),
            FoodItem(R.drawable.food_apple, R.string.cd_food_apple),
            FoodItem(R.drawable.food_cheese, R.string.cd_food_cheese),
        )
    )

    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}


/* -------------------------------------------------------------------------- */
/* UI Helpers                                                                */
/* -------------------------------------------------------------------------- */

@Composable
private fun TitleBanner(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(0.86f),
        color = Color(0xFFE75B2A),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 8.dp,
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, lineHeight = 28.sp
            )
        )
    }
}

@Composable
private fun GreenButton(label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(0.66f),
        color = Color(0xFF2FA34A),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 8.dp
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.padding(vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold, fontSize = 22.sp
            )
        )
    }
}

private data class FoodItem(val res: Int, val cd: Int)

@Composable
private fun FoodGrid2x2(items: List<FoodItem>, modifier: Modifier = Modifier) {
    // AHORA NO NECESITAMOS BOXWITHCONSTRAINTS. El grid se encarga de todo.
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 4.dp)
    ) {
        items(items) { item ->
            Card(
                // <<< LA MAGIA ESTÁ AQUÍ
                // Ocupa el ancho de la columna y fuerza el alto a ser igual.
                modifier = Modifier.aspectRatio(1f),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF65C8C9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = CardDefaults.outlinedCardBorder(true).copy(
                    width = 2.dp, brush = SolidColor(Color(0xFF1E7B82))
                )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = item.res),
                        contentDescription = stringResource(id = item.cd),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(0.7f)
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Arma tu plato – 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewArmaTuPlatoLight() {
    AppTheme(darkTheme = false) { ArmaTuPlatoScreen() }
}

@Preview(
    name = "Arma tu plato – 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewArmaTuPlatoDark() {
    AppTheme(darkTheme = true) { ArmaTuPlatoScreen() }
}
