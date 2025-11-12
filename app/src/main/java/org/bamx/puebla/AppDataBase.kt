package org.bamx.puebla

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow
import java.util.*
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 1. Entity (Tabla de la base de datos)
@Entity(tableName = "weight_progress")
data class WeightProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weight: Double,
    val date: Date,
    val notes: String = ""
) {
    constructor(weight: Double, date: Date, notes: String = "") : this(0, weight, date, notes)
}

// 2. Converters para las fechas
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

// 3. DAO (Data Access Object)
@Dao
interface WeightProgressDao {

    @Query("SELECT * FROM weight_progress ORDER BY date DESC")
    fun getAllWeightProgress(): Flow<List<WeightProgress>>

    @Query("SELECT * FROM weight_progress WHERE date = :date")
    suspend fun getWeightProgressByDate(date: Date): WeightProgress?

    @Insert
    suspend fun insertWeightProgress(weightProgress: WeightProgress)

    @Update
    suspend fun updateWeightProgress(weightProgress: WeightProgress)

    @Delete
    suspend fun deleteWeightProgress(weightProgress: WeightProgress)

    @Query("DELETE FROM weight_progress WHERE id = :id")
    suspend fun deleteWeightProgressById(id: Long)

    @Query("SELECT * FROM weight_progress WHERE id = :id")
    suspend fun getWeightProgressById(id: Long): WeightProgress?
}

// 4. Repository
class WeightProgressRepository(private val dao: WeightProgressDao) {

    fun getAllWeightProgress(): Flow<List<WeightProgress>> {
        return dao.getAllWeightProgress()
    }

    suspend fun addWeightProgress(name: String, weight: Double, date: Date = Date()) {
        val weightProgress = WeightProgress(
            weight = weight,
            date = date,
            notes = name // Usamos el campo notes para almacenar el nombre
        )
        dao.insertWeightProgress(weightProgress)
    }

    suspend fun updateWeightProgress(weightProgress: WeightProgress) {
        dao.updateWeightProgress(weightProgress)
    }

    suspend fun deleteWeightProgress(weightProgress: WeightProgress) {
        dao.deleteWeightProgress(weightProgress)
    }

    suspend fun getWeightProgressById(id: Long): WeightProgress? {
        return dao.getWeightProgressById(id)
    }
}

// 5. Database
// En AppDatabase.kt - Versión simplificada y segura
@Database(
    entities = [WeightProgress::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightProgressDao(): WeightProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun getRepository(context: Context): WeightProgressRepository {
            return WeightProgressRepository(getInstance(context).weightProgressDao())
        }
    }
}

// 7. Helper functions para usar en tu ProgressScreen
object DatabaseHelper {

    // Función para determinar el color de fondo basado en la tendencia del peso
    fun getBackgroundColorForWeight(
        currentWeight: WeightProgress,
        allWeights: List<WeightProgress>
    ): Int {
        val sortedWeights = allWeights.sortedBy { it.date }
        val currentIndex = sortedWeights.indexOfFirst { it.id == currentWeight.id }

        if (currentIndex > 0) {
            val previousWeight = sortedWeights[currentIndex - 1]
            return if (currentWeight.weight <= previousWeight.weight) {
                0xFFE8F7EA.toInt() // Verde - peso igual o menor (bueno)
            } else {
                0xFFFFEDED.toInt() // Rojo - peso mayor (alerta)
            }
        }

        return 0xFFE8F7EA.toInt() // Primer registro
    }

    // Formatear fecha para mostrar
    fun formatDate(date: Date): String {
        return java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }

    // Formatear peso para mostrar
    fun formatWeight(weight: Double): String {
        return String.format("%.1f", weight)
    }

    // Validar peso
    fun isValidWeight(weight: String): Boolean {
        return weight.toDoubleOrNull() != null && weight.toDouble() > 0
    }
}
