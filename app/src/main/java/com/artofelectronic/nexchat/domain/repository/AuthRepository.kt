package com.artofelectronic.nexchat.domain.repository

import android.app.Activity

interface AuthRepository {
    fun isLoggedIn(): Boolean
    suspend fun signupWithEmail(email: String, password: String)
    suspend fun signInWithEmail(email: String, password: String)
    suspend fun signInWithGoogle()
    suspend fun signInWithFacebook(token: String)
    suspend fun signInWithTwitter(activity: Activity)
    suspend fun sendPasswordResetEmail(email: String)
}