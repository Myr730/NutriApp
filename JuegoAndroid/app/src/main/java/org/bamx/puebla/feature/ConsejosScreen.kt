package org.bamx.puebla.feature

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.parents_bg_bottom
import org.bamx.puebla.ui.theme.parents_bg_top

@Composable
fun ConsejosScreen(
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(parents_bg_top, parents_bg_bottom)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Espacio para el botón de regresar
            Spacer(Modifier.height(60.dp))

            // Título principal
            Text(
                text = "Consejos de Nutrición",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B16),
                    fontSize = 28.sp
                )
            )

            Spacer(Modifier.height(8.dp))

            // SECCIÓN 1: MÉTODO DE PORCIONES CON LA MANO
            NutritionSection(
                title = "Método de Porciones con la Mano",
                imageRes = R.drawable.mano, // Asegúrate de tener esta imagen
                content = {
                    Text(
                        text = "Una forma fácil de medir porciones sin balanzas:",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF3A3A3A)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    PortionItem(
                        item = "Proteínas",
                        description = "Palma de la mano (sin dedos)",
                        example = "Pollo, pescado, carne"
                    )

                    PortionItem(
                        item = "Carbohidratos",
                        description = "Puño cerrado",
                        example = "Arroz, pasta, papa"
                    )

                    PortionItem(
                        item = "Verduras",
                        description = "Ambas manos abiertas",
                        example = "Ensaladas, vegetales cocidos"
                    )

                    PortionItem(
                        item = "Grasas",
                        description = "Punta del dedo pulgar",
                        example = "Aceite, mantequilla, nueces"
                    )

                    PortionItem(
                        item = "Frutas",
                        description = "Mano en forma de cuenco",
                        example = "Manzana, naranja, berries"
                    )
                }
            )

            Spacer(Modifier.height(24.dp))

            // SECCIÓN 2: HIDRATACIÓN
            NutritionSection(
                title = "Hidratación Saludable",
                imageRes = R.drawable.agua, // Asegúrate de tener esta imagen
                content = {
                    Text(
                        text = "Mantener una buena hidratación es esencial para la salud:",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF3A3A3A)
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    HydrationTip(
                        title = "¿Cuánta agua tomar?",
                        description = "6-8 vasos al día (aproximadamente 2 litros)"
                    )

                    HydrationTip(
                        title = "Señales de deshidratación",
                        description = "Boca seca, dolor de cabeza, cansancio, orina oscura"
                    )

                    HydrationTip(
                        title = "Mejores momentos para tomar agua",
                        description = "Al despertar, antes de comer, durante el ejercicio"
                    )

                    // Jarra del buen beber
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE3F2FD)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Jarra del Buen Beber",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2)
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            BeverageLevel(
                                level = "Nivel 1",
                                beverage = "Agua natural",
                                portions = "6-8 vasos",
                                color = Color(0xFF64B5F6)
                            )

                            BeverageLevel(
                                level = "Nivel 2",
                                beverage = "Leche semidescremada",
                                portions = "0-2 vasos",
                                color = Color(0xFF90CAF9)
                            )

                            BeverageLevel(
                                level = "Nivel 3",
                                beverage = "Café y té sin azúcar",
                                portions = "0-4 tazas",
                                color = Color(0xFFBBDEFB)
                            )

                            BeverageLevel(
                                level = "EVITAR",
                                beverage = "Refrescos y jugos azucarados",
                                portions = "0 vasos",
                                color = Color(0xFFFFCDD2)
                            )
                        }
                    }
                }
            )

            Spacer(Modifier.height(24.dp))

            // SECCIÓN 3: RECETAS SALUDABLES
            NutritionSection(
                title = "Recetas Fáciles y Saludables",
                imageRes = R.drawable.recetas, // Puedes usar una imagen genérica de comida
                content = {
                    Text(
                        text = "Recetas rápidas para toda la familia:",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF3A3A3A)
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // RECETA 1
                    RecipeCard(
                        title = "Ensalada de Pollo y Aguacate",
                        time = "15 min",
                        ingredients = listOf(
                            "1 pechuga de pollo cocida",
                            "1 aguacate maduro",
                            "1 taza de lechuga",
                            "1 jitomate",
                            "1/4 de cebolla morada",
                            "Jugo de limón",
                            "Sal y pimienta"
                        ),
                        steps = listOf(
                            "Deshebrar el pollo cocido",
                            "Picar el aguacate, jitomate y cebolla",
                            "Mezclar todos los ingredientes",
                            "Agregar jugo de limón al gusto",
                            "Sazonar con sal y pimienta"
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    // RECETA 2
                    RecipeCard(
                        title = "Smoothie de Frutas",
                        time = "5 min",
                        ingredients = listOf(
                            "1 plátano",
                            "1/2 taza de fresas",
                            "1/2 taza de yogurt natural",
                            "1/2 taza de leche",
                            "1 cucharada de miel (opcional)",
                            "Hielo al gusto"
                        ),
                        steps = listOf(
                            "Pelar y picar el plátano",
                            "Lavar las fresas",
                            "Agregar todos los ingredientes a la licuadora",
                            "Licuar hasta obtener consistencia suave",
                            "Servir inmediatamente"
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    // RECETA 3
                    RecipeCard(
                        title = "Verduras Salteadas",
                        time = "10 min",
                        ingredients = listOf(
                            "1 zanahoria",
                            "1 calabacita",
                            "1/2 pimiento morrón",
                            "1 cucharadita de aceite de oliva",
                            "1 diente de ajo",
                            "Sal y hierbas al gusto"
                        ),
                        steps = listOf(
                            "Picar todas las verduras en tiras",
                            "Calentar el aceite en un sartén",
                            "Saltear el ajo por 30 segundos",
                            "Agregar las verduras y cocinar 5-7 min",
                            "Sazonar con sal y hierbas"
                        )
                    )
                }
            )

            Spacer(Modifier.height(40.dp))
        }

        // Botón de regresar
        Image(
            painter = painterResource(id = R.drawable.ic_back2),
            contentDescription = stringResource(id = R.string.cd_back),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp)
                .size(52.dp)
                .clickable { onBackClick() },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun NutritionSection(
    title: String,
    imageRes: Int,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Título de la sección
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFF8A3D)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Imagen centrada
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Ilustración de $title",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            // Contenido de la sección
            content()
        }
    }
}

