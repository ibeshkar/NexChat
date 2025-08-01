package com.artofelectronic.nexchat.utils

import com.artofelectronic.nexchat.data.repository.FirebaseAuthRepository
import com.artofelectronic.nexchat.data.repository.UserRepositoryImpl
import com.artofelectronic.nexchat.domain.usecases.auth.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithFacebookUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithGoogleUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithTwitterUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignupWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CheckUserLoggedInUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CreateOrUpdateUserUseCase
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk

fun createMockAuthViewModel(): AuthViewModel {

    val authRepository = mockk<FirebaseAuthRepository>(relaxed = true)
    val userRepository = mockk<UserRepositoryImpl>(relaxed = true)

    coEvery { authRepository.isLoggedIn() } returns true

    val checkUserLoggedInUseCase = CheckUserLoggedInUseCase(authRepository)
    val signupWithEmailUseCase = SignupWithEmailUseCase(authRepository)
    val signInWithGoogleUseCase = SignInWithGoogleUseCase(authRepository)
    val createOrUpdateUserUseCase = CreateOrUpdateUserUseCase(userRepository)
    val signInWithTwitterUseCase = SignInWithTwitterUseCase(authRepository)
    val signInWithFacebook = SignInWithFacebookUseCase(authRepository)
    val signInWithEmailUseCase = SignInWithEmailUseCase(authRepository)
    val sendPasswordResetEmailUseCase = SendPasswordResetEmailUseCase(authRepository)


    return AuthViewModel(
        checkUserLoggedInUseCase,
        signupWithEmailUseCase,
        signInWithGoogleUseCase,
        createOrUpdateUserUseCase,
        signInWithTwitterUseCase,
        signInWithFacebook,
        signInWithEmailUseCase,
        sendPasswordResetEmailUseCase
    )
}