package com.artofelectronic.nexchat.ui.screens

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.artofelectronic.nexchat.ui.components.ChatListItem
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel
import com.artofelectronic.nexchat.utils.Resource
import com.artofelectronic.nexchat.utils.navigateToChat

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

    when (val state = chats) {
        is Resource.Success -> {
            LazyColumn {
                items(state.data) { chat ->
                    ChatListItem(
                        chat = chat,
                        currentUserId = userId,
                        onChatClick = { navController.navigateToChat(chat.chatId) }
                    )

                    HorizontalDivider()
                }
            }
        }

        is Resource.Error -> {
            Toast.makeText(LocalContext.current, state.message, Toast.LENGTH_LONG).show()
        }

        is Resource.Loading -> {
            FullScreenLoadingDialog()
        }
    }


}