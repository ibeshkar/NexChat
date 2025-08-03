package com.artofelectronic.nexchat.utils


import androidx.compose.ui.graphics.Color
import com.artofelectronic.nexchat.domain.model.User
import kotlin.math.absoluteValue
import kotlin.text.ifEmpty


fun String.getColorFromName(): Color {
    val colors = listOf(
        Color(0xFFB3E5FC), // Light Blue
        Color(0xFFFFF9C4), // Light Yellow
        Color(0xFFC8E6C9), // Light Green
        Color(0xFFFFCCBC), // Light Orange
        Color(0xFFD1C4E9), // Light Purple
        Color(0xFFFFCDD2)  // Light Red
    )

    val index = (this.hashCode().absoluteValue) % colors.size
    return colors[index]
}

fun User.displayName(): String? = displayName
    .ifEmpty { email.substringBefore("@") }
    .ifEmpty { "User ${userId.take(3)}..." }