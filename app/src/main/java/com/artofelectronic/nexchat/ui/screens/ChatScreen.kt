package com.artofelectronic.nexchat.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.components.ListOfMessages
import com.artofelectronic.nexchat.ui.components.SendMessageBox
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String?,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadChatInformation(chatId.orEmpty())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.chat_with, uiState.name)
                    )
                }
            )
        },
        bottomBar = {
            SendMessageBox {
                viewModel.sendTextMessage(
                    chatId = chatId.orEmpty(),
                    senderId = viewModel.getUserId().orEmpty(),
                    text = it
                )
            }
        }
    ) { paddingValues ->
        ListOfMessages(paddingValues, messages)
    }
}