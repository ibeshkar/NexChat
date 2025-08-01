package com.artofelectronic.nexchat.ui.viewmodels

import com.artofelectronic.nexchat.domain.usecases.auth.GetCurrentUserIdUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.CreateOrContinueChatUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUsersUseCase
import com.artofelectronic.nexchat.domain.usecases.users.ObserveUsersUseCase
import com.artofelectronic.nexchat.domain.usecases.users.UserChangeListenerUseCase
import com.artofelectronic.nexchat.utils.Fake_ReceiverId
import com.artofelectronic.nexchat.utils.Fake_UserId
import com.artofelectronic.nexchat.utils.MainDispatcherRule
import com.artofelectronic.nexchat.utils.Resource
import com.artofelectronic.nexchat.utils.fakeChat
import com.artofelectronic.nexchat.utils.users
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class UserViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    private lateinit var observeUsersUseCase: ObserveUsersUseCase
    private lateinit var fetchUsersUseCase: FetchUsersUseCase
    private lateinit var userChangeListenerUseCase: UserChangeListenerUseCase
    private lateinit var createOrContinueChatUseCase: CreateOrContinueChatUseCase


    private lateinit var userViewModel: UserViewModel


    @Before
    fun setup() {
        getCurrentUserIdUseCase = mockk()
        observeUsersUseCase = mockk()
        fetchUsersUseCase = mockk()
        userChangeListenerUseCase = mockk()
        createOrContinueChatUseCase = mockk()

        coEvery { getCurrentUserIdUseCase() } returns Fake_UserId


        userViewModel = UserViewModel(
            getCurrentUserIdUseCase,
            observeUsersUseCase,
            fetchUsersUseCase,
            userChangeListenerUseCase,
            createOrContinueChatUseCase
        )
    }

    @Test
    fun `init should set currentUserId`() {
        assertEquals(Fake_UserId, userViewModel.currentUserId.value)
    }

    @Test
    fun `observeUsers should update userList`() = runTest {
        coEvery { fetchUsersUseCase() } just Runs
        coEvery { userChangeListenerUseCase() } returns mockk()
        coEvery { observeUsersUseCase() } returns flowOf(users)

        userViewModel.observeUsers()
        testScheduler.advanceUntilIdle()

        val state = userViewModel.userList.value
        assertTrue(state is Resource.Success)
        assertEquals(users, (state as Resource.Success).data)

        coVerify { fetchUsersUseCase() }
        coVerify { userChangeListenerUseCase() }
        coVerify { observeUsersUseCase() }
    }

    @Test
    fun `onUserSelected with null currentUserId should do nothing`() = runTest {
        userViewModel.onUserSelected(null, Fake_ReceiverId)

        val state = userViewModel.userList.value
        assertTrue(state is Resource.Error)
        assertEquals("User Id is not valid!", (state as Resource.Error).throwable.message)

        coVerify(exactly = 0) { createOrContinueChatUseCase(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onUserSelected with valid ids should emit chatId`() = runTest {
        coEvery { createOrContinueChatUseCase(any(), any()) } returns fakeChat

        userViewModel.onUserSelected(Fake_UserId, Fake_ReceiverId)
        testScheduler.advanceUntilIdle()

        val navValues = mutableListOf<String>()
        val job = launch(UnconfinedTestDispatcher()) {
            userViewModel.navigation.collect { navValues.add(it) }
        }

        userViewModel.onUserSelected(Fake_UserId, Fake_ReceiverId)
        testScheduler.advanceUntilIdle()

        assertEquals(listOf(fakeChat.chatId), navValues)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onUserSelected when use case throws should emit error`() = runTest {
        coEvery { createOrContinueChatUseCase(any(), any()) } throws RuntimeException("Boom!")

        val navValues = mutableListOf<String>()
        val job = launch(UnconfinedTestDispatcher()) {
            userViewModel.navigation.collect { navValues.add(it) }
        }

        userViewModel.onUserSelected(Fake_UserId, Fake_ReceiverId)
        testScheduler.advanceUntilIdle()

        assertEquals(listOf("Boom!"), navValues)
        job.cancel()
    }
}