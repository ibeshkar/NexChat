package com.artofelectronic.nexchat.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.components.ChatListItem
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.components.RetryLayout
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToChat
import com.artofelectronic.nexchat.utils.Resource

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val chatState by viewModel.chatList.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    val otherUserId = remember { mutableStateOf("") }
    val otherUser by viewModel.userProfile.collectAsState()

    LaunchedEffect(currentUserId) {
        viewModel.observeChats(currentUserId)
        viewModel.fetchUserProfile(otherUserId.value)
    }

    when (val state = chatState) {
        is Resource.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.data) { chat ->
                    otherUserId.value = chat.participants.firstOrNull { it != currentUserId }.orEmpty()

                    ChatListItem(
                        chat = chat,
                        otherUser = otherUser,
                        onChatClick = { navController.navigateToChat(chat.chatId) }
                    )
                }
            }
        }

        is Resource.Error -> {
            RetryLayout(
                errorMessage = state.throwable.message ?: stringResource(R.string.unknown_error),
                onClick = viewModel.refreshChats(currentUserId)
            )
        }

        is Resource.Loading -> {
            FullScreenLoadingDialog()
        }
    }
}