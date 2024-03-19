package tech.almost_senseless.voskle.ui.customComposables

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import tech.almost_senseless.voskle.BuildConfig
import tech.almost_senseless.voskle.ErrorKind
import tech.almost_senseless.voskle.OSSLicensesActivity
import tech.almost_senseless.voskle.R
import tech.almost_senseless.voskle.VLTAction
import tech.almost_senseless.voskle.VLTState
import tech.almost_senseless.voskle.data.FontSizes
import tech.almost_senseless.voskle.data.UserPreferences
import tech.almost_senseless.voskle.util.ObservableInputStream
import tech.almost_senseless.voskle.util.UnzipUtils
import tech.almost_senseless.voskle.vosklib.SPEAKER_MODEL_PATH
import java.io.IOException

@Composable
fun SettingsDialog(
    settings: UserPreferences,
    state: VLTState,
    contactUs: () -> Unit,
    onAction: (VLTAction) -> Unit,
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = {
        onAction(VLTAction.ShowSettingsDialog(false))
    }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Button(onClick = {
                    onAction(VLTAction.ShowSettingsDialog(false))
                }) {
                    Text(text = stringResource(id = R.string.dialog_close))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement =  Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item  {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(text = stringResource(id = R.string.application_settings), fontSize = 5.em,
                            modifier = Modifier
                                .semantics { heading() },
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(text = stringResource(id = R.string.transcript_font_size))
                            FontSizeRadioButtons(settings = settings, onAction = onAction)
                        }
                    }
                }
                item { MyDivider() }
                item {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .toggleable(
                            value = settings.autoscroll,
                            role = Role.Switch,
                            onValueChange = {
                                onAction(VLTAction.ToggleAutoscroll(!settings.autoscroll))
                            },
                        ),
                        verticalAlignment = Alignment.CenterVertically) {
                        Switch(checked = settings.autoscroll, null)
                        Text(text = stringResource(id = R.string.autoscroll))
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                    ) {
                        Text(text = stringResource(id = R.string.autoscroll_description))
                    }
                }
                item { MyDivider() }
                item {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .toggleable(
                            value = settings.stopRecordingOnFocusLoss,
                            role = Role.Switch,
                            onValueChange = {
                                onAction(VLTAction.ToggleStopRecordingOnFocusLoss(!settings.stopRecordingOnFocusLoss))
                            },
                        ),
                        verticalAlignment = Alignment.CenterVertically) {
                        Switch(checked = settings.stopRecordingOnFocusLoss, null)
                        Text(text = stringResource(id = R.string.stop_recording_on_focus_loss))
                    }
                }
                item { MyDivider() }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .toggleable(
                                value = settings.generateSpeakerLabels,
                                role = Role.Switch,
                                enabled = state.voskHubInstance?.isSpeakerModelAvailable() ?: false,
                                onValueChange = {
                                    onAction(VLTAction.ToggleGenerateSpeakerLabels(!settings.generateSpeakerLabels))
                                }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = settings.generateSpeakerLabels,
                            onCheckedChange = null,
                            enabled = state.voskHubInstance?.isSpeakerModelAvailable() ?: false
                        )
                        Text(text = stringResource(id = R.string.recognize_speakers))
                    }
                    if (state.voskHubInstance?.isSpeakerModelAvailable() == false) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Button(onClick = {
                                             downloadSpeakerModel(context, onAction)
                            },
                                enabled = !state.voskHubInstance.isSpeakerModelAvailable() && (state.speakerModelProcessingProgress == null),
                            ) {
                                Text(text = stringResource(id = R.string.download_speaker_model))
                            }
                            if (state.speakerModelProcessingProgress != null) {
                                LinearProgressIndicator(progress = state.speakerModelProcessingProgress / 100f)
                            }
                        }
                    }
                }
                item { MyDivider() }
                item {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                    ) {
                        Text(text = stringResource(id = R.string.about), fontSize = 5.em,
                            modifier = Modifier.semantics { heading() },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { contactUs() }) {
                            Text(text = stringResource(id = R.string.contact_us))
                        }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = {
                            val intent = Intent(context, OSSLicensesActivity::class.java)
                            context.startActivity(intent)
                        }) {
                            Text(text = stringResource(id = R.string.view_oss_licenses))
                        }
                    }
                }
                item { MyDivider() }
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val appVersion = BuildConfig.VERSION_NAME
                        val appVersionInt = BuildConfig.VERSION_CODE
                        Text(text = stringResource(id = R.string.version_info, appVersion, appVersionInt),
                            textAlign = TextAlign.Center)
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = R.string.copyright))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = R.string.license))
                    }
                }

            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun FontSizeRadioButtons(
    settings: UserPreferences,
    onAction: (VLTAction) -> Unit
) {
    Column(
        Modifier
            .selectableGroup()
            .then(
                Modifier
                    .padding(horizontal = 5.dp)
            )
    ) {
        FontSizes.values().forEach { fontSize ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (fontSize == settings.fontSize),
                        onClick = { onAction(VLTAction.SetTranscriptFontSize(fontSize)) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (fontSize == settings.fontSize),
                    onClick = null // null recommended for accessibility with screen readers
                )

                // We could technically use a mapper function to return the correct string resource
                // but here it's done dynamically for now.
                val context = LocalContext.current
                val fontSizeName = context.getString(context.resources.getIdentifier(
                    fontSize.name, "string", context.packageName
                ))
                Text(
                    text = fontSizeName,
                    fontSize = fontSize.size
                )
            }
        }
    }
}

@Composable
fun MyDivider(){
    Divider(color = Color(0xFF2d2d2d), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
}

private fun downloadSpeakerModel(context: Context, onAction: (VLTAction) -> Unit) {
    val url = "https://alphacephei.com/vosk/models/$SPEAKER_MODEL_PATH.zip"
    val request = Request.Builder()
        .url(url)
        .build()
    val httpClient = OkHttpClient()
    httpClient.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            onAction(VLTAction.SetError(ErrorKind.ConnectionFailed(e.localizedMessage ?: "")))
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) {
                    onAction(VLTAction.SetError(
                        ErrorKind.UnexpectedResponse("${response.code} (${response.message})")
                    ))
                } else {
                    try {
                        val externalFilesDir = context.getExternalFilesDir(null)
                        val dataStream = ObservableInputStream(response.body!!.byteStream()) {
                            val progress = it * 100 / response.body!!.contentLength()
                            onAction(VLTAction.UpdateSpeakerModelProcessingProgress(progress.toFloat()))
                        }
                        val sourceFile = kotlin.io.path.createTempFile().toFile()
                        sourceFile.outputStream().use { output ->
                            dataStream.copyTo(output)
                        }
                        UnzipUtils.unzip(sourceFile, "$externalFilesDir/models")
                        onAction(VLTAction.UpdateSpeakerModelProcessingProgress(null))
                        sourceFile.delete()
                    } catch (e: IOException) {
                        onAction(VLTAction.SetError(ErrorKind.DataProcessionFailed(
                        if (e.localizedMessage != null) {
                            "${e.localizedMessage}\n"
                        } else {
                            ""
                        }
                        )))
                    }
                }
            }
        }
    })
}