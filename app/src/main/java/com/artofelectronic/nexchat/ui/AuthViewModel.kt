package com.artofelectronic.nexchat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.domain.CheckUserSignInStatusUseCase
import com.artofelectronic.nexchat.ui.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkUserSignInStatusUseCase: CheckUserSignInStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState: StateFlow<AuthUiState> get() = _uiState.asStateFlow()


    init {
        checkAuthenticationStatus()
    }

    /**
     * Checks the authentication status of the user.
     */
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            val isUserSignedIn = checkUserSignInStatusUseCase()
            _uiState.value =
                if (isUserSignedIn) AuthUiState.Authenticated else AuthUiState.Unauthenticated
        }
    }
}