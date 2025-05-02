package com.artofelectronic.nexchat.data.repository

import com.artofelectronic.nexchat.domain.SignInRepository
import com.artofelectronic.nexchat.utils.FirebaseUtil

class FirebaseAuthRepository : SignInRepository {
    override suspend fun isUserSignedIn() = FirebaseUtil.isUserSignedIn()
}