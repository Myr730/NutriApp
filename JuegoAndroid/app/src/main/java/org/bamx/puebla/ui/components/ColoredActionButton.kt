package org.bamx.puebla.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun ColoredActionButton(
    text: String,
    container: Color,
    content: Color = Color.White,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {}, // sin lógica
        shape = MaterialTheme.shapes.large, // lo afinamos con radius abajo
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.5f),
            disabledContentColor = content.copy(alpha = 0.8f)
        ),
        modifier = modifier
            .defaultMinSize(minHeight = Dimens.homeButtonHeight)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium, // Fredoka
            textAlign = TextAlign.Center
        )
    }
}
