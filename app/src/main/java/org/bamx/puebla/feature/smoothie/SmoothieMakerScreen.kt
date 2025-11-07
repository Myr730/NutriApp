package org.bamx.puebla.feature.smoothie

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import androidx.compose.foundation.layout.WindowInsets

@Composable
fun SmoothieMakerScreen(
    onBackClick: () -> Unit = {}, // ← AGREGAR parámetro de navegación
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 1) Fondo (cocina + Nutri + licuadora)
        Image(
            painter = painterResource(id = R.drawable.bg_smoothie),
            contentDescription = stringResource(R.string.cd_bg_smoothie),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2) Botón de regreso (arriba-izquierda) - AGREGAR .clickable
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 12.dp, top = 12.dp)
                .size(48.dp)
                .clickable { onBackClick() }, // ← AGREGAR navegación
            contentScale = ContentScale.Fit
        )

        // 3) Cartel superior + cápsula de ingredientes
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBanner(text = stringResource(R.string.smoothie_title))
            Spacer(Modifier.height(10.dp))
            RequiredIngredientsCapsule(
                ingredients = listOf(
                    R.drawable.food_strawberry,
                    R.drawable.food_strawberry,
                    R.drawable.food_banana
                )
            )
        }

        // 4) Frutas disponibles (fila inferior) centradas con la franja café
        BottomFruitsRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(vertical = 12.dp),
            rowWidthFraction = 0.90f,
            bottomPadding = 4.dp,
            fruits = listOf(
                R.drawable.food_grapes,
                R.drawable.food_apple,
                R.drawable.food_banana,
                R.drawable.food_strawberry,
                R.drawable.food_orange
            )
        )
    }
}

/* ---------- Piezas UI ---------- */

@Composable
private fun TitleBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .wrapContentWidth()
            .shadow(8.dp, RoundedCornerShape(18.dp)),
        color = Color(0xFFE75B2A),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                lineHeight = 20.sp
            )
        )
    }
}

@Composable
private fun RequiredIngredientsCapsule(
    ingredients: List<Int>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .wrapContentWidth()
            .shadow(6.dp, RoundedCornerShape(14.dp)),
        color = Color(0xFFFDFDFD),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ingredients.forEach { res ->
                Image(
                    painter = painterResource(id = res),
                    contentDescription = stringResource(R.string.cd_required_ingredient),
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun BottomFruitsRow(
    fruits: List<Int>,
    modifier: Modifier = Modifier,
    rowWidthFraction: Float = 0.84f,
    bottomPadding: Dp = 4.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth(rowWidthFraction)
            .padding(bottom = bottomPadding)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        fruits.forEach { res ->
            FruitChip(
                resId = res,
                onClick = {
                    // Lógica cuando se hace click en una fruta
                    println("Fruta clickeada: $res")
                    // Aquí podrías agregar la fruta a la licuadora
                }
            )
        }
    }
}

@Composable
private fun FruitChip(
    resId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // ← AGREGAR parámetro para el click
) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .size(63.dp)
            .clickable { onClick() } // ← Hacer las frutas clickeables
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = stringResource(R.string.cd_food_apple),
            contentScale = ContentScale.Fit
        )
    }
}

/* ---------- Previews ---------- */

@Preview(
    name = "Smoothie – 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewSmoothieLight() {
    AppTheme(darkTheme = false) {
        SmoothieMakerScreen(
            onBackClick = {} // ← AGREGAR para el preview
        )
    }
}

@Preview(
    name = "Smoothie – 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewSmoothieDark() {
    AppTheme(darkTheme = true) {
        SmoothieMakerScreen(
            onBackClick = {} // ← AGREGAR para el preview
        )
    }
}
