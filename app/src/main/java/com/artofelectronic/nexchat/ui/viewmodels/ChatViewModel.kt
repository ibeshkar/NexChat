package com.artofelectronic.nexchat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.domain.usecases.auth.GetCurrentUserIdUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.FetchChatsOnceIfNeededUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUserProfileUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.GetMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ObserveChatsUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ObserveMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.RefreshChatsUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.RetryPendingUpdatesUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.SendMessageUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ChatRealtimeSyncUseCase
import com.artofelectronic.nexchat.utils.Resource
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val observeChatsUseCase: ObserveChatsUseCase,
    private val refreshChatsUseCase: RefreshChatsUseCase,
    private val retryPendingUpdatesUseCase: RetryPendingUpdatesUseCase,
    private val fetchChatsOnceIfNeededUseCase: FetchChatsOnceIfNeededUseCase,
    private val chatRealtimeSyncUseCase: ChatRealtimeSyncUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase
) : ViewModel() {

    private val _chatList = MutableStateFlow<Resource<List<Chat>>>(Resource.Loading())
    val chatList: StateFlow<Resource<List<Chat>>> = _chatList

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _logoutState = MutableStateFlow(false)
    val logoutState: StateFlow<Boolean> = _logoutState

    private var chatsListener: ListenerRegistration? = null
    private var messageListener: ListenerRegistration? = null


    init {
        getCurrentUserId()
    }

    private fun getCurrentUserId() {
        _currentUserId.value = getCurrentUserIdUseCase()
    }

    fun observeChats(userId: String?) {
        viewModelScope.launch {
            try {
                if (userId.isNullOrEmpty()) {
                    return@launch
                }

                fetchChatsOnceIfNeededUseCase(userId)
                chatsListener = chatRealtimeSyncUseCase(userId)
                observeChatsUseCase().collectLatest { chats ->
                    _chatList.value = Resource.Success(chats)
                }
            } catch (e: Exception) {
                _chatList.value = Resource.Error(e)
            }
        }
    }

    fun refreshChats(userId: String?) {
        viewModelScope.launch {
            try {
                if (userId.isNullOrEmpty()) return@launch

                val currentData = (_chatList.value as? Resource.Success)?.data
                _chatList.value = Resource.Loading(currentData)
                refreshChatsUseCase(userId)
            } catch (e: Exception) {
                _chatList.value = Resource.Error(e)
            }
        }
    }

    fun retryPending() {
        viewModelScope.launch {
            retryPendingUpdatesUseCase()
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

    fun observeMessages(chatId: String) {
        viewModelScope.launch {
            messageListener = getMessagesUseCase(chatId)
            observeMessagesUseCase(chatId).collectLatest { messages ->
                _messages.value = messages
            }
        }
    }

    fun sendMessage(chatId: String, message: String, senderId: String, receiverId: String) {
        viewModelScope.launch {
            val message = Message(
                messageId = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = senderId,
                receiverId = receiverId,
                text = message,
                timestamp = Timestamp.now()
            )
            sendMessageUseCase(message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatsListener?.remove()
        messageListener?.remove()
    }
}