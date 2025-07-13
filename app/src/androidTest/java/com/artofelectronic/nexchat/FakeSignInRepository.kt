//package com.artofelectronic.nexchat
//
//import com.artofelectronic.nexchat.domain.repository.SignInRepository
//import com.artofelectronic.nexchat.ui.state.AuthState
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
//            isSignedIn = true
//            AuthState.Success()
//        } else {
//            AuthState.Error("Invalid credentials")
//        }
//    }
//
//    override suspend fun resetPassword(email: String): AuthState {
//        TODO("Not yet implemented")
//    }
//}