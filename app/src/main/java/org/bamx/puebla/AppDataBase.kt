package org.bamx.puebla

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.*


// 1. Entity para progreso de peso
@Entity(tableName = "weight_progress")
data class WeightProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weight: Double,
    val date: Date,
    val notes: String = ""
)

// 2. Entity para configuraciones de tiempo
@Entity(tableName = "time_settings")
data class TimeSettings(
    @PrimaryKey // CORRECCIÓN: Agregar PrimaryKey
    val id: Int = 1, // CORRECCIÓN: Agregar campo id
    val gameTimeMinutes: Int = 0,
    val breakTimeSeconds: Int = 0,
    val lastGameStartTime: Date? = null
)

// 3. Converters para las fechas
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

// 4. DAO para WeightProgress
@Dao
interface WeightProgressDao {
    @Query("SELECT * FROM weight_progress ORDER BY date DESC")
    fun getAllWeightProgress(): Flow<List<WeightProgress>>

    @Insert
    suspend fun insertWeightProgress(weightProgress: WeightProgress): Long

    @Update
    suspend fun updateWeightProgress(weightProgress: WeightProgress)

    @Delete
    suspend fun deleteWeightProgress(weightProgress: WeightProgress)
}

// 5. DAO para TimeSettings - CORREGIDO
@Dao
interface TimeSettingsDao {
    @Query("SELECT * FROM time_settings WHERE id = 1")
    fun getTimeSettings(): Flow<TimeSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSettings(timeSettings: TimeSettings)

    @Update
    suspend fun updateTimeSettings(timeSettings: TimeSettings)

    // CORRECCIÓN: Cambiar el parámetro a nullable
    @Query("UPDATE time_settings SET lastGameStartTime = :startTime WHERE id = 1")
    suspend fun updateLastGameStartTime(startTime: Date?) // ✅ Cambiado a Date?
}

// 6. Repository para WeightProgress
class WeightProgressRepository(private val dao: WeightProgressDao) {
    fun getAllWeightProgress(): Flow<List<WeightProgress>> {
        return dao.getAllWeightProgress()
    }

    suspend fun addWeightProgress(name: String, weight: Double, date: Date = Date()): Boolean {
        return try {
            val weightProgress = WeightProgress(
                weight = weight,
                date = date,
                notes = name
            )
            dao.insertWeightProgress(weightProgress)
            true
        } catch (e: Exception) {
            Log.e("WeightProgressRepository", "Error al agregar progreso: ${e.message}")
            false
        }
    }

    suspend fun updateWeightProgress(weightProgress: WeightProgress): Boolean {
        return try {
            dao.updateWeightProgress(weightProgress)
            true
        } catch (e: Exception) {
            Log.e("WeightProgressRepository", "Error al actualizar progreso: ${e.message}")
            false
        }
    }

    suspend fun deleteWeightProgress(weightProgress: WeightProgress): Boolean {
        return try {
            dao.deleteWeightProgress(weightProgress)
            true
        } catch (e: Exception) {
            Log.e("WeightProgressRepository", "Error al eliminar progreso: ${e.message}")
            false
        }
    }
}

// 7. Repository para TimeSettings - CORREGIDO
class TimeSettingsRepository(private val dao: TimeSettingsDao) {
    fun getTimeSettings(): Flow<TimeSettings?> {
        return dao.getTimeSettings()
    }

    suspend fun saveTimeSettings(gameTimeMinutes: Int, breakTimeSeconds: Int): Boolean {
        return try {
            val timeSettings = TimeSettings(
                id = 1,
                gameTimeMinutes = gameTimeMinutes,
                breakTimeSeconds = breakTimeSeconds
            )
            dao.insertTimeSettings(timeSettings)
            true
        } catch (e: Exception) {
            Log.e("TimeSettingsRepository", "Error guardando configuraciones: ${e.message}")
            false
        }
    }

    // CORRECCIÓN: Cambiar el parámetro a nullable
    suspend fun updateLastGameStartTime(startTime: Date?): Boolean { // ✅ Cambiado a Date?
        return try {
            dao.updateLastGameStartTime(startTime)
            true
        } catch (e: Exception) {
            Log.e("TimeSettingsRepository", "Error actualizando tiempo de inicio: ${e.message}")
            false
        }
    }

    suspend fun getCurrentTimeSettings(): TimeSettings? {
        return try {
            dao.getTimeSettings().firstOrNull()
        } catch (e: Exception) {
            Log.e("TimeSettingsRepository", "Error obteniendo configuraciones: ${e.message}")
            null
        }
    }
}

// 8. Database principal - VERSIÓN CORREGIDA
@Database(
    entities = [WeightProgress::class, TimeSettings::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightProgressDao(): WeightProgressDao
    abstract fun timeSettingsDao(): TimeSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            return try {
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error al crear la base de datos: ${e.message}")
                null
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database.db"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Insertar configuraciones por defecto usando SQL directo
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                // Usar la instancia recién creada, no getInstance()
                                INSTANCE?.timeSettingsDao()?.insertTimeSettings(
                                    TimeSettings(
                                        id = 1,
                                        gameTimeMinutes = 15,
                                        breakTimeSeconds = 30
                                    )
                                )
                                Log.d("AppDatabase", "✅ Configuraciones por defecto insertadas")
                            } catch (e: Exception) {
                                Log.e("AppDatabase", "❌ Error insertando configuraciones: ${e.message}")
                            }
                        }
                    }
                })
                .build()
        }

        // Los demás métodos permanecen igual...
        fun getRepository(context: Context): WeightProgressRepository? {
            return try {
                val database = getInstance(context)
                if (database != null) {
                    WeightProgressRepository(database.weightProgressDao())
                } else {
                    Log.e("AppDatabase", "No se pudo obtener la instancia de la base de datos")
                    null
                }
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error obteniendo repository: ${e.message}")
                null
            }
        }

        fun getTimeSettingsRepository(context: Context): TimeSettingsRepository? {
            return try {
                val database = getInstance(context)
                if (database != null) {
                    TimeSettingsRepository(database.timeSettingsDao())
                } else {
                    Log.e("AppDatabase", "No se pudo obtener la instancia de la base de datos")
                    null
                }
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error obteniendo time settings repository: ${e.message}")
                null
            }
        }
    }
}

// 9. Helper functions
object DatabaseHelper {
    fun getBackgroundColorForWeight(
        currentWeight: WeightProgress,
        allWeights: List<WeightProgress>
    ): Int {
        if (allWeights.size < 2) return 0xFFE8F7EA.toInt()

        val sortedWeights = allWeights.sortedBy { it.date }
        val currentIndex = sortedWeights.indexOfFirst { it.id == currentWeight.id }

        if (currentIndex > 0) {
            val previousWeight = sortedWeights[currentIndex - 1]
            return if (currentWeight.weight <= previousWeight.weight) {
                0xFFE8F7EA.toInt() // Verde
            } else {
                0xFFFFEDED.toInt() // Rojo
            }
        }
        return 0xFFE8F7EA.toInt()
    }

    fun formatDate(date: Date): String {
        return try {
            java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            "Fecha inválida"
        }
    }

    fun formatWeight(weight: Double): String {
        return String.format("%.1f", weight)
    }

    fun isValidWeight(weight: String): Boolean {
        return weight.toDoubleOrNull() != null && weight.toDouble() > 0
    }
}
