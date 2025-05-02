package com.artofelectronic.nexchat.domain

import javax.inject.Inject

class CheckUserSignInStatusUseCase @Inject constructor(private val signInRepository: SignInRepository) {
    suspend operator fun invoke() = signInRepository.isUserSignedIn()
}