package org.bamx.puebla.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.bamx.puebla.ui.theme.Dimens
import org.bamx.puebla.ui.theme.brand_green_button
import org.bamx.puebla.ui.theme.brand_orange_button
import org.bamx.puebla.ui.theme.brand_purple_button

@Composable
fun PlayButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(Dimens.homeButtonRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = brand_orange_button,
            contentColor = Color.White
        ),
        modifier = modifier.width(Dimens.homeButtonWidth)
            .defaultMinSize(minHeight = Dimens.homeButtonHeight)
    ) {
        Text(text = "JUGAR", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
    }
}

@Composable
fun ParentsButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(Dimens.homeButtonRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = brand_green_button,
            contentColor = Color.White
        ),
        modifier = modifier.width(Dimens.homeButtonWidth)
            .defaultMinSize(minHeight = Dimens.homeButtonHeight)
    ) {
        Text(text = "PADRES", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
    }
}

@Composable
fun SettingsButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(Dimens.homeButtonRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = brand_purple_button,
            contentColor = Color.White
        ),
        modifier = modifier.width(Dimens.homeButtonWidth)
            .defaultMinSize(minHeight = Dimens.homeButtonHeight)
    ) {
        Text(text = "AJUSTES", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
    }
}
