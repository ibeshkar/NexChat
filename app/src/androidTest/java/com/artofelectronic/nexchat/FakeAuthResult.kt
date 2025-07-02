package com.artofelectronic.nexchat

import android.os.Parcel
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class FakeAuthResult(private val user: FirebaseUser?) : AuthResult {
    override fun getUser(): FirebaseUser? = user
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun getAdditionalUserInfo(): AdditionalUserInfo? = null
    override fun getCredential(): AuthCredential? = null
}