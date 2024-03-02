package tech.almost_senseless.voskle.ui.customComposables

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
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import tech.almost_senseless.voskle.BuildConfig
import tech.almost_senseless.voskle.R
import tech.almost_senseless.voskle.VLTAction
import tech.almost_senseless.voskle.data.UserPreferences

@Composable
fun SettingsDialog(
    settings: UserPreferences,
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
                            FontRatioRadioButtons(settings = settings, onAction = onAction)
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
                            val intent = with(Intent(context, OssLicensesMenuActivity::class.java)) {
                                val title = context.getString(R.string.oss_licenses)
                                putExtra("title", title)
                            }
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

@Composable
fun FontRatioRadioButtons(
    settings: UserPreferences,
    onAction: (VLTAction) -> Unit
) {
    val radioOptions = listOf(3f, 4f, 5f, 6f, 7f)
    Column(
        Modifier
            .selectableGroup()
            .then(
                Modifier
                    .padding(horizontal = 5.dp)
            )
    ) {
        radioOptions.forEach { value ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (value == settings.transcriptFontRatio),
                        onClick = { onAction(VLTAction.SetTranscriptFontRatio(value)) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (value == settings.transcriptFontRatio),
                    onClick = null // null recommended for accessibility with screen readers
                )
                Text(
                    text = "${(value*100).toInt()} %",
                    fontSize = value.em
                )
            }
        }
    }
}

@Composable
fun MyDivider(){
    Divider(color = Color(0xFF2d2d2d), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
}