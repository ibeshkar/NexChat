package com.artofelectronic.nexchat.ui

import com.artofelectronic.nexchat.domain.usecases.CheckUserLoggedInUseCase
import com.artofelectronic.nexchat.domain.usecases.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithFacebookUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithGoogleUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithTwitterUseCase
import com.artofelectronic.nexchat.domain.usecases.SignupWithEmailUseCase
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk

class AuthViewModelTest {

//    private val signInRepository: FirebaseAuthRepository = mockk {
//        coEvery { isUserSignedIn() } returns true
//    }
//
//    private val signUpRepository: SignupRepositoryImpl = mockk {
//        coEvery {
//            signup(any(), any())
//            signInWithGoogle(any())
//            signInWithTwitter(any())
//            signInWithFacebook(any())
//        } returns mockk()
//    }
//
//    private val checkUserLoggedInUseCase = CheckUserLoggedInUseCase(signInRepository)
//    private val signupWithEmailUseCase = SignupWithEmailUseCase(signUpRepository)
//    private val signInWithGoogleUseCase = SignInWithGoogleUseCase(signUpRepository)
//    private val signInWithTwitterUseCase = SignInWithTwitterUseCase(signUpRepository)
//    private val signInWithFacebookUseCase = SignInWithFacebookUseCase(signUpRepository)
//    private val signInWithEmailUseCase = SignInWithEmailUseCase(signInRepository)
//    private val sendPasswordResetEmailUseCase = SendPasswordResetEmailUseCase(signInRepository)
//
//    private val viewModel = AuthViewModel(
//        checkUserLoggedInUseCase,
//        signupWithEmailUseCase,
//        signInWithGoogleUseCase,
//        signInWithTwitterUseCase,
//        signInWithFacebookUseCase,
//        signInWithEmailUseCase,
//        sendPasswordResetEmailUseCase
//    )
}