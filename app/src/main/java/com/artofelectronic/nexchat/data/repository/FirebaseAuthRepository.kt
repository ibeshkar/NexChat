package com.artofelectronic.nexchat.data.repository

import com.artofelectronic.nexchat.domain.repository.SignInRepository
import com.artofelectronic.nexchat.utils.FirebaseUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class FirebaseAuthRepository : SignInRepository {
    override suspend fun isUserSignedIn() = FirebaseUtil.isUserSignedIn()

    override suspend fun signIn(email: String, password: String): Task<AuthResult> =
        FirebaseUtil.signInUserInFirebase(email, password)
}