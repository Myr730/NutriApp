package org.bamx.puebla.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bamx.puebla.R
import org.bamx.puebla.ui.components.AppScaffold
import org.bamx.puebla.ui.components.PrimaryButton
import org.bamx.puebla.ui.components.SecondaryButton
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens

@Composable
fun HomeScreen() {
    // Si ya subiste el fondo, cambia null -> R.drawable.bg_home
    AppScaffold(
        backgroundResId = R.drawable.bg_home,
        darkenBackground = 0.12f
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header con logo (opcional) + títulos
            HeaderHome(
                // Si ya subiste el logo, cambia null -> R.drawable.logo_bamx
                logoRes = R.drawable.logo_bamx
            )

            // Área ilustrativa central (opcional)
            IllustrationHome(
                // Si ya subiste la ilustración, cambia null -> R.drawable.home_illustration
                illustrationRes = R.drawable.home_illustration
            )

            // Botones de acción (sin navegación real)
            ActionsHome()
        }
    }
}

@Composable
private fun HeaderHome(logoRes: Int?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (logoRes != null) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = stringResource(id = R.string.cd_logo_bamx),
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(Dimens.space8))
        }

        // Título como encabezado semántico
        Text(
            text = stringResource(id = R.string.home_header_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.semantics { heading() }
        )

        Spacer(Modifier.height(Dimens.space8))

        Text(
            text = stringResource(id = R.string.home_subtitle),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun IllustrationHome(illustrationRes: Int?) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (illustrationRes != null) {
            Image(
                painter = painterResource(id = illustrationRes),
                contentDescription = stringResource(id = R.string.cd_home_illustration),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        } else {
            // Placeholder visible en previews si aún no hay asset
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 240.dp, height = 160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ilustración",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionsHome() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(
            text = stringResource(id = R.string.home_btn_start),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(Dimens.space12))
        SecondaryButton(
            text = stringResource(id = R.string.home_btn_parents),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Home - 411x891 Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewHomeLight() {
    AppTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Preview(
    name = "Home - 360x640 Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewHomeDarkSmall() {
    AppTheme(darkTheme = true) {
        HomeScreen()
    }
}
