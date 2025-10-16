package org.bamx.puebla.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun AppScaffold(
    @DrawableRes backgroundResId: Int? = null,
    darkenBackground: Float = 0f,
    topBar: (@Composable () -> Unit)? = null,
    padTopBar: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(Dimens.screenPadding),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundResId != null) {
            BackgroundImage(
                resId = backgroundResId,
                darken = darkenBackground,
                contentDescription = null
            )
        }
        Column(modifier = Modifier.fillMaxSize()) {
            if (topBar != null) {
                if (padTopBar) {
                    Box(
                        modifier = Modifier
                            .padding(Dimens.screenPadding)
                    ) { topBar() }
                } else {
                    topBar()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                content = content
            )
        }
    }
}
