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

@Composable
fun HelloPreviewText() {
    Text(text = stringResource(id = R.string.hello_preview))
}

@Preview(showBackground = true, name = "Hello Preview")
@Composable
private fun HelloPreview() {
    Surface {
        HelloPreviewText()
    }
}
