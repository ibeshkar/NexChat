package com.artofelectronic.nexchat.ui

import android.content.Intent
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppNavigation : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep Splash screen during authentication check
        splashScreen.setKeepOnScreenCondition { authViewModel.uiState.value is AuthUiState.Loading }

        enableEdgeToEdge()

        callbackManager = CallbackManager.Factory.create()

        // Register Facebook callback
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    authViewModel.handleFacebookToken(result.accessToken.token)
                }

                override fun onCancel() {
                    authViewModel.handleFacebookError(
                        FacebookException("Facebook login cancelled")
                    )
                }

                override fun onError(error: FacebookException) {
                    authViewModel.handleFacebookError(error)
                }
            })

        setContent {
            MyApp(authViewModel)
        }
    }

    override fun onActivityResult(req: Int, res: Int, data: Intent?) {
        super.onActivityResult(req, res, data)
        callbackManager.onActivityResult(req, res, data)
    }
}

@Composable
private fun MyApp(authViewModel: AuthViewModel) {
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

        Routing(navController, authViewModel)
    }
}

