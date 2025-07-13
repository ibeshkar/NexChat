package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.data.models.User
import com.artofelectronic.nexchat.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserInFirestoreUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) = userRepository.createUserIfNotExists(user)
}