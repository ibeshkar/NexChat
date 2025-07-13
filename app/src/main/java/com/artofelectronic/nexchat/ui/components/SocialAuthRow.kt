package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SocialAuthRow(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onTwitterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SocialButton(provider = AuthProvider.Google, onClick = onGoogleClick)
        SocialButton(provider = AuthProvider.Facebook, onClick = onFacebookClick)
        SocialButton(provider = AuthProvider.Twitter, onClick = onTwitterClick)
    }
}