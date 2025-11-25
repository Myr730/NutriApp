package org.bamx.puebla.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

/**
 * Encabezado semántico para títulos de sección/pantalla.
 * Usa Typography del tema; marca semantics.heading() para a11y.
 */
@Composable
fun Headline(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
            .semantics { heading() }
            .padding(bottom = Dimens.space8)
    )
}

@Preview(name = "Headline - Light", showBackground = true)
@Composable
private fun PreviewHeadlineLight() {
    AppTheme(darkTheme = false) {
        Headline(text = "Título de ejemplo")
    }
}

@Preview(name = "Headline - Dark", showBackground = true)
@Composable
private fun PreviewHeadlineDark() {
    AppTheme(darkTheme = true) {
        Headline(text = "Título de ejemplo")
    }
}
