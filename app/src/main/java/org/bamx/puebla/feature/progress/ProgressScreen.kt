package org.bamx.puebla.feature.progress

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bamx.puebla.R
import org.bamx.puebla.ui.theme.AppTheme
import org.bamx.puebla.ui.theme.Dimens
import org.bamx.puebla.ui.theme.header_cream
import org.bamx.puebla.ui.theme.parents_bg_bottom
import org.bamx.puebla.ui.theme.parents_bg_top
import java.util.*
import androidx.compose.foundation.layout.WindowInsets

@Composable
fun ProgressScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Inicialización segura fuera del composable
    val repository = remember {
        runCatching {
            org.bamx.puebla.AppDatabase.getRepository(context)
        }.getOrNull().also { repo ->
            if (repo == null) {
                Log.e("ProgressScreen", "No se pudo inicializar la base de datos")
            }
        }
    }

    val viewModel = remember { ProgressViewModel(repository) }
    val weightProgressList by viewModel.weightProgressList.collectAsState(initial = emptyList())

    ProgressScreenContent(
        onBackClick = onBackClick,
        weightProgressList = weightProgressList,
        onAddWeightClick = { name, weight, date ->
            viewModel.addWeightProgress(name, weight, date)
        },
        hasDatabaseError = repository == null
    )
}

// ViewModel corregido - Sin GlobalScope
class ProgressViewModel(private val repository: org.bamx.puebla.WeightProgressRepository?) {

    val weightProgressList = repository?.getAllWeightProgress() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    fun addWeightProgress(name: String, weight: Double, date: Date = Date()) {
        if (repository == null) {
            Log.e("ProgressViewModel", "Repository es null, no se puede guardar")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.addWeightProgress(name, weight, date)
                Log.d("ProgressViewModel", "Peso guardado exitosamente: $name, $weight")
            } catch (e: Exception) {
                Log.e("ProgressViewModel", "Error guardando peso: ${e.message}")
            }
        }
    }

    fun updateWeightProgress(weightProgress: org.bamx.puebla.WeightProgress) {
        if (repository == null) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.updateWeightProgress(weightProgress)
            } catch (e: Exception) {
                Log.e("ProgressViewModel", "Error actualizando peso: ${e.message}")
            }
        }
    }

    fun deleteWeightProgress(weightProgress: org.bamx.puebla.WeightProgress) {
        if (repository == null) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.deleteWeightProgress(weightProgress)
            } catch (e: Exception) {
                Log.e("ProgressViewModel", "Error eliminando peso: ${e.message}")
            }
        }
    }
}

// Diálogo actualizado para incluir nombre y fecha
@Composable
fun AddWeightDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Date) -> Unit
) {
    var nameInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Registrar Progreso")
        },
        text = {
            Column {
                // Campo para nombre
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo para peso
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { newValue ->
                        // Validación mejorada
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            weightInput = newValue
                        }
                    },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Información de fecha
                Text(
                    text = "Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Nota: La fecha actual se registrará automáticamente",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val weight = weightInput.toDoubleOrNull()
                    if (nameInput.isNotEmpty() && weight != null && weight > 0) {
                        onConfirm(nameInput, weight, selectedDate)
                        onDismiss()
                    }
                },
                enabled = nameInput.isNotEmpty() && weightInput.toDoubleOrNull() != null
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ProgressScreenContent(
    onBackClick: () -> Unit = {},
    weightProgressList: List<org.bamx.puebla.WeightProgress> = emptyList(),
    onAddWeightClick: (String, Double, Date) -> Unit = { _, _, _ -> },
    hasDatabaseError: Boolean = false
) {
    var showAddWeightDialog by remember { mutableStateOf(false) }
    val screenW = LocalConfiguration.current.screenWidthDp
    val isSmall = screenW <= 360

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(parents_bg_top, parents_bg_bottom)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.screenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(60.dp))

            Text(
                text = "Mi Progreso",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B16),
                    fontSize = 32.sp
                )
            )

            Spacer(Modifier.height(18.dp))

            // Mostrar error de base de datos si existe
            if (hasDatabaseError) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDED))
                ) {
                    Text(
                        text = "Error: No se pudo cargar la base de datos",
                        color = Color(0xFFD32F2F),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // Tarjeta HISTORIAL COMPLETO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Registros de Progreso",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color(0xFFFF8A3D),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    // Encabezado de tabla con TRES columnas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Nombre",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF2F2F2F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = "Fecha",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF2F2F2F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.weight(1.5f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Peso (kg)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF2F2F2F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    if (weightProgressList.isEmpty()) {
                        Text(
                            text = if (hasDatabaseError) {
                                "Base de datos no disponible"
                            } else {
                                "No hay registros de progreso\nToca el botón + para agregar uno"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF666666)
                            )
                        )
                    } else {
                        weightProgressList.forEach { weightProgress ->
                            val dateFormatted = try {
                                org.bamx.puebla.DatabaseHelper.formatDate(weightProgress.date)
                            } catch (e: Exception) {
                                "Fecha inválida"
                            }
                            val weightFormatted = try {
                                org.bamx.puebla.DatabaseHelper.formatWeight(weightProgress.weight)
                            } catch (e: Exception) {
                                "0.0"
                            }
                            val bgColor = try {
                                Color(org.bamx.puebla.DatabaseHelper.getBackgroundColorForWeight(weightProgress, weightProgressList))
                            } catch (e: Exception) {
                                Color(0xFFE8F7EA)
                            }

                            ProgressRow(
                                name = weightProgress.notes.takeIf { it.isNotEmpty() } ?: "Sin nombre",
                                date = dateFormatted,
                                weight = weightFormatted,
                                bg = bgColor
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(110.dp))
        }

        // Botón de regresar - simplificado
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

        // FAB para agregar registro
        FloatingActionButton(
            onClick = {
                if (!hasDatabaseError) {
                    showAddWeightDialog = true
                }
            },
            shape = RoundedCornerShape(30.dp),
            containerColor = if (hasDatabaseError) Color.Gray else Color(0xFFFF8A3D),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = Dimens.screenPadding, bottom = Dimens.screenPadding + 10.dp)
                .size(64.dp)
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp
                )
            )
        }

        // Diálogo
        if (showAddWeightDialog) {
            AddWeightDialog(
                onDismiss = { showAddWeightDialog = false },
                onConfirm = onAddWeightClick
            )
        }
    }
}

@Composable
private fun ProgressRow(
    name: String,
    date: String,
    weight: String,
    bg: Color,
) {
    Surface(
        color = bg,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF3A3A3A),
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(2f),
                maxLines = 1
            )

            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF3A3A3A),
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.Center
            )

            Text(
                text = weight,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF3A3A3A),
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(
    name = "Progreso - Light",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
private fun PreviewProgressLight() {
    AppTheme(darkTheme = false) {
        ProgressScreenContent(
            onBackClick = { }
        )
    }
}

@Preview(
    name = "Progreso - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun PreviewProgressDark() {
    AppTheme(darkTheme = true) {
        ProgressScreenContent(
            onBackClick = { }
        )
    }
}
