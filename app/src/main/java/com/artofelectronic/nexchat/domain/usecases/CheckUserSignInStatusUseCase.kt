package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.SignInRepository
import javax.inject.Inject

class CheckUserSignInStatusUseCase @Inject constructor(private val signInRepository: SignInRepository) {
    suspend operator fun invoke() = signInRepository.isUserSignedIn()
}