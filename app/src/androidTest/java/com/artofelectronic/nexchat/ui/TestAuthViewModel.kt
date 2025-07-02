package com.artofelectronic.nexchat.ui

import com.artofelectronic.nexchat.FakeSignInRepository
import com.artofelectronic.nexchat.domain.repository.SignInRepository
import com.artofelectronic.nexchat.domain.usecases.SignInWithEmailUseCase
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import io.mockk.mockk

class TestAuthViewModel(
    repository: SignInRepository = FakeSignInRepository()
) : AuthViewModel(
    checkUserSignInStatusUseCase = mockk(relaxed = true),
    signupWithEmailUseCase = mockk(relaxed = true),
    signInWithGoogleUseCase = mockk(relaxed = true),
    signInWithTwitterUseCase = mockk(relaxed = true),
    signInWithFacebook = mockk(relaxed = true),
    signInWithEmailUseCase = SignInWithEmailUseCase(repository),
    sendPasswordResetEmailUseCase = mockk(relaxed = true)
)