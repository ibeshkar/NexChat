package com.artofelectronic.nexchat.utils

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.screens.ForgotPasswordScreen
import com.artofelectronic.nexchat.ui.screens.SignInScreen
import com.artofelectronic.nexchat.ui.screens.SignupScreen
import com.artofelectronic.nexchat.ui.screens.StartScreen


fun createTestNavHostController(
    composeTestRule: AndroidComposeTestRule<*, ComponentActivity>,
    startDestination: String
): TestNavHostController {

    val navHostController = TestNavHostController(composeTestRule.activity)

    val authViewModel = createMockAuthViewModel()

    composeTestRule.setContent {
        navHostController.navigatorProvider.addNavigator(ComposeNavigator())

        NavHost(
            navController = navHostController,
            startDestination = startDestination
        ) {
            composable(Screens.Start.route) { StartScreen(navHostController) }
            composable(Screens.SignIn.route) { SignInScreen(navHostController, authViewModel) }
            composable(Screens.SignUp.route) { SignupScreen(navHostController, authViewModel) }
            composable(Screens.ForgotPassword.route) { ForgotPasswordScreen(navHostController, authViewModel) }
        }
    }

    return navHostController
}