package com.artofelectronic.nexchat.domain.repository

interface SignInRepository {
    suspend fun isUserSignedIn(): Boolean
}