@Composable
private fun PortionItem(
    item: String,
    description: String,
    example: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF666666)
                )
            )
        }
        Text(
            text = example,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF9575CD),
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun HydrationTip(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de gota de agua
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF4CAF50), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💧",
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF3A3A3A)
                    )
                )
            }
        }
    }
}

@Composable
private fun BeverageLevel(
    level: String,
    beverage: String,
    portions: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indicador de color
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = beverage,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF3A3A3A)
                )
            )
        }

        Text(
            text = portions,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF666666)
            )
        )
    }
}

@Composable
private fun RecipeCard(
    title: String,
    time: String,
    ingredients: List<String>,
    steps: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Encabezado de la receta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF57C00)
                    )
                )
                Text(
                    text = "⏱️ $time",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666)
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            // Ingredientes
            Text(
                text = "Ingredientes:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A3A3A)
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            ingredients.forEach { ingredient ->
                Text(
                    text = "• $ingredient",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666)
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Pasos
            Text(
                text = "Preparación:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A3A3A)
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            steps.forEachIndexed { index, step ->
                Text(
                    text = "${index + 1}. $step",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666)
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

/* ---------- PREVIEWS ---------- */

@Preview(
    name = "Consejos - Light",
    showBackground = true,
    widthDp = 411,
    heightDp = 891
)
@Composable
private fun PreviewConsejosLight() {
    AppTheme(darkTheme = false) {
        ConsejosScreen(
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Consejos - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun PreviewConsejosDark() {
    AppTheme(darkTheme = true) {
        ConsejosScreen(
            onBackClick = {}
        )
    }
}
