package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.SignUpRepository
import javax.inject.Inject

class SignupWithEmailUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {
    suspend operator fun invoke(email: String, password: String) = signUpRepository.signup(email, password)
}