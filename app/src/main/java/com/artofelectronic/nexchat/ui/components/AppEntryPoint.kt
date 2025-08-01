package com.artofelectronic.nexchat.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.state.LoginState
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel

@Composable
fun AppEntryPoint(viewModel: AuthViewModel = hiltViewModel()) {

    val loginState by viewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.Loading -> FullScreenLoadingDialog()
        is LoginState.Authenticated -> AppScaffold(startDestination = Screens.Chats.route)
        is LoginState.UnAuthenticated -> AppScaffold(startDestination = Screens.Start.route)
    }
}