package org.bamx.puebla.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

/**
 * Tarjeta de sección reutilizable sin interacción.
 * - Encabezado con Headline().
 * - Contenido libre debajo.
 */
@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(Dimens.space16)) {
            Headline(text = title)
            Spacer(Modifier.height(Dimens.space8))
            content()
        }
    }
}

@Preview(name = "SectionCard - Light", showBackground = true)
@Composable
private fun PreviewSectionCardLight() {
    AppTheme(darkTheme = false) {
        SectionCard(title = "Título de sección") {
            Text("Contenido de ejemplo.")
        }
    }
}

@Preview(name = "SectionCard - Dark", showBackground = true)
@Composable
private fun PreviewSectionCardDark() {
    AppTheme(darkTheme = true) {
        SectionCard(title = "Título de sección") {
            Text("Contenido de ejemplo.")
        }
    }
}
