package org.bamx.puebla.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.screenPadding)
    ) {
        Text(text = stringResource(id = R.string.home_title))
        Spacer(Modifier.height(Dimens.space16))
        Text(text = "Placeholder de layout (header/logo, botones, ilustración).")
    }
}

@Preview(
    name = "Home - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewHomeLight() {
    AppTheme(darkTheme = false) { HomeScreen() }
}

@Preview(
    name = "Home - 360x640 Dark",
    showBackground = true,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewHomeDarkSmall() {
    AppTheme(darkTheme = true) { HomeScreen() }
}
