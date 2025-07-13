//package com.artofelectronic.nexchat.domain.usecases
//
//import com.artofelectronic.nexchat.data.repository.FakeSignInRepository
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//
//class CheckUserLoggedInUseCaseTest {
//
//    private lateinit var fakeSignInRepository: FakeSignInRepository
//    private lateinit var checkUserLoggedInUseCase: CheckUserLoggedInUseCase
//
//    @Before
//    fun setUp() {
//        fakeSignInRepository = FakeSignInRepository()
//        checkUserLoggedInUseCase = CheckUserLoggedInUseCase(fakeSignInRepository)
//    }
//
//    @Test
//    fun `when user is signed in, return true`() = runBlocking {
//        fakeSignInRepository.setSignedInStatus(true)
//        val result = checkUserLoggedInUseCase.invoke()
//        assertEquals(true, result)
//    }
//
//    @Test
//    fun `when user is not signed in, return false`() = runBlocking {
//        fakeSignInRepository.setSignedInStatus(false)
//        val result = checkUserLoggedInUseCase.invoke()
//        assertEquals(false, result)
//    }
//}