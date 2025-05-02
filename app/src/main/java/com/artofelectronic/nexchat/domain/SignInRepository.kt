package com.artofelectronic.nexchat.domain

interface SignInRepository {
    suspend fun isUserSignedIn(): Boolean
}