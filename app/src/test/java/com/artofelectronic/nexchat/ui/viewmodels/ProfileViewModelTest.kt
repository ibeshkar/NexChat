package com.artofelectronic.nexchat.ui.viewmodels

import android.net.Uri
import com.artofelectronic.nexchat.domain.usecases.auth.GetCurrentUserIdUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CreateOrUpdateUserUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUserProfileUseCase
import com.artofelectronic.nexchat.domain.usecases.users.SignOutUseCase
import com.artofelectronic.nexchat.domain.usecases.users.UpdateAvatarUrlUseCase
import com.artofelectronic.nexchat.ui.state.UiState
import com.artofelectronic.nexchat.utils.Fake_UserId
import com.artofelectronic.nexchat.utils.MainDispatcherRule
import com.artofelectronic.nexchat.utils.fakeUser
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ProfileViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    private lateinit var userProfileUseCase: FetchUserProfileUseCase
    private lateinit var createOrUpdateUserUseCase: CreateOrUpdateUserUseCase
    private lateinit var updateAvatarUrlUseCase: UpdateAvatarUrlUseCase
    private lateinit var fetchUserProfileUseCase: FetchUserProfileUseCase
    private lateinit var signOutUseCase: SignOutUseCase


    private lateinit var profileViewModel: ProfileViewModel


    @Before
    fun setup() {
        getCurrentUserIdUseCase = mockk()
        userProfileUseCase = mockk()
        createOrUpdateUserUseCase = mockk()
        updateAvatarUrlUseCase = mockk()
        fetchUserProfileUseCase = mockk()
        signOutUseCase = mockk()

        coEvery { getCurrentUserIdUseCase() } returns Fake_UserId
        coEvery { userProfileUseCase(any()) } returns fakeUser
        coEvery { createOrUpdateUserUseCase(any()) } just Runs
        coEvery { updateAvatarUrlUseCase(any(), any()) } returns "newAvatarUrl"
        coEvery { fetchUserProfileUseCase(any()) } returns fakeUser
        coEvery { signOutUseCase() } just Runs

        profileViewModel = ProfileViewModel(
            getCurrentUserIdUseCase,
            userProfileUseCase,
            createOrUpdateUserUseCase,
            updateAvatarUrlUseCase,
            fetchUserProfileUseCase,
            signOutUseCase
        )
    }

    @Test
    fun `init should call getUserProfile and set Success state`() = runTest {
        testScheduler.advanceUntilIdle()

        assertEquals(fakeUser, profileViewModel.userProfile.value)
        assertEquals(UiState.Success, profileViewModel.uiState.value)
    }

    @Test
    fun `getUserProfile with null userId emit error`() = runTest {
        every { getCurrentUserIdUseCase() } returns ""

        profileViewModel = ProfileViewModel(
            getCurrentUserIdUseCase,
            userProfileUseCase,
            createOrUpdateUserUseCase,
            updateAvatarUrlUseCase,
            fetchUserProfileUseCase,
            signOutUseCase
        )

        testScheduler.advanceUntilIdle()

        val state = profileViewModel.uiState.value

        assertTrue(state is UiState.Error)
        assertEquals("User Id is not valid!", (state as UiState.Error).message)
    }

    @Test
    fun `getUserProfile should handle exception`() = runTest {
        coEvery { userProfileUseCase(any()) } throws RuntimeException("Boom!")
        
        profileViewModel.getUserProfile()
        testScheduler.advanceUntilIdle()

        assertTrue(profileViewModel.uiState.value is UiState.Error)
        assertEquals("Boom!", (profileViewModel.uiState.value as UiState.Error).message)
    }

    @Test
    fun `fetchUserProfile should set userProfile`() = runTest {
        profileViewModel.fetchUserProfile(Fake_UserId)
        testScheduler.advanceUntilIdle()

        assertEquals(fakeUser, profileViewModel.userProfile.value)
    }

    @Test
    fun `fetchUserProfile with empty userId should do nothing`() = runTest {
        every { getCurrentUserIdUseCase() } returns ""

        profileViewModel = ProfileViewModel(
            getCurrentUserIdUseCase,
            userProfileUseCase,
            createOrUpdateUserUseCase,
            updateAvatarUrlUseCase,
            fetchUserProfileUseCase,
            signOutUseCase
        )

        profileViewModel.fetchUserProfile("")
        testScheduler.advanceUntilIdle()

        assertNull(profileViewModel.userProfile.value)
    }

    @Test
    fun `fetchUserProfile should handle exception`() = runTest {
        coEvery { fetchUserProfileUseCase(any()) } throws RuntimeException()

        profileViewModel.fetchUserProfile(Fake_UserId)
        testScheduler.advanceUntilIdle()

        assertNull(profileViewModel.userProfile.value)
    }

    @Test
    fun `updateUserProfile without avatar change should update profile`() = runTest {
        profileViewModel.updateUserProfile(fakeUser)
        testScheduler.advanceUntilIdle()

        assertEquals(fakeUser, profileViewModel.userProfile.value)
        assertEquals(UiState.Success, profileViewModel.uiState.value)
    }

    @Test
    fun `updateUserProfile with avatar change should update avatar and profile`() = runTest {
        val uri = mockk<Uri>()
        profileViewModel.updateUserProfile(fakeUser, isAvatarChanged = true, avatarUri = uri)
        testScheduler.advanceUntilIdle()

        coVerify { updateAvatarUrlUseCase(Fake_UserId, uri) }
        coVerify { createOrUpdateUserUseCase(fakeUser) }

        assertEquals(UiState.Success, profileViewModel.uiState.value)
    }

    @Test
    fun `signOut should set Success state`() = runTest {
        profileViewModel.signOut()
        testScheduler.advanceUntilIdle()

        assertEquals(UiState.Success, profileViewModel.signOutState.value)
    }

    @Test
    fun `signOut should handle exception`() = runTest {
        coEvery { signOutUseCase() } throws RuntimeException("Logout failed")

        profileViewModel.signOut()
        testScheduler.advanceUntilIdle()

        assertTrue(profileViewModel.signOutState.value is UiState.Error)
        assertEquals("Logout failed", (profileViewModel.signOutState.value as UiState.Error).message)
    }

}