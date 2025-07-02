package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.data.repository.FakeSignInRepository
import com.artofelectronic.nexchat.ui.state.SignupState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SendPasswordResetEmailUseCaseTest {

    private lateinit var sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase

    @Before
    fun setUp() {
        sendPasswordResetEmailUseCase = SendPasswordResetEmailUseCase(FakeSignInRepository())
    }

    @Test
    fun `send password reset email successfully`() = runBlocking {
        val email = "test@example.com"
        val task = sendPasswordResetEmailUseCase.invoke(email)
        assertEquals(SignupState.Success(), task)
    }

    @Test
    fun `send password reset email with invalid email`() = runBlocking {
        val email = "invalid-email"
        val task = sendPasswordResetEmailUseCase.invoke(email)
        assertEquals(SignupState.Error("Invalid email address"), task)
    }

}