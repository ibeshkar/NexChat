package com.artofelectronic.nexchat.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.utils.toLocalDate
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreenContent(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    currentUserId: String?,
    onSendMessage: (String) -> Unit
) {

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()


    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_chat_screen),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(modifier = modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                state = listState,
                reverseLayout = false
            ) {

                val grouped = messages.groupBy { it.timestamp.toLocalDate() }

                grouped.toSortedMap(compareBy { it }).forEach { (date, dayMessages) ->
                    item {
                        DateHeader(date)
                    }

                    itemsIndexed(dayMessages) { index, message ->
                        MessageBubble(
                            message = message,
                            isMine = message.senderId == currentUserId
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.type_a_message_caption),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    },
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    maxLines = 4,
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText.trim())
                            messageText = ""
                        }
                    },
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(50)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ChatScreenContentPreview() {
    ChatScreenContent(
        messages = listOf(
            Message(messageId = "1", chatId = "1", text = "heyyyy!!"),
            Message(messageId = "2", chatId = "1", text = "how are you?"),
            Message(
                messageId = "3", chatId = "2", text = "I'm fine, thanks",
                timestamp = Timestamp(
                    Date(
                        Calendar.getInstance().apply {
                            set(Calendar.YEAR, 2023)
                            set(Calendar.MONTH, 1)
                            set(Calendar.DAY_OF_MONTH, 1)
                        }.timeInMillis
                    )
                )
            ),
            Message(
                messageId = "4",
                chatId = "2",
                text = "how about you? eger  ertrewt wer tew rt wert wer t er erw t wert ewrt ewr t ",
                timestamp = Timestamp(
                    Date(
                        Calendar.getInstance().apply {
                            set(Calendar.DAY_OF_YEAR, -1)
                        }.timeInMillis
                    )
                ),
                senderId = "User1"
            ),
            Message(messageId = "1", chatId = "1", text = "heyyyy!!"),
            Message(messageId = "2", chatId = "1", text = "how are you?"),
            Message(
                messageId = "3",
                chatId = "2",
                text = "I'm fine, thanks, sjhdgfjhsdgfmnsdvfbnmsavdfnmbavsdfnbmvasdnbfmvasnbdfvsand s ansbfdvmansbdfvamsnbdvfyhxcgmyhxmynbvxcnmybvxcnybxvcmnybxvc",
                timestamp = Timestamp(
                    Date(
                        Calendar.getInstance().apply {
                            set(Calendar.YEAR, 2023)
                            set(Calendar.MONTH, 1)
                            set(Calendar.DAY_OF_MONTH, 1)
                        }.timeInMillis
                    )
                )
            ),
            Message(
                messageId = "4", chatId = "2", text = "how about you?", timestamp = Timestamp(
                    Date(
                        Calendar.getInstance().apply {
                            set(Calendar.DAY_OF_YEAR, -1)
                        }.timeInMillis
                    )
                ),
                senderId = "User1"
            )
        ),
        currentUserId = "User1"
    ) { }
}