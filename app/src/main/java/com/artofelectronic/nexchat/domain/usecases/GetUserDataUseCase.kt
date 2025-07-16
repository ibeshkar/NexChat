package com.artofelectronic.nexchat.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    operator fun invoke() = firebaseAuth.currentUser
}