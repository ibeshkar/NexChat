package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.SignInRepository
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(private val signInRepository: SignInRepository) {
    suspend operator fun invoke(email: String) = signInRepository.resetPassword(email)
}