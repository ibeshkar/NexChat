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
import com.artofelectronic.nexchat.domain.usecases.chats.SendMessageUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ChatRealtimeSyncUseCase
import com.artofelectronic.nexchat.utils.Resource
import com.artofelectronic.nexchat.utils.displayName
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val observeChatsUseCase: ObserveChatsUseCase,
    private val fetchChatsOnceIfNeededUseCase: FetchChatsOnceIfNeededUseCase,
    private val chatRealtimeSyncUseCase: ChatRealtimeSyncUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase
) : ViewModel() {

    private val _chatList = MutableStateFlow<Resource<List<Chat>>>(Resource.Loading())
    val chatList: StateFlow<Resource<List<Chat>>> = _chatList.asStateFlow()

    private val _userProfiles = MutableStateFlow<Map<String, User?>>(emptyMap())
    val userProfiles: StateFlow<Map<String, User?>> = _userProfiles.asStateFlow()

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private var chatsListener: ListenerRegistration? = null
    private var messageListener: ListenerRegistration? = null

    val currentUserId = getCurrentUserIdUseCase()


    init {
        observeChats()
    }

    fun observeChats() {
        viewModelScope.launch {
            try {
                if (currentUserId.isNullOrEmpty()) {
                    _chatList.value = Resource.Error(Throwable("User Id is not valid!"))
                    return@launch
                }

                fetchChatsOnceIfNeededUseCase(currentUserId)
                chatsListener = chatRealtimeSyncUseCase(currentUserId)
                observeChatsUseCase().collectLatest { chats ->
                    _chatList.value = Resource.Success(chats)
                }
            } catch (e: Exception) {
                _chatList.value = Resource.Error(e)
            }
        }
    }

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                if (userId.isEmpty()) {
                    _chatList.value = Resource.Error(Throwable("User Id is not valid!"))
                    return@launch
                }

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
            val receiver = fetchUserProfileUseCase(receiverId)?.displayName() ?: "Unknown User"

            val message = Message(
                messageId = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = senderId,
                receiverId = receiverId,
                text = message,
                timestamp = Timestamp.now()
            )

            sendMessageUseCase(message, receiver)
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatsListener?.remove()
        messageListener?.remove()
    }
}