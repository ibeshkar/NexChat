package com.artofelectronic.nexchat.domain.repository

import com.artofelectronic.nexchat.ui.state.SignupState

interface SignInRepository {
    suspend fun isUserSignedIn(): Boolean
    suspend fun signIn(email: String, password: String): SignupState
    suspend fun resetPassword(email: String): SignupState
}