package org.bamx.puebla.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

/**
 * Botón primario sin lógica: onClick vacío.
 * - Garantiza altura mínima de 48dp (tamaño táctil).
 * - Alineado a typography.labelLarge del tema.
 */
@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null
) {
    Button(
        onClick = {}, // placeholder sin lógica
        enabled = enabled,
        modifier = modifier.defaultMinSize(minHeight = Dimens.touchTarget),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        )
    ) {
        Row {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null // decorativo en el botón
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

@Preview(name = "PrimaryButton - Light", showBackground = true)
@Composable
private fun PreviewPrimaryButtonLight() {
    AppTheme(darkTheme = false) {
        PrimaryButton(text = "Continuar")
    }
}

@Preview(name = "PrimaryButton - Dark", showBackground = true)
@Composable
private fun PreviewPrimaryButtonDark() {
    AppTheme(darkTheme = true) {
        PrimaryButton(text = "Continuar", enabled = false)
    }
}
