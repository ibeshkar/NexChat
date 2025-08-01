package com.artofelectronic.nexchat.domain.usecases.auth

import com.artofelectronic.nexchat.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signInWithEmail(email, password)
}