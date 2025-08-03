package com.artofelectronic.nexchat.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.utils.createTestNavHostController
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navHostController: NavHostController


    @Before
    fun setup() {
        navHostController = createTestNavHostController(composeTestRule, Screens.Start.route)
    }

    @Test
    fun testIfImageLogoIsAvailable() {
        composeTestRule.onNodeWithContentDescription("Start Screen Image")
            .assertIsDisplayed()
            .assertWidthIsEqualTo(250.dp)
            .assertHeightIsEqualTo(250.dp)
    }


    @Test
    fun testIfTitleAndDescriptionAreAvailable() {
        composeTestRule.onNodeWithText("Welcome to NEXChat")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Connect with friends and family anytime, anywhere.")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Sign In")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule.onNodeWithText("Sign Up")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun testNavigationToSignInScreen() {
        composeTestRule.onNodeWithText("Sign In").performClick()
        assertEquals(
            Screens.SignIn.route,
            navHostController.currentBackStackEntry?.destination?.route
        )
    }

    @Test
    fun testNavigationToSignUpScreen() {
        composeTestRule.onNodeWithText("Sign Up").performClick()
        assertEquals(
            Screens.SignUp.route,
            navHostController.currentBackStackEntry?.destination?.route
        )
    }
}