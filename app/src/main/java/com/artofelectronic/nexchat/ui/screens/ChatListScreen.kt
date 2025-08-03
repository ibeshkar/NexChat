package com.artofelectronic.nexchat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.artofelectronic.nexchat.ui.components.ChatListItem
import com.artofelectronic.nexchat.ui.components.EmptyStateLayout
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.components.RetryLayout
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToChat
import com.artofelectronic.nexchat.utils.Resource

@Composable
fun ChatListScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = hiltViewModel()
) {

    val chatListState by chatViewModel.chatList.collectAsState()
    val currentUserId = remember { chatViewModel.currentUserId }

    val otherUserId = remember { mutableStateOf("") }
    val otherUser by chatViewModel.userProfile.collectAsState()

    LaunchedEffect(otherUserId) {
        chatViewModel.fetchUserProfile(otherUserId.value)
    }

    when (val state = chatListState) {

        is Resource.Success -> {

            if (state.data.isEmpty()) {
                EmptyStateLayout()
                return
            }

            LazyColumn(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
                items(state.data) { chat ->
                    otherUserId.value =
                        chat.participants.firstOrNull { it != currentUserId }.orEmpty()

                    ChatListItem(
                        chat = chat,
                        avatarUrl = otherUser?.avatarUrl.orEmpty(),
                        onChatClick = { navController.navigateToChat(chat.chatId) }
                    )
                }
            }
        }

        is Resource.Error -> RetryLayout(onClick = { chatViewModel.observeChats() })
        is Resource.Loading -> FullScreenLoadingDialog()
    }
}