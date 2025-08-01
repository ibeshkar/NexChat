package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.artofelectronic.nexchat.utils.getColorFromName

@Composable
fun CircleAvatar(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    userName: String? = null,
    size: Dp = 50.dp,
    contentDescription: String? = "User avatar",
    onPhotoPick: (() -> Unit)? = null
) {

    val context = LocalContext.current
    var imageFailed by remember { mutableStateOf(false) }

    val backgroundColor: Color = userName?.getColorFromName() ?: Color.Transparent

    val initial =
        userName?.trim()?.split(" ")?.take(2)?.mapNotNull { it.firstOrNull()?.uppercase() }
            ?.joinToString("") ?: "?"

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = onPhotoPick != null) { onPhotoPick?.invoke() },
        contentAlignment = Alignment.Center
    ) {

        if (!imageFailed && !imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .listener(
                        onError = { _, _ ->
                            imageFailed = true
                        })
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .matchParentSize()
                    .clip(CircleShape)
            )
        }

        if (imageUrl.isNullOrBlank() || imageFailed) {
            Text(
                text = initial,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun AvatarPreview() {
    CircleAvatar(
        size = 200.dp,
        imageUrl = "http://"
    )
}