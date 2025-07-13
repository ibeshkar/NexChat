package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.artofelectronic.nexchat.data.models.Chat
import com.artofelectronic.nexchat.ui.viewmodels.ChatViewModel
import com.artofelectronic.nexchat.utils.toVerboseDate

@Composable
fun ChatListItem(
    chat: Chat,
    currentUserId: String,
    onChatClick: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val verboseDate by remember { mutableStateOf(chat.lastUpdated.toVerboseDate()) }

    val otherUserId = chat.participants.firstOrNull { it != currentUserId } ?: return
    val otherUser = viewModel.users.value.firstOrNull { it.id == otherUserId } ?: return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onChatClick(chat.chatId)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Avatar(
            imageUrl = otherUser.avatar,
            size = 50.dp,
            contentDescription = "${otherUser.name}'s avatar",
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = otherUser.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
            Text(
                text = chat.lastMessage ?: "No messages yet",
                maxLines = 1,
                softWrap = true,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = verboseDate,
                textAlign = TextAlign.End
            )
        }
    }
}