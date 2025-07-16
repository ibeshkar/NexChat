package com.artofelectronic.nexchat.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artofelectronic.nexchat.data.models.ChatRoom
import com.artofelectronic.nexchat.data.models.Chat
import com.artofelectronic.nexchat.data.models.Message
import com.artofelectronic.nexchat.data.models.User
import com.artofelectronic.nexchat.domain.usecases.FetchChatsUseCase
import com.artofelectronic.nexchat.domain.usecases.GetInitialChatRoomInformation
import com.artofelectronic.nexchat.domain.usecases.GetUserDataUseCase
import com.artofelectronic.nexchat.domain.usecases.LoadConversationsUseCase
import com.artofelectronic.nexchat.domain.usecases.LoadUsersUseCase
import com.artofelectronic.nexchat.domain.usecases.RetrieveMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.SendImageMessageUseCase
import com.artofelectronic.nexchat.domain.usecases.SendTextMessageUseCase
import com.artofelectronic.nexchat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getUserData: GetUserDataUseCase,
    private val getAllUsers: LoadUsersUseCase,
    private val loadAllConversations: LoadConversationsUseCase,
    private val retrieveMessages: RetrieveMessagesUseCase,
    private val sendMessage: SendTextMessageUseCase,
    private val getInitialChatRoomInformation: GetInitialChatRoomInformation,
    private val fetchChatsUseCase: FetchChatsUseCase,
    private val sendTextMessageUseCase: SendTextMessageUseCase,
    private val sendImageMessageUseCase: SendImageMessageUseCase,
) : ViewModel() {

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _chats = MutableStateFlow<Resource<List<Chat>>>(Resource.Loading)
    val chats: StateFlow<Resource<List<Chat>>> = _chats


    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _conversations = MutableStateFlow<List<Chat>>(emptyList())
    val conversations: StateFlow<List<Chat>> = _conversations

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _uiState = MutableStateFlow(ChatRoom())
    val uiState: StateFlow<ChatRoom> = _uiState

    private var messageCollectionJob: Job? = null

    private lateinit var chatRoom: ChatRoom


    init {
        observeUserId()
        fetchChats()
    }

    private fun observeUserId() {
        viewModelScope.launch {
            _userId.value = getUserData()?.uid.orEmpty()
        }
    }

    fun fetchChats() {
        viewModelScope.launch {
            fetchChatsUseCase(userId.value).collect { chats ->
                _chats.value = chats
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _users.value = getAllUsers()
        }
    }

    /**
     * Get all conversations from the firestore collection.
     */
//    fun loadConversations(userId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                loadAllConversations(userId).addOnSuccessListener { querySnapshot ->
//                    val chats = querySnapshot.documents.mapNotNull { document ->
//                        document.toObject(Chat::class.java)
//                    }
//                    _conversations.value = chats
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    /**
     * Get initial chat room information.
     */
    fun loadChatInformation(chatRoomId: String) {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRoom = getInitialChatRoomInformation(chatRoomId)
                withContext(Dispatchers.Main) {
                    _uiState.value = chatRoom
                    _messages.value = chatRoom.messages
                    updateMessages()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun updateMessages() {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                getUserData()?.uid?.let {
                    retrieveMessages(chatId = chatRoom.id)
                        .collect { message ->
                            withContext(Dispatchers.Main) {
//                                _messages.value += message
                            }
                        }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

//    fun onSendMessage(messageText: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val user = getUserData() ?: return@launch
//            val message = Message(
//                senderId = user.uid,
//                senderAvatar = user.photoUrl?.path.orEmpty(),
//                senderName = user.displayName.orEmpty(),
//                isMine = true,
//                content = Content.TextMessage(messageText)
//            )
//            sendMessage(chatId = chatRoom.id, message = message)
//        }
//    }

    fun sendTextMessage(chatId: String, senderId: String, text: String) {
        val message = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            sendTextMessageUseCase(chatId, message)
        }
    }

    fun sendImageMessage(chatId: String, senderId: String, uri: Uri) {
        viewModelScope.launch {
            sendImageMessageUseCase(chatId, senderId, uri)
        }
    }


    fun searchUsers(query: String) {
        viewModelScope.launch {
            _users.value = getAllUsers().filter { it.name.contains(query, ignoreCase = true) }
        }
    }


    override fun onCleared() {
        messageCollectionJob?.cancel()
    }
}