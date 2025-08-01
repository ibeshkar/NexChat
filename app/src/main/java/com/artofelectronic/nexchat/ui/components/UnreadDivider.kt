package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UnreadDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        Text(
            text = "unread messages",
            modifier = Modifier
                .padding(horizontal = 8.dp),
            color = Color.Gray,
            style = MaterialTheme.typography.labelMedium
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
    }
}

@Preview
@Composable
fun UnreadDividerPreview() {
    UnreadDivider()
}