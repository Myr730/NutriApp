// BAMXPuebla/app/src/main/java/org/bamx/puebla/MainActivity.kt
package org.bamx.puebla

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import android.os.Bundle
import org.bamx.puebla.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HelloPreviewText()
                }
            }
        }
    }
}

@Composable
fun HelloPreviewText() {
    Text(
        text = stringResource(id = R.string.hello_preview),
        style = MaterialTheme.typography.headlineLarge
    )
}

@Preview(
    showBackground = true,
    name = "Material3 Preview - Light",
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun HelloPreviewLight() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HelloPreviewText()
        }
    }
}

@Preview(
    showBackground = true,
    name = "Material3 Preview - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun HelloPreviewDark() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HelloPreviewText()
        }
    }
}
