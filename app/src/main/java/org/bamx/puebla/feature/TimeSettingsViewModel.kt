package org.bamx.puebla.feature

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.bamx.puebla.AppDatabase
import java.util.*

class TimeSettingsViewModel(context: Context) : ViewModel() {

    private val repository = AppDatabase.getTimeSettingsRepository(context)

    val timeSettings = repository?.getTimeSettings() ?: flowOf(null)

    fun saveTimeSettings(gameTimeMinutes: Int, breakTimeSeconds: Int) {
        viewModelScope.launch {
            try {
                repository?.saveTimeSettings(gameTimeMinutes, breakTimeSeconds)
                Log.d("TimeSettings", "Tiempos guardados: $gameTimeMinutes min, $breakTimeSeconds seg")
            } catch (e: Exception) {
                Log.e("TimeSettings", "Error guardando tiempos: ${e.message}")
            }
        }
    }

    fun startGameSession() {
        viewModelScope.launch {
            try {
                repository?.updateLastGameStartTime(Date())
                Log.d("TimeSettings", "Sesión de juego iniciada")
            } catch (e: Exception) {
                Log.e("TimeSettings", "Error iniciando sesión: ${e.message}")
            }
        }
    }

    suspend fun shouldShowTimeout(): Boolean {
        return try {
            val settings = repository?.getCurrentTimeSettings()
            if (settings?.lastGameStartTime != null) {
                val currentTime = Date().time
                val gameStartTime = settings.lastGameStartTime.time
                val gameDuration = settings.gameTimeMinutes * 60 * 1000L // Convertir a milisegundos

                currentTime - gameStartTime >= gameDuration
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("TimeSettings", "Error verificando timeout: ${e.message}")
            false
        }
    }

    suspend fun getRemainingBreakTime(): Long {
        return try {
            val settings = repository?.getCurrentTimeSettings()
            if (settings?.lastGameStartTime != null) {
                val currentTime = Date().time
                val gameStartTime = settings.lastGameStartTime.time
                val gameDuration = settings.gameTimeMinutes * 60 * 1000L
                val breakDuration = settings.breakTimeSeconds * 1000L
                val breakStartTime = gameStartTime + gameDuration

                val elapsedBreakTime = currentTime - breakStartTime
                maxOf(0, breakDuration - elapsedBreakTime) / 1000 // Devuelve segundos restantes
            } else {
                0L
            }
        } catch (e: Exception) {
            Log.e("TimeSettings", "Error calculando break time: ${e.message}")
            0L
        }
    }
}
