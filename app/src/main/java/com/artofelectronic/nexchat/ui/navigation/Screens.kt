package com.artofelectronic.nexchat.ui.navigation

sealed class Screen(val route: String) {
    data object Start : Screen("start")
    data object SignUp : Screen("signUp")
    data object SignIn : Screen("signIn")
    data object ForgotPassword : Screen("forgotPassword")
    data object Home : Screen("home")
}