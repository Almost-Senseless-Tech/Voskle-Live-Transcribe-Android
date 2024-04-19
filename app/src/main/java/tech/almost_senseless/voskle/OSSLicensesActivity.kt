package tech.almost_senseless.voskle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import tech.almost_senseless.voskle.data.UserPreferences
import tech.almost_senseless.voskle.data.UserPreferencesRepository
import tech.almost_senseless.voskle.ui.theme.VoskleLiveTranscribeTheme

class OSSLicensesActivity : ComponentActivity() {
    private lateinit var viewModel: VLTViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            viewModel = viewModel(
                factory = VLTViewModelFactory(
                    UserPreferencesRepository(
                        applicationContext.dataStore
                    ), applicationContext
                )
            )
            val settings = viewModel.settings.collectAsState(initial = UserPreferences())
            VoskleLiveTranscribeTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = true, highContrast = settings.value.highContrast) {
                val context = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                    text = stringResource(id = R.string.oss_licenses),
                                        fontSize = 5.em,
                                        modifier = Modifier
                                            .semantics { heading() },
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            finish()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = context.getString(R.string.back)
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.mediumTopAppBarColors()
                            )
                        }
                    ) { contentPadding ->
                        LibrariesContainer(
                            Modifier
                                .fillMaxSize()
                                .padding(contentPadding)
                        )
                    }
                }
            }
        }
    }
}