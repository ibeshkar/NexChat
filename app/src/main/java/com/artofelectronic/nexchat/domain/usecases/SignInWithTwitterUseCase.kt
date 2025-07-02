package com.artofelectronic.nexchat.domain.usecases

import android.app.Activity
import com.artofelectronic.nexchat.domain.repository.SignUpRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignInWithTwitterUseCase @Inject constructor(
    private val repository: SignUpRepository
) {
    suspend operator fun invoke(activity: Activity): Result<FirebaseUser> {
        return repository.signInWithTwitter(activity)
    }
}