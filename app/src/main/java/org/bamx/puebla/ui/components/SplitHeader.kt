package org.bamx.puebla.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun SplitHeader( // mantenemos el nombre para no romper imports
    title: String,
    backgroundColor: Color,
    height: Dp = 96.dp,
    leadingIcon: ImageVector? = Icons.Filled.ArrowBack,
    leadingContentDescription: String? = null
) {
    val isDark = isSystemInDarkTheme()
    val tint = if (isDark) Color.Black else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(backgroundColor)
    ) {
        // Flecha a la izquierda
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingContentDescription,
                tint = tint,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        }
        // Título centrado
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = tint,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .semantics { heading() }
        )
    }
}
