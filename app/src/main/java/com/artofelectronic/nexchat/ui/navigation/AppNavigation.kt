package com.artofelectronic.nexchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.artofelectronic.nexchat.ui.screens.ChatScreen
import com.artofelectronic.nexchat.ui.screens.ChatListScreen
import com.artofelectronic.nexchat.ui.screens.ForgotPasswordScreen
import com.artofelectronic.nexchat.ui.screens.ProfileScreen
import com.artofelectronic.nexchat.ui.screens.SignInScreen
import com.artofelectronic.nexchat.ui.screens.SignupScreen
import com.artofelectronic.nexchat.ui.screens.StartScreen
import com.artofelectronic.nexchat.ui.screens.UserListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // Auth Screens
        composable(Screens.Start.route) { StartScreen(navController) }
        composable(Screens.SignUp.route) { SignupScreen(navController) }
        composable(Screens.SignIn.route) { SignInScreen(navController) }
        composable(Screens.ForgotPassword.route) { ForgotPasswordScreen(navController) }


        // Main Screens
        composable(Screens.Chats.route) { ChatListScreen(navController) }
        composable(Screens.Users.route) { UserListScreen(navController) }
        composable(Screens.Profile.route) { ProfileScreen(navController) }


        // Chat Detail Screen
        composable(
            route = Screens.Chat.route,
            arguments = listOf(navArgument(Screens.Chat.ARG_CHAT_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString(Screens.Chat.ARG_CHAT_ID)
            ChatScreen(chatId ?: "")
        }
    }
}