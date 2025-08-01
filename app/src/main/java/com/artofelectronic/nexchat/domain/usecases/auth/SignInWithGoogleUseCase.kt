package com.artofelectronic.nexchat.domain.usecases.auth

import com.artofelectronic.nexchat.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signInWithGoogle()
}