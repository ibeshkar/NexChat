package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.data.datasource.remote.FirebaseDataSource
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    operator fun invoke() = firebaseDataSource.getCurrentUser()
}