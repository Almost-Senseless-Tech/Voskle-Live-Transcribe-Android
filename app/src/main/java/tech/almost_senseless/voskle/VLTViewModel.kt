package tech.almost_senseless.voskle

 import android.content.Context
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.text.TextRange
 import androidx.compose.ui.text.input.TextFieldValue
 import androidx.lifecycle.ViewModel
 import androidx.lifecycle.ViewModelProvider
 import androidx.lifecycle.viewModelScope
 import kotlinx.coroutines.launch
 import tech.almost_senseless.voskle.data.Languages
 import tech.almost_senseless.voskle.data.UserPreferencesRepository
 import tech.almost_senseless.voskle.vosklib.VoskHub

class VLTViewModel(private val userPreferences: UserPreferencesRepository, @Suppress("StaticFieldLeak") private val context: Context) : ViewModel() {

    val settings = userPreferences.userPreferencesFlow

    var state by mutableStateOf(VLTState())
        private set


    fun onAction(action: VLTAction){
        when(action){
            is VLTAction.UpdateTranscript -> updateTranscript(action.text)
            is VLTAction.UpdateLastLine -> updateLastLine(action.text)
            is VLTAction.SetLanguage -> setLanguage(action.language)
            is VLTAction.SetRecordingStatus -> setRecordingStatus(action.status)
            is VLTAction.SetModelStatus -> setModelStatus(action.status)
            is VLTAction.SetLanguagePickerState -> setLanguagePickerState(action.expanded)
            is VLTAction.ClearTranscript -> clearTranscript()
            is VLTAction.ShowSettingsDialog -> showSettingsDialog(action.display)
            is VLTAction.ShowPermissionsDialog -> showPermissionsDialog(action.display)
            is VLTAction.SetTranscriptFontRatio -> setTranscriptFontRatio(action.ratio)
            is VLTAction.ShowDownloadConfirmation -> displayDownloadConfirmation(action.display)
            is VLTAction.DownloadModel -> downloadModel(action.downloadFunction, action.modelPath)
            is VLTAction.ShowDownloadSuccess -> displayDownloadSuccess(action.display)
            is VLTAction.SetError -> setError(action.error)
            is VLTAction.DismissError -> dismissError()
            is VLTAction.ToggleAutoscroll -> toggleAutoscroll(action.autoscroll)
            is VLTAction.RegisterVoskHub -> registerVoskHub(action.voskHub)
            is VLTAction.UpdateFetchState -> updateFetchstate(action.state)
            is VLTAction.SetKeyboardInput -> setKeyboardInput(action.enabled)
            is VLTAction.EditTranscript -> editTranscript(action.text)
            is VLTAction.ToggleStopRecordingOnFocusLoss -> toggleStopRcordingOnFocusLoss(action.stopRecordingOnFocusLoss)
            is VLTAction.SetFocusedState -> setFocusedState(action.focused)
            is VLTAction.SetTranscriptFocused -> setTranscriptFocused(action.focused)
            is VLTAction.SetResumeRecording -> resumeRecording(action.resume)
            is VLTAction.MoveCursorLeft -> moveCursorLeft()
            is VLTAction.MoveCursorRight -> moveCursorRight()
        }
    }

    private fun setModelStatus(status: Boolean) {
        state = state.copy(modelLoaded = status)
        if (status)
        {
            updateFetchstate(FetchState.READY)
        }
        else
        {
            updateFetchstate(FetchState.NO_MODEL)
        }
    }

    private fun setRecordingStatus(status: Boolean) {
        state = state.copy(isRecording = status)
    }

    private fun setLanguage(language: Languages) {
        viewModelScope.launch {
            userPreferences.updateLanguage(language)
        }
    }

    private fun updateTranscript(text: String) {
        var newTranscript = state.transcript
        if (text != "\n") {
            if (text.isNotEmpty()) {
                newTranscript = if (!state.transcript.contains('\n'))
                    "$text\n"
                else
                    newTranscript.replaceAfterLast("\n", "$text\n")
            }
        } else {
            if (newTranscript.isNotEmpty() && !newTranscript.endsWith("\n"))
                newTranscript = "$newTranscript\n"
        }
        state = state.copy(transcript = newTranscript, textFieldValue = TextFieldValue(
            text = newTranscript,
            selection = TextRange(newTranscript.length)
        ))
    }

