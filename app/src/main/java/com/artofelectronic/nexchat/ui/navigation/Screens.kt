package com.artofelectronic.nexchat.ui.navigation

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object SignUp : Screen("signUp")
    object SignIn : Screen("signIn")
}