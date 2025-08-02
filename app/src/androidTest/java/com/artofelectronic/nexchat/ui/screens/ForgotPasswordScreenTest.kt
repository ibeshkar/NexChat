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
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.utils.INVALID_EMAIL
import com.artofelectronic.nexchat.utils.createTestNavHostController
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ForgotPasswordScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setup() {
        navHostController =
            createTestNavHostController(
                composeTestRule,
                Screens.ForgotPassword.route
            )
    }


    @Test
    fun testIfLogoIsAvailable() {
        composeTestRule.onNodeWithContentDescription("Top Image")
            .assertIsDisplayed()
            .assertWidthIsEqualTo(130.dp)
            .assertHeightIsEqualTo(130.dp)
    }

    @Test
    fun testInvalidEmailEntry() {
        composeTestRule.onNodeWithText("Enter your email to receive a reset link.")
            .assertIsDisplayed()
            .performTextInput(INVALID_EMAIL)

        composeTestRule.onNodeWithText("Submit")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithText("Invalid email format")
            .assertIsDisplayed()
    }

}