package com.artofelectronic.nexchat.ui.state


data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false
)

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}



data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false
)

sealed class ForgotPasswordUiEvent {
    data class ShowToast(val message: String) : ForgotPasswordUiEvent()
    data object NavigateBack : ForgotPasswordUiEvent()
}