package tech.almost_senseless.voskle

import androidx.compose.foundation.ScrollState
import tech.almost_senseless.voskle.vosklib.VoskHub

data class VLTState(
    val transcript: String = "",
    val isRecording: Boolean = false,
    val modelLoaded: Boolean = false,
    val languagePickerExpanded: Boolean = false,
    val displaySettingsDialog: Boolean = false,
    val displayPermissionsDialog: Boolean = false,
    val displayDownloadConfirmation: Boolean = true,
    val displayDownloadSuccess: Boolean = false,
    val error: ErrorKind? = null,
    val voskHubInstance: VoskHub? = null,
    val fetchState: FetchState = FetchState.NO_MODEL,
)
