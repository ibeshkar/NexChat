package com.artofelectronic.nexchat.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.domain.usecases.auth.GetCurrentUserIdUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CreateOrUpdateUserUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUserProfileUseCase
import com.artofelectronic.nexchat.domain.usecases.users.SignOutUseCase
import com.artofelectronic.nexchat.domain.usecases.users.UpdateAvatarUrlUseCase
import com.artofelectronic.nexchat.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val userProfileUseCase: FetchUserProfileUseCase,
    private val createOrUpdateUserUseCase: CreateOrUpdateUserUseCase,
    private val updateAvatarUrlUseCase: UpdateAvatarUrlUseCase,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val currentUserId = getCurrentUserIdUseCase()

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    private val _signOutState = MutableStateFlow<UiState>(UiState.Idle)
    val signOutState: StateFlow<UiState> = _signOutState.asStateFlow()


    init {
        getUserProfile()
    }

    fun getUserProfile() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                if (currentUserId.isNullOrBlank()) {
                    _uiState.value = UiState.Error("User Id is not valid!")
                    return@launch
                }
                _userProfile.value = userProfileUseCase(currentUserId)
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                if (userId.isEmpty()) return@launch
                _userProfile.value = fetchUserProfileUseCase(userId)
            } catch (_: Exception) {
                _userProfile.value = null
            }
        }
    }

    fun updateUserProfile(user: User?, isAvatarChanged: Boolean = false, avatarUri: Uri? = null) {
        if (user == null) return

        try {
            _uiState.value = UiState.Loading
            viewModelScope.launch {
                if (isAvatarChanged && avatarUri != null) {
                    val avatarUrl = updateAvatarUrlUseCase(user.userId, avatarUri)
                    user.copy(avatarUrl = avatarUrl)
                }

                createOrUpdateUserUseCase(user)
                _userProfile.value = userProfileUseCase(user.userId)
                _uiState.value = UiState.Success
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message ?: "Something's wrong!")
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                _signOutState.value = UiState.Loading
                signOutUseCase()
                _signOutState.value = UiState.Success
            } catch (e: Exception) {
                _signOutState.value = UiState.Error(e.message ?: "Something's wrong!")
            }
        }
    }

}