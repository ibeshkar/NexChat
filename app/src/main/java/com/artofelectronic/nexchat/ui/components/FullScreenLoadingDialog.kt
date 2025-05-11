package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.artofelectronic.nexchat.ui.theme.DarkerGreen

@Composable
fun FullScreenLoadingDialog() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(enabled = false) {}
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = DarkerGreen)
    }
}
