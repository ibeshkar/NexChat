package com.artofelectronic.nexchat.domain.usecases.users

import com.artofelectronic.nexchat.domain.repository.UserRepository
import javax.inject.Inject

class FetchUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String) = userRepository.getUserProfile(userId)
}