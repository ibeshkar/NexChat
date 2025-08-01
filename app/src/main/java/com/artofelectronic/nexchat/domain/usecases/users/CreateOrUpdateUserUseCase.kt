package com.artofelectronic.nexchat.domain.usecases.users

import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.domain.repository.UserRepository
import javax.inject.Inject

class CreateOrUpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) = userRepository.createOrUpdateUser(user)
}