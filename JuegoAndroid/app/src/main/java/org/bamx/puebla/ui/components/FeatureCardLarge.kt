package org.bamx.puebla.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.bamx.puebla.ui.theme.Dimens

/**
 * Tarjeta grande tipo "pill" (solo UI, sin onClick).
 * Preferente usar imagen @DrawableRes para el ícono; si no hay, usa un ImageVector fallback.
 */
@Composable
fun FeatureCardLarge(
    title: String,
    background: Color,
    titleColor: Color = Color.White,
    modifier: Modifier = Modifier,
    @DrawableRes leadingResId: Int? = null,
    leadingIcon: ImageVector? = null,
    leadingContentDescription: String? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.parentsCardHeight)
            .semantics { role = Role.Button }, // semántica, sin interacción real
        shape = RoundedCornerShape(Dimens.parentsCardRadius),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading
            if (leadingResId != null) {
                Image(
                    painter = painterResource(id = leadingResId),
                    contentDescription = leadingContentDescription,
                    modifier = Modifier.size(Dimens.parentsIconSize),
                    contentScale = ContentScale.Fit
                )
            } else if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = leadingContentDescription,
                    modifier = Modifier.size(Dimens.parentsIconSize),
                    tint = Color.White
                )
            } else {
                // Fallback mínimo
                Icon(
                    imageVector = Icons.Filled.InsertChart,
                    contentDescription = leadingContentDescription,
                    modifier = Modifier.size(Dimens.parentsIconSize),
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(16.dp))

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = titleColor,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
