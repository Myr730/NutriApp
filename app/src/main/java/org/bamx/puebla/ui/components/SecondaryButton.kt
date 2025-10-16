package org.bamx.puebla.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = {}, // sin lógica
        enabled = enabled,
        modifier = modifier.defaultMinSize(minHeight = Dimens.touchTarget),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        // Si tu versión de Material3 no tiene outlinedButtonBorder, ver "Fallback" abajo.
        border = ButtonDefaults.outlinedButtonBorder
    ) {
        Row {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null // decorativo
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = if (leadingIcon != null) 8.dp else 0.dp)
            )
        }
    }
}

@Preview(name = "SecondaryButton - Light", showBackground = true)
@Composable
private fun PreviewSecondaryButtonLight() {
    AppTheme(darkTheme = false) {
        SecondaryButton(text = "Más info")
    }
}

@Preview(name = "SecondaryButton - Dark", showBackground = true)
@Composable
private fun PreviewSecondaryButtonDark() {
    AppTheme(darkTheme = true) {
        SecondaryButton(text = "Más info", enabled = false)
    }
}
