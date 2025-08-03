package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.utils.toTimeOnly

@Composable
fun MessageBubble(
    message: Message,
    isMine: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
    ) {

        Surface(
            modifier = Modifier.padding(vertical = 5.dp),
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isMine) 12.dp else 0.dp,
                bottomEnd = if (!isMine) 12.dp else 0.dp
            ),
            color = if (isMine) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.onSecondary,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier
                .widthIn(max = 250.dp)
                .padding(8.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message.timestamp.toTimeOnly(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}