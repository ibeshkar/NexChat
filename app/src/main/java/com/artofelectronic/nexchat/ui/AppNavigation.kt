package com.artofelectronic.nexchat.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.artofelectronic.nexchat.ui.navigation.Routing
import com.artofelectronic.nexchat.ui.state.AuthUiState
import com.artofelectronic.nexchat.ui.theme.NexChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppNavigation : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep Splash screen during authentication check
        splashScreen.setKeepOnScreenCondition { authViewModel.uiState.value is AuthUiState.Loading }

        enableEdgeToEdge()
        setContent {
            MyApp(authViewModel, lifecycleScope)
        }
    }
}

@Composable
private fun MyApp(authViewModel: AuthViewModel, lifecycleScope: LifecycleCoroutineScope) {
    NexChatTheme {
        val navController = rememberNavController()
        val uiState by authViewModel.uiState.collectAsState()

        // Navigate based on authentication state
        LaunchedEffect(key1 = uiState) {
            when (uiState) {
                AuthUiState.Authenticated -> navController.navigate("home")
                AuthUiState.Unauthenticated -> navController.navigate("start")
                AuthUiState.Loading -> {}
            }
        }

        Routing(navController)
    }
}

