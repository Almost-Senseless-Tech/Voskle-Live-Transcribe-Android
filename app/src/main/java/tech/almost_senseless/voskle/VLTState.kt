package tech.almost_senseless.voskle

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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
    val keyboardInput: Boolean = false,
    val isFocused: Boolean = true,
    val transcriptFocused: Boolean = false,
    val resumeRecording: Boolean = false,
    val textFieldValue: TextFieldValue = TextFieldValue(text = "", selection = TextRange.Zero),
    val speakers: MutableList<DoubleArray> = mutableListOf(),
    val currentSpeaker: Int? = null,
    val previousSpeaker: Int? = -1,
    val modelProcessingProgress: Float? = null,
    val speakerModelProcessingProgress: Float? = null
)
