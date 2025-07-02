package com.artofelectronic.nexchat.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.navigation.Routing
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.state.AuthUiState
import com.artofelectronic.nexchat.ui.theme.NexChatTheme
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep Splash screen during authentication check
        splashScreen.setKeepOnScreenCondition {
            authViewModel.uiState.value is AuthUiState.Loading
        }

        enableEdgeToEdge()

        callbackManager = CallbackManager.Factory.create()

        // Register Facebook callback
        LoginManager.getInstance().registerCallback(
            callbackManager,
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
        val context = navController.context

        when (uiState) {
            AuthUiState.Authenticated -> {
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(this)
                    (context as? AuthActivity)?.finish()
                }
            }

            else -> Routing(
                navController,
                Screens.Start.route,
                authViewModel
            )
        }
    }
}