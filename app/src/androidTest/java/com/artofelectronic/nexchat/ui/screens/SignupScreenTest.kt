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
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artofelectronic.nexchat.ui.components.AuthProvider
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.utils.INVALID_EMAIL
import com.artofelectronic.nexchat.utils.INVALID_PASSWORD
import com.artofelectronic.nexchat.utils.VALID_EMAIL
import com.artofelectronic.nexchat.utils.VALID_PASSWORD
import com.artofelectronic.nexchat.utils.createTestNavHostController
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SignupScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navHostController: NavHostController


    @Before
    fun setup() {
        navHostController = createTestNavHostController(composeTestRule, Screens.SignUp.route)
    }

    @Test
    fun testIfTopLogoIsAvailable() {
        composeTestRule.onNodeWithContentDescription("Top Image")
            .assertIsDisplayed()
            .assertWidthIsEqualTo(180.dp)
            .assertHeightIsEqualTo(180.dp)
    }

    @Test
    fun testIfUiElementsAreAvailable() {
        // Check if 'or Sign In with' divider is available
        composeTestRule.onNodeWithText("or Sign In with").assertIsDisplayed()

        // Verify that the "Already have an account?" text is displayed
        composeTestRule.onNodeWithText("Already have an account?").assertIsDisplayed()

        // Verify that the Google, Facebook, and Twitter buttons are displayed and clickable
        composeTestRule.onNodeWithContentDescription(AuthProvider.Google.name)
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithContentDescription(AuthProvider.Facebook.name)
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithContentDescription(AuthProvider.Twitter.name)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun testInvalidEmailAndPasswordEntry() {
        composeTestRule.onNodeWithText("Email")
            .assertIsDisplayed()
            .performTextInput(INVALID_EMAIL)

        composeTestRule.onNodeWithText("Password")
            .assertIsDisplayed()
            .performTextInput(INVALID_PASSWORD)

        composeTestRule.onNodeWithText("Confirm Password")
            .assertIsDisplayed()
            .performTextInput(INVALID_PASSWORD)

        composeTestRule.onNodeWithText("Sign Up")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithText("Invalid email format")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Weak password (min 8 chars, 1 upper, 1 digit, 1 special)")
            .assertIsDisplayed()
    }

    @Test
    fun testEmailAndPasswordEntry() {
        composeTestRule.onNodeWithText("Email")
            .assertIsDisplayed()
            .performTextInput(VALID_EMAIL)

        composeTestRule.onNodeWithText("Password")
            .assertIsDisplayed()
            .performTextInput(VALID_PASSWORD)

        // Check if the password visibility toggle works (if applicable)
        composeTestRule.onNodeWithContentDescription("Visibility Icon Password")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        // Verify the password field is still present after toggling visibility
        composeTestRule.onNodeWithText(VALID_PASSWORD).assertIsDisplayed()



        composeTestRule.onNodeWithText("Confirm Password")
            .assertIsDisplayed()
            .performTextInput("Matin2018")

        // Check if the confirm visibility toggle works (if applicable)
        composeTestRule.onNodeWithContentDescription("Visibility Icon Confirm Password")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        // Verify the confirm field is still present after toggling visibility
        composeTestRule.onNodeWithText(VALID_PASSWORD).assertIsDisplayed()
    }

    @Test
    fun testNavigationToSignIn() {
        composeTestRule.onNodeWithText(" Sign In")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        assertEquals(
            Screens.SignIn.route,
            navHostController.currentBackStackEntry?.destination?.route
        )
    }
}