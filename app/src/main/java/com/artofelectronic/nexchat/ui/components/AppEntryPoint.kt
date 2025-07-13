package com.artofelectronic.nexchat.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel

@Composable
fun AppEntryPoint(viewModel: AuthViewModel = hiltViewModel()) {

    val isSignedIn by remember { viewModel.isLoggedIn }

    AppScaffold(
        startDestination = if (isSignedIn) Screens.Chats.route else Screens.Start.route
    )
}