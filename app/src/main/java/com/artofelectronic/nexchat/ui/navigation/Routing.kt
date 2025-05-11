package com.artofelectronic.nexchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artofelectronic.nexchat.ui.AuthViewModel
import com.artofelectronic.nexchat.ui.screens.ForgotPasswordScreen
import com.artofelectronic.nexchat.ui.screens.HomeScreen
import com.artofelectronic.nexchat.ui.screens.SignInScreen
import com.artofelectronic.nexchat.ui.screens.SignupScreen
import com.artofelectronic.nexchat.ui.screens.StartScreen

@Composable
fun Routing(
    navController: NavHostController,
    viewModel: ViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Start.route) {
        composable(Screen.Start.route) {
            StartScreen(navController)
        }
        composable(Screen.SignUp.route) {
            SignupScreen(navController, viewModel as AuthViewModel)
        }
        composable(Screen.SignIn.route) {
            SignInScreen(navController, viewModel as AuthViewModel)
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController, viewModel as AuthViewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
    }
}