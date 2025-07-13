package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.artofelectronic.nexchat.data.models.Message
import com.artofelectronic.nexchat.utils.toVerboseDate

@Composable
fun MessageItem(message: Message) {

    val isMine = message.isMine

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        if (!isMine) {
            Avatar(
                imageUrl = message.imageUrl.orEmpty(),
                size = 40.dp,
                contentDescription = "${message.senderId}'s avatar"
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column {
            if (!isMine) {
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Text(
                    text = message.senderId,
                    fontWeight = FontWeight.Bold
                )
            }

            if (message.imageUrl != null) {
                AsyncImage(
                    model = message.imageUrl,
                    contentDescription = message.chatId,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                ) {
                    Text(
                        text = message.text ?: "...",
                        modifier = Modifier.padding(8.dp),
                        color = if (isMine) MaterialTheme.colorScheme.onPrimary else Color.White

                    )
                }
            }

            Text(
                text = message.timestamp.toVerboseDate(),
                fontSize = 12.sp
            )
        }
    }
}