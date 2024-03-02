package tech.almost_senseless.voskle.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val TAG = "PreferencesRepo"

data class UserPreferences(
    val language: Languages = Languages.ENGLISH_US,
    val transcriptFontRatio: Float = 3f,
    val autoscroll: Boolean = true,
    val stopRecordingOnFocusLoss: Boolean = true
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val language = stringPreferencesKey("language")
        val transcriptionFontRatio = floatPreferencesKey("transcription_font_ratio")
        val autoscroll = booleanPreferencesKey("autoscroll")
        val stopRecordingOnFocusLoss = booleanPreferencesKey("stopRecordingOnFocusLoss")
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

    suspend fun updateTranscriptFontRatio(transcriptFontRatio: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.transcriptionFontRatio] = transcriptFontRatio
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


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val language = Languages.valueOf(
            preferences[PreferencesKeys.language] ?: Languages.ENGLISH_US.name
        )
        val transcriptFontRatio = preferences[PreferencesKeys.transcriptionFontRatio] ?: 3f
        val autoscroll = preferences[PreferencesKeys.autoscroll] ?: true
        val stopRecordingOnFocusLoss = preferences[PreferencesKeys.stopRecordingOnFocusLoss] ?: true
return UserPreferences(language, transcriptFontRatio, autoscroll, stopRecordingOnFocusLoss)
    }
}