package com.artofelectronic.nexchat.domain.usecases.users

import com.artofelectronic.nexchat.domain.repository.UserRepository
import javax.inject.Inject

class FetchUsersUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.fetchUsersOnceIfNeeded()
}