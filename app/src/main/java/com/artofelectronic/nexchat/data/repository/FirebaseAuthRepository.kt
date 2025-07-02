package com.artofelectronic.nexchat.data.repository

import com.artofelectronic.nexchat.domain.repository.SignInRepository
import com.artofelectronic.nexchat.ui.state.SignupState
import com.artofelectronic.nexchat.utils.FirebaseUtil

class FirebaseAuthRepository : SignInRepository {
    override suspend fun isUserSignedIn() = FirebaseUtil.isUserSignedIn()

    override suspend fun signIn(email: String, password: String): SignupState {
        var result: SignupState = SignupState.Idle

        FirebaseUtil.signInUserInFirebase(email, password)
            .addOnCompleteListener {
                result = if (it.isSuccessful) {
                    SignupState.Success()
                } else {
                    SignupState.Error(it.exception?.message ?: "Unknown error occurred")
                }
            }
            .addOnFailureListener {
                result = SignupState.Error(it.message ?: "Unknown error occurred")
            }

        return result
    }


    override suspend fun resetPassword(email: String): SignupState {
        var result: SignupState = SignupState.Idle

        FirebaseUtil.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                result = if (it.isSuccessful) {
                    SignupState.Success()
                } else {
                    SignupState.Error(it.exception?.message ?: "Unknown error occurred")
                }
            }
            .addOnFailureListener {
                result = SignupState.Error(it.message ?: "Unknown error occurred")
            }

        return result
    }

}