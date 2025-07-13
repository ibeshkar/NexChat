package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.UserRepository
import javax.inject.Inject

class LoadUsersUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getAllUsers()
}