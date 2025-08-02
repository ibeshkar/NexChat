package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.utils.toVerboseDate

@Composable
fun ChatListItem(
    chat: Chat,
    otherUser: User?,
    onChatClick: (String) -> Unit
) {

    val verboseDate by remember { mutableStateOf(chat.lastUpdated.toVerboseDate()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onChatClick(chat.chatId)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        CircleAvatar(
            imageUrl = otherUser?.avatarUrl.orEmpty(),
            userName = otherUser?.displayName,
            size = 60.dp,
            contentDescription = "${chat.chatId}'s avatar",
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = otherUser?.displayName.orEmpty(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 80.dp)
                )

                Text(
                    text = if (chat.lastMessage.isNullOrEmpty())
                        stringResource(R.string.no_messages_yet_caption)
                    else chat.lastMessage,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }

            Text(
                text = verboseDate,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}