package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.artofelectronic.nexchat.R

@Composable
fun SocialButton(provider: AuthProvider, onClick: () -> Unit) {
    val iconRes = when (provider) {
        AuthProvider.Google -> R.drawable.ic_google
        AuthProvider.Facebook -> R.drawable.ic_facebook
        AuthProvider.Twitter -> R.drawable.ic_twitter
    }

    val onClickListener = remember {
        { onClick() }
    }

    Button(
        onClick = onClickListener,
        modifier = Modifier.size(60.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = provider.name,
            tint = Color.White
        )
    }
}

enum class AuthProvider { Google, Facebook, Twitter }