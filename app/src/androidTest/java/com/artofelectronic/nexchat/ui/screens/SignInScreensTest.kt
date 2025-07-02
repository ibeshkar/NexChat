package com.artofelectronic.nexchat.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artofelectronic.nexchat.FakeSignInRepository
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.TestAuthViewModel
import com.artofelectronic.nexchat.ui.components.AuthProvider
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.state.SignupState
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreensTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        viewModel = createMockViewModel()
    }


    @Test
    fun testSignInScreenUI() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screens.SignIn.route) {
                composable(Screens.SignIn.route) {
                    SignInScreen(navController, viewModel)
                }
            }
        }

        // Verify that the top image is displayed with the correct size
        composeTestRule.onNodeWithContentDescription("Top Image")
            .assertWidthIsEqualTo(200.dp)
            .assertHeightIsEqualTo(200.dp)

        // Verify that the email and password fields are displayed
        composeTestRule.onNodeWithText("Email")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Password")
            .assertIsDisplayed()

        // Verify that the forgot password link is displayed and clickable
        composeTestRule.onNodeWithText("Forgot password?")
            .assertIsDisplayed()
            .assertHasClickAction()

        // Verify that the sign in button is displayed and clickable
        composeTestRule.onNodeWithText("Sign In")
            .assertIsDisplayed()
            .assertHasClickAction()

        // Verify that the "Have no account" text is displayed
        composeTestRule.onNodeWithText("Have no account?")
            .assertIsDisplayed()

        // Verify that the sign up link is displayed and clickable
        composeTestRule.onNodeWithText(" Sign up")
            .assertIsDisplayed()
            .assertHasClickAction()

        // Verify that the "or Sign In with" text is displayed
        composeTestRule.onNodeWithText("or Sign In with")
            .assertIsDisplayed()

        // Verify that the Google, Facebook, and Twitter buttons are displayed and clickable
        composeTestRule.onNodeWithContentDescription(AuthProvider.Google.name)
            .assertHasClickAction()
        composeTestRule.onNodeWithContentDescription(AuthProvider.Twitter.name)
            .assertHasClickAction()
        composeTestRule.onNodeWithContentDescription(AuthProvider.Facebook.name)
            .assertHasClickAction()
    }

    @Test
    fun testInvalidEmailAndPasswordEntry() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screens.SignIn.route) {
                composable(Screens.SignIn.route) {
                    SignInScreen(navController, viewModel)
                }
            }
        }

        // Enter invalid email and password
        val invalidEmail = "invalidemail"
        val invalidPassword = "123"

        composeTestRule.onNodeWithText("Email")
            .performTextInput(invalidEmail)
        composeTestRule.onNodeWithText("Password")
            .performTextInput(invalidPassword)

        // Click the Sign In button
        composeTestRule.onNodeWithText("Sign In").performClick()

        // Assert error messages are shown
        composeTestRule.onNodeWithText("Invalid email format")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Weak password (min 6 chars, 1 upper, 1 digit, 1 special)")
            .assertIsDisplayed()
    }

    @Test
    fun testEmailAndPasswordEntry() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screens.SignIn.route) {
                composable(Screens.SignIn.route) {
                    SignInScreen(navController, viewModel)
                }
            }
        }

        // Enter valid email and password
        val testEmail = "test@example.com"
        val testPassword = "password123"

        composeTestRule.onNodeWithText("Email")
            .assertIsDisplayed()
            .performTextInput(testEmail)

        composeTestRule.onNodeWithText("Password")
            .assertIsDisplayed()
            .performTextInput(testPassword)

        // Assert that the text fields contain the entered text
        composeTestRule.onNodeWithText(testEmail).assertIsDisplayed()

        composeTestRule.onNodeWithTag("PasswordField").assertIsDisplayed()

        // Check if the password visibility toggle works (if applicable)
        composeTestRule.onNodeWithContentDescription("Visibility Icon")
            .assertExists()
            .performClick()

        // Verify the password field is still present after toggling visibility
        composeTestRule.onNodeWithTag("PasswordField").assertIsDisplayed()
    }

    @Test
    fun testNavigationToSignUp() {
        val navController = createTestNavHostController(composeTestRule, Screens.SignIn.route)

        // Click "Sign up" text
        composeTestRule.onNodeWithText(" Sign up").performClick()

        // Check if the navigation was triggered to SignUp screen
        assertEquals(Screens.SignUp.route, navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun testSuccessfulSignInNavigation() {
        val fakeRepository = FakeSignInRepository()
        fakeRepository.setSignedInStatus(true)
        val fakeViewModel = TestAuthViewModel(repository = fakeRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screens.SignIn.route) {
                composable(Screens.SignIn.route) {
                    SignInScreen(navController, fakeViewModel)
                }
                composable(Screens.Home.route) {
                    HomeScreen(navController)
                }
            }
        }

        // Enter valid credentials
        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("Taha1391!")
        composeTestRule.onNodeWithText("Sign In").performClick()

        // Wait for state update
        composeTestRule.waitForIdle()

        // Assert the state is success after clicking sign-in
        assert(fakeViewModel.signInState.value is SignupState.Success)
    }

    @Test
    fun testSignInWithEmailFailure() {
        val fakeRepository = FakeSignInRepository()
        fakeRepository.setSignedInStatus(false)
        val fakeViewModel = TestAuthViewModel(repository = fakeRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screens.SignIn.route) {
                composable(Screens.SignIn.route) {
                    SignInScreen(navController, fakeViewModel)
                }
            }
        }

        // Perform user actions
        composeTestRule.onNodeWithText("Email").performTextInput("wrong@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("wrongpass")
        composeTestRule.onNodeWithText("Sign In").performClick()

        // Wait for UI update
        composeTestRule.waitForIdle()

        // Verify error state
        assert(fakeViewModel.signInState.value is SignupState.Error)
    }
}