package com.artofelectronic.nexchat.domain.repository

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface SignUpRepository {
    suspend fun signup(email: String, password: String): Task<AuthResult>
    suspend fun signInWithGoogle(context: Context): Result<String>
    suspend fun signInWithTwitter(activity: Activity): Result<FirebaseUser>
    suspend fun signInWithFacebook(token: String): Result<FirebaseUser>
}