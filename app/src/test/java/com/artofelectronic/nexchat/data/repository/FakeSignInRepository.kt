//package com.artofelectronic.nexchat.data.repository
//
//import com.artofelectronic.nexchat.domain.repository.SignInRepository
//import com.artofelectronic.nexchat.ui.state.AuthState
//import com.artofelectronic.nexchat.utils.InputValidator
//
//class FakeSignInRepository : SignInRepository {
//
//    private var isSignedIn = false
//
//    fun setSignedInStatus(status: Boolean) {
//        isSignedIn = status
//    }
//
//    override suspend fun isUserSignedIn(): Boolean {
//        return isSignedIn
//    }
//
//    override suspend fun signIn(email: String, password: String): AuthState {
//        return if (email == "test@example.com" && password == "Taha1391!") {
//            AuthState.Success()
//        } else {
//            AuthState.Error("Invalid credentials")
//        }
//    }
//
//    override suspend fun resetPassword(email: String): AuthState {
//        return if (InputValidator.validateEmail(email).isNullOrEmpty()) {
//            AuthState.Success()
//        } else {
//            AuthState.Error("Invalid email address")
//        }
//    }
//}