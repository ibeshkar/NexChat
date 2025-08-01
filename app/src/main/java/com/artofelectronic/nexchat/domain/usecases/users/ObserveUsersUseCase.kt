package com.artofelectronic.nexchat.domain.usecases.users

import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUsersUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(): Flow<List<User>> = repository.observeUsers()
}