package tech.almost_senseless.voskle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import tech.almost_senseless.voskle.ui.theme.VoskleLiveTranscribeTheme

class OSSLicensesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VoskleLiveTranscribeTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = true) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.oss_licenses), fontSize = 5.em,
                            modifier = Modifier
                                .semantics { heading() },
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LibrariesContainer(
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}