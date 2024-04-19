package tech.almost_senseless.voskle.data

import android.util.Log
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val TAG = "PreferencesRepo"

enum class FontSizes(val size: TextUnit, val lineHeight: TextUnit) {
    MEDIUM(16.sp, 24.sp),
    LARGE(24.sp, 36.sp),
    VERY_LARGE(36.sp, 54.sp),
    LARGEST(72.sp, 108.sp)
}

data class UserPreferences(
    val language: Languages = Languages.ENGLISH_US,
    val fontSize: FontSizes = FontSizes.MEDIUM,
    val autoscroll: Boolean = true,
    val stopRecordingOnFocusLoss: Boolean = true,
    val generateSpeakerLabels: Boolean = false,
    val highContrast: Boolean = true
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val language = stringPreferencesKey("language")
        val transcriptFontSize = stringPreferencesKey("transcript_font_size")
        val autoscroll = booleanPreferencesKey("autoscroll")
        val stopRecordingOnFocusLoss = booleanPreferencesKey("stopRecordingOnFocusLoss")
        val generateSpeakerLabels = booleanPreferencesKey("generateSpeakerLabels")
        val highContrast = booleanPreferencesKey("highContrast")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun updateLanguage(language: Languages) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.language] = language.name
        }
    }

    suspend fun updateTranscriptFontSize(transcriptFontSize: FontSizes) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.transcriptFontSize] = transcriptFontSize.name
        }
    }

    suspend fun updateAutoscroll(autoscroll: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.autoscroll] = autoscroll
        }
    }

    suspend fun updateStopRecordingOnFocusLoss(stopRecordingOnFocusLoss: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.stopRecordingOnFocusLoss] = stopRecordingOnFocusLoss
        }
    }

    suspend fun updateGenerateSpeakerLabels(generateSpeakerLabels: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.generateSpeakerLabels] = generateSpeakerLabels
        }
    }

    suspend fun updateHighContrast(highContrast: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.highContrast] = highContrast
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val language = Languages.valueOf(
            preferences[PreferencesKeys.language] ?: Languages.ENGLISH_US.name
        )
        val transcriptFontSize = FontSizes.valueOf(
            preferences[PreferencesKeys.transcriptFontSize] ?: FontSizes.MEDIUM.name
        )
        val autoscroll = preferences[PreferencesKeys.autoscroll] ?: true
        val stopRecordingOnFocusLoss = preferences[PreferencesKeys.stopRecordingOnFocusLoss] ?: true
        val generateSpeakerLabels = preferences[PreferencesKeys.generateSpeakerLabels] ?: false
        val highContrast = preferences[PreferencesKeys.highContrast] ?: true
return UserPreferences(language, transcriptFontSize, autoscroll, stopRecordingOnFocusLoss, generateSpeakerLabels, highContrast)
    }
}