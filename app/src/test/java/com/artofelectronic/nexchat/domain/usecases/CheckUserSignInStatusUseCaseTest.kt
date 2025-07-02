package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.data.repository.FakeSignInRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CheckUserSignInStatusUseCaseTest {

    private lateinit var fakeSignInRepository: FakeSignInRepository
    private lateinit var checkUserSignInStatusUseCase: CheckUserSignInStatusUseCase

    @Before
    fun setUp() {
        fakeSignInRepository = FakeSignInRepository()
        checkUserSignInStatusUseCase = CheckUserSignInStatusUseCase(fakeSignInRepository)
    }

    @Test
    fun `when user is signed in, return true`() = runBlocking {
        fakeSignInRepository.setSignedInStatus(true)
        val result = checkUserSignInStatusUseCase.invoke()
        assertEquals(true, result)
    }

    @Test
    fun `when user is not signed in, return false`() = runBlocking {
        fakeSignInRepository.setSignedInStatus(false)
        val result = checkUserSignInStatusUseCase.invoke()
        assertEquals(false, result)
    }
}