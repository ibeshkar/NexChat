package com.artofelectronic.nexchat.ui.state

sealed class SignupState {
    data object Idle : SignupState()
    data object Loading : SignupState()
    data class Success(val user: Any? = null) : SignupState()
    data class Error(val message: String) : SignupState()
}