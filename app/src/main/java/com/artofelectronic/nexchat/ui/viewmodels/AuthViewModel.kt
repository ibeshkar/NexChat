package com.artofelectronic.nexchat.ui.viewmodels

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.data.models.User
import com.artofelectronic.nexchat.domain.usecases.CheckUserSignInStatusUseCase
import com.artofelectronic.nexchat.domain.usecases.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignupWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithFacebookUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithGoogleUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithTwitterUseCase
import com.artofelectronic.nexchat.ui.state.AuthUiState
import com.artofelectronic.nexchat.ui.state.SignupState
import com.artofelectronic.nexchat.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AuthViewModel @Inject constructor(
    private val checkUserSignInStatusUseCase: CheckUserSignInStatusUseCase,
    private val signupWithEmailUseCase: SignupWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithTwitterUseCase: SignInWithTwitterUseCase,
    private val signInWithFacebook: SignInWithFacebookUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState: StateFlow<AuthUiState> get() = _uiState.asStateFlow()

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState: StateFlow<SignupState> get() = _signupState.asStateFlow()

    private val _signInState = MutableStateFlow<SignupState>(SignupState.Idle)
    open val signInState: StateFlow<SignupState> get() = _signInState.asStateFlow()

    var socialSignInState by mutableStateOf<Result<FirebaseUser>?>(null)
        private set

    private val _resetPasswordState = MutableStateFlow<SignupState>(SignupState.Idle)
    val resetPasswordState: StateFlow<SignupState> get() = _resetPasswordState.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    /**
     * Checks the authentication status of the user.
     */
    fun checkAuthenticationStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val isUserSignedIn = checkUserSignInStatusUseCase()
            _uiState.value =
                if (isUserSignedIn) AuthUiState.Authenticated else AuthUiState.Unauthenticated
        }
    }

    /**
     * Signs up a user with the provided email and password.
     */
    fun signupWithEmail(email: String, password: String) {
        _signupState.value = SignupState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                signupWithEmailUseCase(email, password).addOnCompleteListener { resultTask ->
                    if (resultTask.isSuccessful) {
                        resultTask.result?.user?.let { createUserDetailsInFirestoreIfRequired(it) }
                    } else {
                        _signupState.value =
                            SignupState.Error(
                                resultTask.exception?.message ?: "Unknown error occurred"
                            )
                    }
                }
            } catch (e: Exception) {
                _signupState.value = SignupState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Signs in with Google using the provided context and web client ID.
     */
    fun signInWithGoogle(context: Context) {
        _signupState.value = SignupState.Loading
        viewModelScope.launch {
            val result = signInWithGoogleUseCase(context)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    FirebaseUtil.signInWithFirebase(it).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            task.result?.user?.let { user ->
                                createUserDetailsInFirestoreIfRequired(user)
                            }
                        } else {
                            _signupState.value =
                                SignupState.Error(
                                    task.exception?.message ?: "Unknown error occurred"
                                )
                        }
                    }
                }
            } else {
                _signupState.value =
                    SignupState.Error(result.exceptionOrNull()?.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Handles the Facebook token to sign in with Facebook.
     */
    fun handleFacebookToken(token: String) {
        viewModelScope.launch {
            socialSignInState = signInWithFacebook(token)
        }
    }

    /**
     * Handles the error occurred during Facebook sign-in.
     */
    fun handleFacebookError(e: Throwable) {
        socialSignInState = Result.failure(e)
    }

    /**
     * Signs in with Twitter using the provided activity.
     */
    fun signInWithTwitter(activity: Activity) {
        _signupState.value = SignupState.Loading
        viewModelScope.launch {
            val result = signInWithTwitterUseCase(activity)
            if (result.isSuccess) {
                result.getOrNull()?.let { createUserDetailsInFirestoreIfRequired(it) }
            } else {
                _signupState.value =
                    SignupState.Error(result.exceptionOrNull()?.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Creates user details in Firestore if required.
     */
    private fun createUserDetailsInFirestoreIfRequired(user: FirebaseUser) {
        FirebaseUtil.currentUserDetails().get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                createUserRecord(user)
            } else {
                _signupState.value = SignupState.Success()
            }
        }.addOnFailureListener { exception ->
            _signupState.value =
                SignupState.Error(
                    exception.message ?: "Unknown error occurred"
                )
        }
    }

    /**
     * Creates a user record in Firestore using the provided FirebaseUser.
     */
    private fun createUserRecord(user: FirebaseUser) {
        val newUser = User(
            uid = user.uid,
            email = user.email ?: "",
            displayName = user.displayName ?: "",
            photoUrl = user.photoUrl?.toString() ?: "",
        )

        FirebaseUtil.createUserInFirestore(newUser).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _signupState.value = SignupState.Success()
            } else {
                _signupState.value =
                    SignupState.Error(task.exception?.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Signs in with email and password.
     */
    fun signInWithEmail(email: String, password: String) {
        _signInState.value = SignupState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _signInState.value = signInWithEmailUseCase(email, password)
        }
    }

    /**
     * Sends a password reset email to the provided email address.
     */
    fun sendPasswordResetEmail(email: String) {
        _resetPasswordState.value = SignupState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _resetPasswordState.value = sendPasswordResetEmailUseCase(email)
        }
    }
}