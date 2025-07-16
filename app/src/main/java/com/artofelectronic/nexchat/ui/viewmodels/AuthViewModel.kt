package com.artofelectronic.nexchat.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.data.models.User
import com.artofelectronic.nexchat.domain.usecases.CheckUserLoggedInUseCase
import com.artofelectronic.nexchat.domain.usecases.CreateUserInFirestoreUseCase
import com.artofelectronic.nexchat.domain.usecases.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignupWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithFacebookUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithGoogleUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithTwitterUseCase
import com.artofelectronic.nexchat.ui.state.AuthState
import com.artofelectronic.nexchat.ui.state.AuthUiState
import com.artofelectronic.nexchat.utils.InputValidator
import com.facebook.AccessToken
import com.facebook.CallbackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val createUserInFirestoreUseCase: CreateUserInFirestoreUseCase,
    private val signInWithTwitterUseCase: SignInWithTwitterUseCase,
    private val signInWithFacebook: SignInWithFacebookUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _isAuthStatusPending = MutableStateFlow(true)
    val isAuthStatusPending: StateFlow<Boolean> = _isAuthStatusPending

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState: StateFlow<AuthUiState> = _authUiState

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    val facebookCallbackManager = CallbackManager.Factory.create()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoggedIn.value = checkUserLoggedInUseCase()
            _isAuthStatusPending.value = false
        }
    }


    fun onEmailChanged(email: String) {
        _authUiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _authUiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChanged(confirm: String) {
        _authUiState.update { it.copy(confirmPassword = confirm, confirmPasswordError = null) }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }


    fun signupWithEmail() {
        val state = _authUiState.value

        val emailError = InputValidator.validateEmail(state.email)
        val passwordError = InputValidator.validatePassword(state.password)
        val confirmPasswordError =
            if (state.password != state.confirmPassword) "Passwords do not match" else null

        if (emailError != null || passwordError != null || confirmPasswordError != null) {
            _authUiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        _authUiState.update { it.copy(isLoading = true) }
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val result = signupWithEmailUseCase(state.email, state.password)
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createUserInFirestoreUseCase(
                        User(
                            id = user.uid,
                            email = state.email,
                            name = user.displayName ?: user.email?.substringBefore("@").orEmpty(),
                            avatar = "" // Email signup has no avatar by default
                        )
                    )

                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Signup failed")
                    _authUiState.update { it.copy(isLoading = false) }
                    return@launch
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Signup failed")
                _authUiState.update { it.copy(isLoading = false) }
            }
        }
    }


    fun signupWithGoogle() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = signInWithGoogleUseCase()
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createUserInFirestoreUseCase(
                        User(
                            id = user.uid,
                            email = user.email ?: "",
                            name = user.displayName ?: user.email?.substringBefore("@").orEmpty(),
                            avatar = user.photoUrl?.toString() ?: ""
                        )
                    )
                }

                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Google Sign-In failed")
            }
        }
    }


    fun handleFacebookAccessToken(accessToken: AccessToken) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = signInWithFacebook(accessToken.token)
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createUserInFirestoreUseCase(
                        User(
                            id = user.uid,
                            email = user.email ?: "",
                            name = user.displayName ?: "",
                            avatar = user.photoUrl?.toString() ?: ""
                        )
                    )
                }

                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Facebook sign-in failed")
            }
        }
    }

    fun signInWithTwitter(activity: Activity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val result = signInWithTwitterUseCase(activity)
                if (result != null && result.user != null) {
                    val user = result.user!!
                    createUserInFirestoreUseCase(
                        User(
                            id = user.uid,
                            email = user.email ?: "",
                            name = user.displayName ?: "",
                            avatar = user.photoUrl?.toString() ?: ""
                        )
                    )
                }

                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Twitter sign-in failed")
            }
        }
    }

    fun signInWithEmail() {
        val state = _authUiState.value

        val emailError = InputValidator.validateEmail(state.email)
        val passwordError = InputValidator.validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            _authUiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        _authUiState.update { it.copy(isLoading = true) }
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val result = signInWithEmailUseCase(state.email, state.password)
                if (result != null && result.user != null) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("SignIn failed")
                    _authUiState.update { it.copy(isLoading = false) }
                    return@launch
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "SignIn failed")
                _authUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun sendPasswordResetEmail() {
        val state = _authUiState.value

        val emailError = InputValidator.validateEmail(state.email)
        if (emailError != null) {
            _authUiState.update { it.copy(emailError = emailError) }
            return
        }

        _authUiState.update { it.copy(isLoading = true) }
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                sendPasswordResetEmailUseCase(state.email)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error occurred!")
                _authUiState.update { it.copy(isLoading = false) }
            }
        }
    }
}