package org.bamx.puebla.feature.timeout

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.theme.*
import org.bamx.puebla.ui.theme.Dimens.timeoutHeaderCorner
import org.bamx.puebla.ui.theme.Dimens.timeoutPanelCorner
import org.bamx.puebla.ui.theme.Dimens.timeoutScreenPadding

@Composable
fun TimeoutScreen(
    onBreakCompleted: () -> Unit = {},
    remainingTime: Long
) {
    val minutes = remainingTime / 60
    val seconds = remainingTime % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    val screenW = LocalConfiguration.current.screenWidthDp
    val isSmall = screenW <= 360

    val signWidthFraction = if (isSmall) 0.94f else 0.90f
    val mascotWidthFraction = if (isSmall) 0.62f else 0.58f

    val timerSize = if (isSmall) 96.dp else 108.dp
    val timerBottomPadding = 20.dp
    val bottomClearance = timerSize + timerBottomPadding + 6.dp

    AppScaffold(
        backgroundResId = R.drawable.bg_timeout,
        darkenBackground = 0f,
        padTopBar = false
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = timeoutScreenPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(12.dp))

                TimeoutHeaderAndBody(
                    title = stringResource(R.string.timeout_title),
                    body = stringResource(R.string.timeout_body),
                    widthFraction = signWidthFraction
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.nutri_timeout),
                        contentDescription = stringResource(id = R.string.cd_timeout_mascot),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = bottomClearance)
                            .fillMaxWidth(mascotWidthFraction * 3)
                    )
                }

                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }

            TimeoutTimer(
                timeText = timeText,
                size = timerSize,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = timerBottomPadding)
            )
        }
    }
}


@Composable
private fun TimeoutHeaderAndBody(
    title: String,
    body: String,
    widthFraction: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(widthFraction),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado verde más alto y con esquinas grandes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = timeoutHeaderCorner, topEnd = timeoutHeaderCorner))
                .background(timeout_header_green)
                .padding(horizontal = 24.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = title,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    lineHeight = 28.sp
                )
            )
        }

        // Panel beige con buen padding y contraste
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = timeoutPanelCorner, bottomEnd = timeoutPanelCorner))
                .background(timeout_panel_beige)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            androidx.compose.material3.Text(
                text = body,
                color = Color(0xFF3E3326),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp
                )
            )
        }
    }
}

@Composable
private fun TimeoutTimer(
    timeText: String,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_timer_round),
            contentDescription = stringResource(R.string.cd_timeout_timer),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        androidx.compose.material3.Text(
            text = timeText,
            color = Color(0xFF3E3326),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

/* ---------- PREVIEWS CORREGIDAS ---------- */

@Preview(
    name = "Timeout - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewTimeoutLight() {
    AppTheme(darkTheme = false) {
        TimeoutScreen(
            onBreakCompleted = { },
            remainingTime = 120 // 2 minutos para el preview
        )
    }
}

@Preview(
    name = "Timeout - 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewTimeoutDark() {
    AppTheme(darkTheme = true) {
        TimeoutScreen(
            onBreakCompleted = { },
            remainingTime = 45 // 45 segundos para el preview
        )
    }
}

// Preview con diferentes tiempos para testing
@Preview(
    name = "Timeout - 5 Minutos",
    showBackground = true
)
@Composable
private fun PreviewTimeout5Minutes() {
    AppTheme {
        TimeoutScreen(
            onBreakCompleted = { },
            remainingTime = 300 // 5 minutos
        )
    }
}

@Preview(
    name = "Timeout - 30 Segundos",
    showBackground = true
)
@Composable
private fun PreviewTimeout30Seconds() {
    AppTheme {
        TimeoutScreen(
            onBreakCompleted = { },
            remainingTime = 30 // 30 segundos
        )
    }
}

@Preview(
    name = "Timeout - Tiempo Casi Terminado",
    showBackground = true
)
@Composable
private fun PreviewTimeoutAlmostDone() {
    AppTheme {
        TimeoutScreen(
            onBreakCompleted = { },
            remainingTime = 5 // 5 segundos restantes
        )
    }
}
