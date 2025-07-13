package com.artofelectronic.nexchat.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.artofelectronic.nexchat.ui.components.ChatListItem
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel
import com.artofelectronic.nexchat.utils.navigateToChat
import com.artofelectronic.nexchat.utils.navigateToUserList

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val userId by viewModel.userId.collectAsState()
    val chats by viewModel.chats.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchChats()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigateToUserList() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Chat")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(chats) { chat ->
                ChatListItem(
                    chat = chat,
                    currentUserId = userId,
                    onChatClick = { navController.navigateToChat(chat.chatId) }
                )

                HorizontalDivider()
            }
        }
    }
}