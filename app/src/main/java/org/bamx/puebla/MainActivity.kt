// BAMXPuebla/app/src/main/java/org/bamx/puebla/MainActivity.kt
package org.bamx.puebla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                HelloPreviewText()
            }
        }
    }
}

// BAMXPuebla/app/src/main/java/org/bamx/puebla/MainActivity.kt
@Composable
fun HelloPreviewText() {
    // Usamos Material3 desde el BOM
    androidx.compose.material3.Text(text = stringResource(id = R.string.hello_preview))
}

@Preview(showBackground = true, name = "Material3 Preview - Light")
@Composable
private fun HelloPreviewLight() {
    androidx.compose.material3.Surface {
        HelloPreviewText()
    }
}

@Preview(
    showBackground = true,
    name = "Material3 Preview - Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HelloPreviewDark() {
    androidx.compose.material3.Surface {
        HelloPreviewText()
    }
}
