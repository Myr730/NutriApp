package org.bamx.puebla.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun GameStatChip(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    AssistChip(
        onClick = {}, // sin lógica
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        label = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = label, style = MaterialTheme.typography.labelLarge)
                Text(text = value, style = MaterialTheme.typography.labelLarge)
            }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
