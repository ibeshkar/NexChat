package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.data.repository.FakeSignInRepository
import com.artofelectronic.nexchat.ui.state.SignupState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignInWithEmailUseCaseTest {

    private lateinit var signInWithEmailUseCase: SignInWithEmailUseCase

    @Before
    fun setUp() {
        signInWithEmailUseCase = SignInWithEmailUseCase(FakeSignInRepository())
    }

    @Test
    fun `sign in with valid credentials`() = runBlocking {
        val email = "test@example.com"
        val password = "Taha1391!"
        val result = signInWithEmailUseCase.invoke(email, password)
        assertEquals(SignupState.Success(), result)
    }

    @Test
    fun `sign in with invalid credentials`() = runBlocking {
        val email = "invalid-email"
        val password = "invalid-password"
        val result = signInWithEmailUseCase.invoke(email, password)
        assertEquals(SignupState.Error("Invalid credentials"), result)
    }
}