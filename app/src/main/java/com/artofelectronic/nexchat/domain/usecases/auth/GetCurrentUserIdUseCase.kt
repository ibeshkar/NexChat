package com.artofelectronic.nexchat.domain.usecases.auth

import com.artofelectronic.nexchat.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? = authRepository.getCurrentUserId()
}