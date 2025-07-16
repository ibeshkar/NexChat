package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.AuthRepository
import javax.inject.Inject

class CheckUserLoggedInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.isLoggedIn()
}