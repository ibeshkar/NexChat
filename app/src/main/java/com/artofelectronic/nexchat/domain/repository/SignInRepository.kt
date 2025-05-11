package com.artofelectronic.nexchat.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface SignInRepository {
    suspend fun isUserSignedIn(): Boolean
    suspend fun signIn(email: String, password: String): Task<AuthResult>
}