    private fun updateLastLine(text: String) {
        var newTranscript = state.transcript
        if (text.isNotEmpty()) {
            newTranscript = if (!state.transcript.contains('\n'))
                text
            else
                newTranscript.replaceAfterLast("\n", text)
        }
        state = state.copy(transcript = newTranscript, textFieldValue = TextFieldValue(
            text = newTranscript,
            selection = TextRange(newTranscript.length)
        ))
    }

    private fun setLanguagePickerState(expanded: Boolean) {
        state = state.copy(languagePickerExpanded = expanded)
    }

    private fun clearTranscript() {
        state = state.copy(
            transcript = "",
            textFieldValue = TextFieldValue(text = "", selection = TextRange.Zero)
        )
    }

    private fun showSettingsDialog(display: Boolean) {
        state = state.copy(displaySettingsDialog = display)
    }

    private fun showPermissionsDialog(display: Boolean) {
        state = state.copy(displayPermissionsDialog = display)
    }

    private fun downloadModel(downloadFunction: (VLTViewModel, String) -> Unit, modelPath: String) {
        downloadFunction(this, modelPath)
    }

    private fun setTranscriptFontRatio(ratio: Float) {
        viewModelScope.launch {
            userPreferences.updateTranscriptFontRatio(ratio)
        }
    }

    private fun displayDownloadConfirmation(display: Boolean) {
        state = state.copy(displayDownloadConfirmation = display)
    }

    private fun displayDownloadSuccess(display: Boolean) {
        state = state.copy(displayDownloadSuccess = display)
    }

    private fun setError(error: ErrorKind) {
        state = state.copy(error = error)
    }

    private fun dismissError() {
        state = state.copy(error = null)
    }

    private fun toggleAutoscroll(autoscroll: Boolean) {
        viewModelScope.launch {
            userPreferences.updateAutoscroll(autoscroll)
        }
    }

    private fun toggleStopRcordingOnFocusLoss(stopRecordingOnFocusLoss: Boolean) {
        viewModelScope.launch {
            userPreferences.updateStopRecordingOnFocusLoss(stopRecordingOnFocusLoss)
        }
    }

    private fun registerVoskHub(voskHub: VoskHub){
        if (state.voskHubInstance !== voskHub)
        {
            state.voskHubInstance?.reset()
            state = state.copy(voskHubInstance = voskHub)
        }
    }

    private fun updateFetchstate(fetchState: FetchState) {
        state = state.copy(fetchState = fetchState)
    }


    private fun initVoskHub(){
        val voskHub = VoskHub(context)
        voskHub.subscribeToViewModel(this)
        voskHub.initModel()
    }

    fun getVoskHub(): VoskHub {
        if(this.state.voskHubInstance == null)
            this.initVoskHub()
        return this.state.voskHubInstance!!
    }

    override fun onCleared(){
        this.state.voskHubInstance?.reset()
        super.onCleared()
    }

    private fun setKeyboardInput(enabled: Boolean) {
        state = state.copy(keyboardInput = enabled)
    }

    private fun editTranscript(text: String) {
        val textRange = TextRange(text.length)
        if (state.textFieldValue.selection == textRange) {
            state = state.copy(
                transcript = text,
                textFieldValue = TextFieldValue(
                    text = text,
                    selection = textRange
                )
            )
        } else {
            val lengthDiff = text.length - state.textFieldValue.text.length
            val currentSelection = state.textFieldValue.selection.start
            state = state.copy(
                transcript = text,
                textFieldValue = TextFieldValue(
                    text = text,
                    selection = TextRange(currentSelection + lengthDiff)
                )
            )
        }
    }

    private fun setFocusedState(focused: Boolean) {
        state = state.copy(isFocused = focused)
    }

    private fun setTranscriptFocused(focused: Boolean) {
        state = state.copy(transcriptFocused = focused)
    }

    private fun resumeRecording(resume: Boolean) {
        state = state.copy(resumeRecording = resume)
    }

    private fun moveCursorLeft() {
        val newSlection = state.textFieldValue.selection.start - 1
        state = state.copy(
            textFieldValue = state.textFieldValue.copy(
                selection = TextRange(newSlection)
            )
        )
    }

    private fun moveCursorRight() {
        val newSlection = state.textFieldValue.selection.start + 1
        state = state.copy(
            textFieldValue = state.textFieldValue.copy(
                selection = TextRange(newSlection)
            )
        )
    }
}

@Suppress("UNCHECKED_CAST")
class VLTViewModelFactory(private val userPreferences: UserPreferencesRepository, private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = VLTViewModel(userPreferences, context) as T
}