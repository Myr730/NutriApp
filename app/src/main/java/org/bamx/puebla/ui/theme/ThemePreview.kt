package org.bamx.puebla.ui.theme

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration

@Preview(name = "Theme Light", showBackground = true)
@Composable
private fun ThemePreviewLight() {
    AppTheme(darkTheme = false) {
        Surface { Text("Vista previa tema claro") }
    }
}

@Preview(
    name = "Theme Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ThemePreviewDark() {
    AppTheme(darkTheme = true) {
        Surface { Text("Vista previa tema oscuro") }
    }
}
