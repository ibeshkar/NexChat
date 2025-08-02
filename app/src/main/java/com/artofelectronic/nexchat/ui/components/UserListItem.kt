package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.artofelectronic.nexchat.domain.model.User

@Composable
fun UserListItem(user: User, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CircleAvatar(
            imageUrl = user.avatarUrl,
            userName = user.displayName,
            size = 60.dp
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = user.displayName.ifEmpty { user.email },
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(end = 80.dp)
        )
    }
}