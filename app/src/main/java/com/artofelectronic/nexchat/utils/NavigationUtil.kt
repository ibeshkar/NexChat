package com.artofelectronic.nexchat.utils

import androidx.navigation.NavController
import com.artofelectronic.nexchat.ui.navigation.Screens

object NavigationUtil {

    fun NavController.navigateToStart() {
        this.navigate(Screens.Start.route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun NavController.navigateToSignup() {
        this.navigate(Screens.SignUp.route) {
            popUpTo(Screens.SignIn.route) { inclusive = true }
        }
    }

    fun NavController.navigateToSignIn() {
        this.navigate(Screens.SignIn.route)
    }

    fun NavController.navigateToForgotPassword() {
        this.navigate(Screens.ForgotPassword.route)
    }

    fun NavController.navigateToChats() {
        this.navigate(Screens.Chats.route) {
            popUpTo(Screens.Start.route) { inclusive = true }
        }
    }

    fun NavController.navigateToChat(chatId: String) {
        this.navigate(Screens.Chat.createRoute(chatId)) {
            popUpTo(Screens.Chats.route) { inclusive = false }
        }
    }

    fun NavController.navigateToUserList() {
        this.navigate(Screens.Users.route)
    }

}