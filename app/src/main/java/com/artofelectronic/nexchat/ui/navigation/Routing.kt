package com.artofelectronic.nexchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.screens.ForgotPasswordScreen
import com.artofelectronic.nexchat.ui.screens.HomeScreen
import com.artofelectronic.nexchat.ui.screens.SignInScreen
import com.artofelectronic.nexchat.ui.screens.SignupScreen
import com.artofelectronic.nexchat.ui.screens.StartScreen

@Composable
fun Routing(
    navController: NavHostController,
    startDestination: String,
    viewModel: ViewModel
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screens.Start.route) {
            StartScreen(navController)
        }
        composable(Screens.SignUp.route) {
            SignupScreen(navController, viewModel as AuthViewModel)
        }
        composable(Screens.SignIn.route) {
            SignInScreen(navController, viewModel as AuthViewModel)
        }
        composable(Screens.ForgotPassword.route) {
            ForgotPasswordScreen(navController, viewModel as AuthViewModel)
        }
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
    }
}