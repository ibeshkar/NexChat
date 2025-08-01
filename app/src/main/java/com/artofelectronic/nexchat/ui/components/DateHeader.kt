package com.artofelectronic.nexchat.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.artofelectronic.nexchat.utils.toVerboseLabel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateHeader(date: LocalDate) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.LightGray.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = date.toVerboseLabel(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}