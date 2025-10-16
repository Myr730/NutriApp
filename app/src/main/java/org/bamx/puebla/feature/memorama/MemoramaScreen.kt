package org.bamx.puebla.feature.memorama

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens.memoramaCardCorner
import org.bamx.puebla.ui.theme.Dimens.memoramaGridHSpace
import org.bamx.puebla.ui.theme.Dimens.memoramaGridVSpace
import org.bamx.puebla.ui.theme.timeout_header_green

@Composable
fun MemoramaScreen() {
    AppScaffold(
        backgroundResId = R.drawable.bg_memorama,
        darkenBackground = 0f,
        padTopBar = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarMemorama()

            Spacer(Modifier.height(20.dp))

            MemoramaGridContainer()

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }
    }
}

@Composable
private fun TopBarMemorama() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .heightIn(min = 68.dp)
            .padding(top = 8.dp)
    ) {
        TitleBanner(
            text = stringResource(id = R.string.memorama_title),
            modifier = Modifier.align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_pause),
            contentDescription = stringResource(id = R.string.cd_pause),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
                .size(52.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
@Suppress("UnusedBoxWithConstraintsScope") // Se suprime el falso positivo del IDE
private fun MemoramaGridContainer() {
    val cardAspectRatio = 0.68f
    val columns = 3
    val rows = 4
    val contentPaddingHorizontal = 12.dp
    val contentPaddingVerticalTop = 12.dp
    val contentPaddingVerticalBottom = 16.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(0.82f),
        contentAlignment = Alignment.Center
    ) {
        val hPaddingPx = with(LocalDensity.current) { contentPaddingHorizontal.toPx() * 2 }
        val hSpacingPx = with(LocalDensity.current) { memoramaGridHSpace.toPx() * (columns - 1) }
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }

        val cardWidthPx = (maxWidthPx - hPaddingPx - hSpacingPx) / columns
        val cardWidthDp = with(LocalDensity.current) { cardWidthPx.toDp() }

        val cardHeightDp = cardWidthDp / cardAspectRatio

        val totalGridHeight = (cardHeightDp * rows) +
            (memoramaGridVSpace * (rows - 1)) +
            contentPaddingVerticalTop +
            contentPaddingVerticalBottom

        Box(
            modifier = Modifier.height(totalGridHeight)
        ) {
            val items = List(12) { it }

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(memoramaGridHSpace),
                verticalArrangement = Arrangement.spacedBy(memoramaGridVSpace),
                contentPadding = PaddingValues(
                    start = contentPaddingHorizontal,
                    end = contentPaddingHorizontal,
                    top = contentPaddingVerticalTop,
                    bottom = contentPaddingVerticalBottom
                ),
                userScrollEnabled = false
            ) {
                items(items) {
                    MemoramaCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(cardAspectRatio)
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.88f) // El ancho ahora es una propiedad interna
            .clip(RoundedCornerShape(20.dp))
            .background(timeout_header_green)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                lineHeight = 26.sp
            )
        )
    }
}

@Composable
private fun MemoramaCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.card_back),
            contentDescription = stringResource(id = R.string.cd_card_back),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(memoramaCardCorner))
        )
        Image(
            painter = painterResource(id = R.drawable.ic_card_placeholder),
            contentDescription = stringResource(id = R.string.cd_card_placeholder),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth(0.45f)
        )
    }
}

@Preview(
    name = "Memorama - 411x891 Light",
    showBackground = true,
    backgroundColor = 0xFFFDEFD9,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewMemoramaLight() {
    AppTheme(darkTheme = false) { MemoramaScreen() }
}

@Preview(
    name = "Memorama - 360x640 Dark",
    showBackground = true,
    backgroundColor = 0xFFFDEFD9,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewMemoramaDark() {
    AppTheme(darkTheme = true) { MemoramaScreen() }
}
