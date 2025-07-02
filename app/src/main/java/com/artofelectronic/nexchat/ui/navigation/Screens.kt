package com.artofelectronic.nexchat.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val label: String = "", val icon: ImageVector? = null) {
    data object Start : Screens("start")
    data object SignUp : Screens("signUp")
    data object SignIn : Screens("signIn")
    data object ForgotPassword : Screens("forgotPassword")
    data object Home : Screens("home")
    data object Chats : Screens("chats", "Chats", Icons.AutoMirrored.Filled.Chat)
    data object Profile : Screens("profile", "Profile", Icons.Default.Person)
}