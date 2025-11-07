package org.bamx.puebla.feature.guess

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.graphics.Color

@Composable
fun GuessTheVegetableScreen(
    onBackClick: () -> Unit = {}
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.guess_title), // “¡ADIVINA LA VERDURA!”
    wordPlaceholders: String = "_  _  _  _  _",
    @DrawableRes backgroundRes: Int = R.drawable.bg_guess,           // fondo exterior
    @DrawableRes characterRes: Int = R.drawable.nutri_guess

) {
    Box(modifier = modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = stringResource(id = R.string.cd_background),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Botón de regresar
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp)
                .size(52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TopBar: back + cartel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = backRes),
                    contentDescription = stringResource(id = R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp)
                )

                TitleBanner(
                    text = title,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.66f)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Guiones bajos (placeholder de la palabra)
            Text(
                text = wordPlaceholders,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    lineHeight = 28.sp
                )
            )

            // Personaje al centro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = characterRes),
                    contentDescription = stringResource(id = R.string.cd_character),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth(0.50f)
                        .padding(top = 8.dp)
                )
            }

            // Teclado (A–Z) – 7 columnas
            KeyboardGrid(
                letters = ('A'..'Z').map { it.toString() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

/* ---------- COMPONENTES ---------- */

@Composable
private fun TitleBanner(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color(0xFFE75B2A),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 8.dp
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
        )
    }
}

@Composable
private fun KeyboardGrid(
    letters: List<String>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 6.dp)
    ) {
        items(letters) { key ->
            Surface(
                color = Color(0xFF74B86A), // verde amable
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 6.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.0f), // tecla cuadrada
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = key,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            }
        }
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Guess – 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewGuessLight() {
    AppTheme(darkTheme = false) { GuessTheVegetableScreen() }
}

@Preview(
    name = "Guess – 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewGuessDark() {
    AppTheme(darkTheme = true) { GuessTheVegetableScreen() }
}
