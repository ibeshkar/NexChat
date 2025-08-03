package com.artofelectronic.nexchat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Light Color Scheme
val NEXChatLightColorScheme = lightColorScheme(
    primary = NEXChatLightPrimary,
    onPrimary = NEXChatLightOnPrimary,
    secondary = NEXChatLightSecondary,
    onSecondary = NEXChatLightOnSecondary,
    background = NEXChatLightBackground,
    onBackground = NEXChatLightOnBackground,
    surface = NEXChatLightSurface,
    onSurface = NEXChatLightOnBackground,
    outline = NEXChatLightOutline,
    tertiary = NEXChatLightTertiary,
    surfaceContainer = NEXChatLightOutgoingMsg
)

// Dark Color Scheme
val NEXChatDarkColorScheme = darkColorScheme(
    primary = NEXChatDarkPrimary,
    onPrimary = NEXChatDarkOnPrimary,
    secondary = NEXChatDarkSecondary,
    onSecondary = NEXChatDarkOnSecondary,
    background = NEXChatDarkBackground,
    onBackground = NEXChatDarkOnBackground,
    surface = NEXChatDarkSurface,
    onSurface = NEXChatDarkOnBackground,
    outline = NEXChatDarkOutline,
    tertiary = NEXChatDarkTertiary,
    surfaceContainer = NEXChatDarkOutgoingMsg
)

@Composable
fun NexChatTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) NEXChatDarkColorScheme else NEXChatLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}