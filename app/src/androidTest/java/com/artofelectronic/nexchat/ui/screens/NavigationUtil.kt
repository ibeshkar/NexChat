//package com.artofelectronic.nexchat.ui.screens
//
//import androidx.activity.ComponentActivity
//import androidx.compose.ui.test.junit4.AndroidComposeTestRule
//import androidx.navigation.compose.ComposeNavigator
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.testing.TestNavHostController
//import com.artofelectronic.nexchat.data.repository.FirebaseAuthRepository
//import com.artofelectronic.nexchat.domain.usecases.CheckUserLoggedInUseCase
//import com.artofelectronic.nexchat.domain.usecases.SendPasswordResetEmailUseCase
//import com.artofelectronic.nexchat.domain.usecases.SignInWithEmailUseCase
//import com.artofelectronic.nexchat.domain.usecases.SignInWithFacebookUseCase
//import com.artofelectronic.nexchat.domain.usecases.SignInWithGoogleUseCase
//import com.artofelectronic.nexchat.domain.usecases.SignInWithTwitterUseCase
//import com.artofelectronic.nexchat.domain.usecases.SignupWithEmailUseCase
//import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
//import com.artofelectronic.nexchat.ui.navigation.Screens
//import com.artofelectronic.nexchat.utils.FirebaseUtil
//import io.mockk.coEvery
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.mockkObject
//
//
//fun createTestNavHostController(
//    composeTestRule: AndroidComposeTestRule<*, ComponentActivity>,
//    startDestination: String = Screens.Start.route
//): TestNavHostController {
//
//    val navController = TestNavHostController(composeTestRule.activity)
//
//    val mockViewModel = createMockViewModel()
//
//    composeTestRule.setContent {
//        navController.navigatorProvider.addNavigator(ComposeNavigator())
//        // Setting up the navigation host
//        NavHost(
//            navController = navController,
//            startDestination = startDestination
//        ) {
//            composable(Screens.Start.route) { StartScreen(navController) }
//            composable(Screens.SignIn.route) { SignInScreen(navController, mockViewModel) }
//            composable(Screens.SignUp.route) { SignupScreen(navController, mockViewModel) }
//            composable(Screens.Home.route) { HomeScreen(navController) }
//        }
//    }
//    return navController
//}
//
//fun createMockViewModel(): AuthViewModel {
//    // Mocking FirebaseUtil
//    mockkObject(FirebaseUtil)
//
//    // Setting up the behavior of FirebaseUtil
//    every { FirebaseUtil.isUserSignedIn() } returns true
//
//    // Mocking the repository that uses FirebaseUtil
//    val signInRepository = mockk<FirebaseAuthRepository>()
//    coEvery { signInRepository.isUserSignedIn() } returns true
//
//    // Creating use case with mocked repository
//    val checkUserLoggedInUseCase = CheckUserLoggedInUseCase(signInRepository)
//    val signupWithEmailUseCase = mockk<SignupWithEmailUseCase>()
//    val signInWithGoogleUseCase = mockk<SignInWithGoogleUseCase>()
//    val signInWithTwitterUseCase = mockk<SignInWithTwitterUseCase>()
//    val signInWithFacebook = mockk<SignInWithFacebookUseCase>()
//    val signInWithEmailUseCase = mockk<SignInWithEmailUseCase>()
//    val sendPasswordResetEmailUseCase = mockk<SendPasswordResetEmailUseCase>()
//
//    return AuthViewModel(
//        checkUserLoggedInUseCase,
//        signupWithEmailUseCase,
//        signInWithGoogleUseCase,
//        signInWithTwitterUseCase,
//        signInWithFacebook,
//        signInWithEmailUseCase,
//        sendPasswordResetEmailUseCase
//    )
//}