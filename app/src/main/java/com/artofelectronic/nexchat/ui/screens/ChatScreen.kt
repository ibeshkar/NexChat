package com.artofelectronic.nexchat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.artofelectronic.nexchat.ui.components.ChatScreenContent
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel
import com.artofelectronic.nexchat.utils.generateChatId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    chatId: String?,
    chatViewModel: ChatViewModel = hiltViewModel()
) {

    val messages by chatViewModel.messages.collectAsState()
    val currentUserId = remember { chatViewModel.currentUserId }

    val receiverId = chatId?.split("_")?.find { it != currentUserId }.orEmpty()
    val chatId = chatId ?: generateChatId(currentUserId, receiverId)


    LaunchedEffect(chatId) {
        chatViewModel.observeMessages(chatId.orEmpty())
    }

    ChatScreenContent(
        messages = messages,
        currentUserId = currentUserId
    ) { message ->
        chatViewModel.sendMessage(
            chatId = chatId.orEmpty(),
            message = message,
            senderId = currentUserId.orEmpty(),
            receiverId = receiverId
        )
    }
}