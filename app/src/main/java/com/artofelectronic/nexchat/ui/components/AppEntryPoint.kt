package com.artofelectronic.nexchat.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.artofelectronic.nexchat.ui.activities.MainActivity
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel

@Composable
fun AppEntryPoint(viewModel: AuthViewModel = hiltViewModel()) {

    val isSignedIn by viewModel.isLoggedIn.collectAsState()
    val isAuthStatusPending by viewModel.isAuthStatusPending.collectAsState()

    if (isAuthStatusPending) {
        (LocalActivity.current as MainActivity).installSplashScreen()
    } else {
        AppScaffold(
            startDestination = if (isSignedIn) Screens.Chats.route else Screens.Start.route
        )
    }
}