package com.artofelectronic.nexchat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.artofelectronic.nexchat.ui.components.ChatScreenContent
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel
import com.artofelectronic.nexchat.utils.generateChatId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String?,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val messages by viewModel.messages.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val receiverId = chatId?.split("_")?.find { it != currentUserId }.orEmpty()
    val chatId = chatId ?: generateChatId(currentUserId, receiverId)


    LaunchedEffect(chatId) {
        viewModel.observeMessages(chatId.orEmpty())
    }

    ChatScreenContent(
        messages = messages,
        currentUserId = currentUserId
    ) { message ->
        viewModel.sendMessage(
            chatId = chatId.orEmpty(),
            message = message,
            senderId = currentUserId.orEmpty(),
            receiverId = receiverId
        )
    }
}