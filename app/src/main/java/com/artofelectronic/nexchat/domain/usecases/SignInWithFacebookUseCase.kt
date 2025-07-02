package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.SignUpRepository
import javax.inject.Inject

class SignInWithFacebookUseCase @Inject constructor(
    private val repo: SignUpRepository
) {
    suspend operator fun invoke(token: String) = repo.signInWithFacebook(token)
}