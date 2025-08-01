package com.artofelectronic.nexchat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.domain.usecases.auth.GetCurrentUserIdUseCase
import com.artofelectronic.nexchat.domain.usecases.users.ObserveUsersUseCase
import com.artofelectronic.nexchat.domain.usecases.users.UserChangeListenerUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.CreateOrContinueChatUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUsersUseCase
import com.artofelectronic.nexchat.utils.Resource
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNullOrBlank

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val observeUsersUseCase: ObserveUsersUseCase,
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val userChangeListenerUseCase: UserChangeListenerUseCase,
    private val createOrContinueChatUseCase: CreateOrContinueChatUseCase
) : ViewModel() {

    private val _currentUserId = MutableStateFlow(getCurrentUserIdUseCase())
    val currentUserId = _currentUserId

    private val _userList = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val userList: StateFlow<Resource<List<User>>> = _userList

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation

    private var usersListener: ListenerRegistration? = null

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            fetchUsersUseCase()
            usersListener = userChangeListenerUseCase()
            observeUsersUseCase().collectLatest {
                _userList.value = Resource.Success(it)
            }
        }
    }

    fun syncUsers() {
        viewModelScope.launch {
            val currentData = (_userList.value as? Resource.Success)?.data
            _userList.value = Resource.Loading(currentData)
            try {
                userChangeListenerUseCase()
            } catch (e: Exception) {
                _userList.value = Resource.Error(e, currentData)
            }
        }
    }

    fun onUserSelected(currentUserId: String?, selectedUserId: String) {
        viewModelScope.launch {
            try {
                if (currentUserId.isNullOrBlank()) {
                    return@launch
                }
                val chat = createOrContinueChatUseCase(currentUserId, selectedUserId)
                _navigation.emit(chat.chatId)
            } catch (e: Exception) {
                _navigation.emit(e.message ?: "Failed to start chat")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        usersListener?.remove()
    }
}