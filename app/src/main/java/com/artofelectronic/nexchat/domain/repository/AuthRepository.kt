package com.artofelectronic.nexchat.domain.repository

import android.app.Activity
import com.google.firebase.auth.AuthResult

interface AuthRepository {
    fun getCurrentUserId(): String?
    suspend fun isLoggedIn(): Boolean
    suspend fun signupWithEmail(email: String, password: String): AuthResult?
    suspend fun signInWithEmail(email: String, password: String): AuthResult?
    suspend fun signInWithGoogle(): AuthResult?
    suspend fun signInWithFacebook(token: String): AuthResult?
    suspend fun signInWithTwitter(activity: Activity): AuthResult?
    suspend fun sendPasswordResetEmail(email: String)
}