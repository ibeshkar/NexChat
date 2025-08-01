package com.artofelectronic.nexchat.ui.state

sealed class LoginState {
    data object Loading : LoginState()
    data object Authenticated : LoginState()
    data object UnAuthenticated : LoginState()
}