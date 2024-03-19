package tech.almost_senseless.voskle.ui.customComposables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import tech.almost_senseless.voskle.R
import tech.almost_senseless.voskle.VLTAction
import tech.almost_senseless.voskle.VLTState
import tech.almost_senseless.voskle.data.UserPreferences

@Composable
fun Textarea(
    settings: UserPreferences,
    state: VLTState,
    modifier: Modifier = Modifier,
    onAction: (VLTAction) -> Unit,
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(scrollState.maxValue) {
        if (state.isRecording && settings.autoscroll) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    TextField(
        value = state.textFieldValue,
        onValueChange = {
            if (state.keyboardInput) {
                onAction(VLTAction.EditTranscript(it.text))
            } else {
                onAction(VLTAction.UpdateTranscript(it.text))
            }
                        },
        readOnly = !state.keyboardInput,
        label = { Text(text = stringResource(id = R.string.transcript_label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .verticalScroll(scrollState)
            .onKeyEvent {
                val currentSelection = state.textFieldValue.selection.start
                if (it.key == Key.DirectionLeft && TextRange(currentSelection) != TextRange.Zero) {
                    onAction(VLTAction.MoveCursorLeft)
                    true
                } else {
                    if (it.key == Key.DirectionRight && TextRange(currentSelection) != TextRange(
                            state.transcript.length
                        )
                    ) {
                        onAction(VLTAction.MoveCursorRight)
                        true
                    } else {
                        false
                    }
                }
            }
            .then(modifier),
        textStyle = LocalTextStyle.current.copy(fontSize = settings.fontSize.size, lineHeight = settings.fontSize.lineHeight),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                if (state.resumeRecording) {
                    onAction(VLTAction.SetResumeRecording(false))
                    state.voskHubInstance!!.toggleRecording()
                }
                onAction(VLTAction.SetKeyboardInput(false))
            }
        )
    )
}