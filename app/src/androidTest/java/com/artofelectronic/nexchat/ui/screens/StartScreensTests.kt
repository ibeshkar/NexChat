//package com.artofelectronic.nexchat.ui.screens
//
//import androidx.activity.ComponentActivity
//import androidx.compose.ui.test.assertHasClickAction
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.navigation.testing.TestNavHostController
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.artofelectronic.nexchat.ui.navigation.Screens
//import junit.framework.TestCase.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class StartScreensTests {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var navController: TestNavHostController
//
//    @Before
//    fun setup() {
//        navController = createTestNavHostController(composeTestRule)
//    }
//
//    @Test
//    fun testStartScreenUI() {
//        // Verify welcome text
//        composeTestRule.onNodeWithText("Welcome to NEXChat")
//            .assertIsDisplayed()
//
//        // Verify description text
//        composeTestRule.onNodeWithText("Connect with friends and family anytime, anywhere.")
//            .assertIsDisplayed()
//
//        // Check Sign In button presence and click action
//        composeTestRule.onNodeWithText("Sign In")
//            .assertIsDisplayed()
//            .assertHasClickAction()
//
//        // Check Sign Up button presence and click action
//        composeTestRule.onNodeWithText("Sign Up")
//            .assertIsDisplayed()
//            .assertHasClickAction()
//    }
//
//    @Test
//    fun testNavigationToSignIn() {
//        composeTestRule.onNodeWithText("Sign In").performClick()
//        assertEquals(Screens.SignIn.route, navController.currentBackStackEntry?.destination?.route)
//    }
//
//    @Test
//    fun testNavigationToSignUp() {
//        composeTestRule.onNodeWithText("Sign Up").performClick()
//        assertEquals(Screens.SignUp.route, navController.currentBackStackEntry?.destination?.route)
//    }
//}