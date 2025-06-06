package com.artofelectronic.nexchat

import com.artofelectronic.nexchat.domain.repository.SignInRepository
import com.artofelectronic.nexchat.ui.state.SignupState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

class FakeSignInRepository : SignInRepository {

    private var isSignedIn = false

    fun setSignedInStatus(status: Boolean) {
        isSignedIn = status
    }

    override suspend fun isUserSignedIn(): Boolean {
        return isSignedIn
    }

    override suspend fun signIn(email: String, password: String): SignupState {
        return if (email == "test@example.com" && password == "Taha1391!") {
            isSignedIn = true
            SignupState.Success()
        } else {
            SignupState.Error("Invalid credentials")
        }
    }

    override suspend fun resetPassword(email: String): SignupState {
        TODO("Not yet implemented")
    }
}