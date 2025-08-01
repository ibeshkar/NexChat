package com.artofelectronic.nexchat.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.domain.usecases.users.CheckUserLoggedInUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CreateOrUpdateUserUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignupWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithFacebookUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithGoogleUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithTwitterUseCase
import com.artofelectronic.nexchat.ui.state.UiState
import com.artofelectronic.nexchat.ui.state.AuthFormData
import com.artofelectronic.nexchat.ui.state.LoginState
import com.artofelectronic.nexchat.utils.InputValidator
import com.artofelectronic.nexchat.utils.toDomain
import com.facebook.AccessToken
import com.facebook.CallbackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AuthViewModel @Inject constructor(
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase,
    private val signupWithEmailUseCase: SignupWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val createOrUpdateUserUseCase: CreateOrUpdateUserUseCase,
    private val signInWithTwitterUseCase: SignInWithTwitterUseCase,
    private val signInWithFacebook: SignInWithFacebookUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState> = _loginState

    private val _authFormData = MutableStateFlow(AuthFormData())
    val authFormData: StateFlow<AuthFormData> = _authFormData

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    val facebookCallbackManager = CallbackManager.Factory.create()


    init {
        viewModelScope.launch {
            if (checkUserLoggedInUseCase())
                _loginState.value = LoginState.Authenticated
            else
                _loginState.value = LoginState.UnAuthenticated
        }
    }


    fun onEmailChanged(email: String) {
        _authFormData.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _authFormData.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChanged(confirm: String) {
        _authFormData.update { it.copy(confirmPassword = confirm, confirmPasswordError = null) }
    }

    fun resetAuthState() {
        _uiState.value = UiState.Idle
    }


    fun signupWithEmail() {
        val state = _authFormData.value

        val emailError = InputValidator.validateEmail(state.email)
        val passwordError = InputValidator.validatePassword(state.password)
        val confirmPasswordError =
            if (state.password != state.confirmPassword) "Passwords do not match" else null

        if (emailError != null || passwordError != null || confirmPasswordError != null) {
            _authFormData.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        _authFormData.update { it.copy(isLoading = true) }
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val result = signupWithEmailUseCase(state.email, state.password)
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createOrUpdateUserUseCase(user.toDomain())

                    _uiState.value = UiState.Success
                    _authFormData.update { it.copy(isLoading = false) }
                } else {
                    _uiState.value = UiState.Error("Signup failed")
                    _authFormData.update { it.copy(isLoading = false) }
                    return@launch
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Signup failed")
                _authFormData.update { it.copy(isLoading = false) }
            }
        }
    }


    fun signupWithGoogle() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = signInWithGoogleUseCase()
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createOrUpdateUserUseCase(user.toDomain())
                }

                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Google Sign-In failed")
            }
        }
    }


    fun handleFacebookAccessToken(accessToken: AccessToken) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = signInWithFacebook(accessToken.token)
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createOrUpdateUserUseCase(user.toDomain())
                }

                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Facebook sign-in failed")
            }
        }
    }

    fun signInWithTwitter(activity: Activity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val result = signInWithTwitterUseCase(activity)
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createOrUpdateUserUseCase(user.toDomain())
                }

                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Twitter sign-in failed")
            }
        }
    }

    fun signInWithEmail() {
        val state = _authFormData.value

        val emailError = InputValidator.validateEmail(state.email)
        val passwordError = InputValidator.validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            _authFormData.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        _authFormData.update { it.copy(isLoading = true) }
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val result = signInWithEmailUseCase(state.email, state.password)
                if (result != null && result.user != null) {
                    _uiState.value = UiState.Success
                    _authFormData.update { it.copy(isLoading = false) }
                } else {
                    _uiState.value = UiState.Error("SignIn failed")
                    _authFormData.update { it.copy(isLoading = false) }
                    return@launch
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "SignIn failed")
                _authFormData.update { it.copy(isLoading = false) }
            }
        }
    }

    fun sendPasswordResetEmail() {
        val formData = _authFormData.value

        val emailError = InputValidator.validateEmail(formData.email)
        if (emailError != null) {
            _authFormData.update { it.copy(emailError = emailError) }
            return
        }

        _authFormData.update { it.copy(isLoading = true) }
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                sendPasswordResetEmailUseCase(formData.email)
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error occurred!")
                _authFormData.update { it.copy(isLoading = false) }
            }
        }
    }
}