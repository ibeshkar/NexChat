package com.artofelectronic.nexchat.ui.navigation

sealed class Screens(val route: String) {

    // ────── Auth Screens ──────
    data object Start : Screens("start")
    data object SignUp : Screens("signUp")
    data object SignIn : Screens("signIn")
    data object ForgotPassword : Screens("forgotPassword")

    // ────── Main Navigation (Bottom Nav) ──────
    data object Home : Screens("home")
    data object Chats : Screens("chats")
    data object Profile : Screens("profile")
    data object Users : Screens("users")

    // ────── Detail / Dynamic Screens ──────
    data object Chat : Screens("chat/{chatId}") {
        fun createRoute(chatId: String) = "chat/$chatId"
        const val ARG_CHAT_ID = "chatId"
    }
}