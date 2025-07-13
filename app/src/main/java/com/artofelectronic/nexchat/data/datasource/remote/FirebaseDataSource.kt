package com.artofelectronic.nexchat.data.datasource.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(private val auth: FirebaseAuth) {

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}