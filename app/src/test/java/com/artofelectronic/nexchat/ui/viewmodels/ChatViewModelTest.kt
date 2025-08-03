package com.artofelectronic.nexchat.ui.viewmodels

import com.artofelectronic.nexchat.domain.usecases.auth.GetCurrentUserIdUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ChatRealtimeSyncUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.FetchChatsOnceIfNeededUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.GetMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ObserveChatsUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ObserveMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.SendMessageUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUserProfileUseCase
import com.artofelectronic.nexchat.utils.Fake_ChatId
import com.artofelectronic.nexchat.utils.Fake_ReceiverId
import com.artofelectronic.nexchat.utils.Fake_UserId
import com.artofelectronic.nexchat.utils.MainDispatcherRule
import com.artofelectronic.nexchat.utils.Resource
import com.artofelectronic.nexchat.utils.chats
import com.artofelectronic.nexchat.utils.fakeUser
import com.artofelectronic.nexchat.utils.messages
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ChatViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    private lateinit var observeChatsUseCase: ObserveChatsUseCase
    private lateinit var fetchChatsOnceIfNeededUseCase: FetchChatsOnceIfNeededUseCase
    private lateinit var chatRealtimeSyncUseCase: ChatRealtimeSyncUseCase
    private lateinit var observeMessagesUseCase: ObserveMessagesUseCase
    private lateinit var sendMessageUseCase: SendMessageUseCase
    private lateinit var getMessagesUseCase: GetMessagesUseCase
    private lateinit var fetchUserProfileUseCase: FetchUserProfileUseCase

    private lateinit var chatViewModel: ChatViewModel


    @Before
    fun setup() {
        getCurrentUserIdUseCase = mockk()
        observeChatsUseCase = mockk()
        fetchChatsOnceIfNeededUseCase = mockk()
        chatRealtimeSyncUseCase = mockk()
        observeMessagesUseCase = mockk()
        sendMessageUseCase = mockk()
        getMessagesUseCase = mockk()
        fetchUserProfileUseCase = mockk()

        coEvery { getCurrentUserIdUseCase() } returns Fake_UserId

        chatViewModel = ChatViewModel(
            getCurrentUserIdUseCase,
            observeChatsUseCase,
            fetchChatsOnceIfNeededUseCase,
            chatRealtimeSyncUseCase,
            observeMessagesUseCase,
            sendMessageUseCase,
            getMessagesUseCase,
            fetchUserProfileUseCase,
        )
    }

    @Test
    fun `observeChats with null userId emit error`() = runTest {
        coEvery { getCurrentUserIdUseCase() } returns null

        chatViewModel = ChatViewModel(
            getCurrentUserIdUseCase,
            observeChatsUseCase,
            fetchChatsOnceIfNeededUseCase,
            chatRealtimeSyncUseCase,
            observeMessagesUseCase,
            sendMessageUseCase,
            getMessagesUseCase,
            fetchUserProfileUseCase,
        )

        chatViewModel.observeChats()

        val state = chatViewModel.chatList.value
        assertTrue(state is Resource.Error)
        assertEquals("User Id is not valid!", (state as Resource.Error).throwable.message)


        coVerify(exactly = 0) { fetchChatsOnceIfNeededUseCase(any()) }
        verify(exactly = 0) { chatRealtimeSyncUseCase(any()) }
        verify(exactly = 0) { observeChatsUseCase() }
    }

    @Test
    fun `observeChats with empty userId sets error`() = runTest {
        coEvery { getCurrentUserIdUseCase() } returns ""

        chatViewModel = ChatViewModel(
            getCurrentUserIdUseCase,
            observeChatsUseCase,
            fetchChatsOnceIfNeededUseCase,
            chatRealtimeSyncUseCase,
            observeMessagesUseCase,
            sendMessageUseCase,
            getMessagesUseCase,
            fetchUserProfileUseCase,
        )

        chatViewModel.observeChats()

        val state = chatViewModel.chatList.value
        assertTrue(state is Resource.Error)
        assertEquals("User Id is not valid!", (state as Resource.Error).throwable.message)

        coVerify(exactly = 0) { fetchChatsOnceIfNeededUseCase(any()) }
        verify(exactly = 0) { chatRealtimeSyncUseCase(any()) }
        verify(exactly = 0) { observeChatsUseCase() }
    }

    @Test
    fun `observeChats with valid user Id collect chats`() = runTest {
        coEvery { fetchChatsOnceIfNeededUseCase(any()) } just Runs
        every { chatRealtimeSyncUseCase(any()) } returns mockk()
        every { observeChatsUseCase() } returns flowOf(chats)
        coEvery { getCurrentUserIdUseCase() } returns Fake_UserId

        chatViewModel = ChatViewModel(
            getCurrentUserIdUseCase,
            observeChatsUseCase,
            fetchChatsOnceIfNeededUseCase,
            chatRealtimeSyncUseCase,
            observeMessagesUseCase,
            sendMessageUseCase,
            getMessagesUseCase,
            fetchUserProfileUseCase,
        )

        chatViewModel.observeChats()
        testScheduler.advanceUntilIdle()

        val state = chatViewModel.chatList.value
        assertTrue(state is Resource.Success)
        assertEquals(chats, (state as Resource.Success).data)
    }

    @Test
    fun `fetchUserProfile with empty userId sets error`() = runTest {
        chatViewModel.fetchUserProfile("")

        assertTrue(chatViewModel.chatList.value is Resource.Error)

        coVerify(exactly = 0) { fetchUserProfileUseCase(any()) }
    }

    @Test
    fun `fetchUserProfile with valid userId update state`() = runTest {
        coEvery { fetchUserProfileUseCase(Fake_UserId) } returns fakeUser

        chatViewModel.fetchUserProfile(Fake_UserId)
        testScheduler.advanceUntilIdle()

        assertEquals(fakeUser, chatViewModel.userProfile.value)
    }

    @Test
    fun `observeMessages should collect messages`() = runTest {
        every { getMessagesUseCase(any()) } returns mockk()
        every { observeMessagesUseCase(Fake_ChatId) } returns flowOf(messages)

        chatViewModel.observeMessages(Fake_ChatId)
        testScheduler.advanceUntilIdle()

        assertEquals(messages, chatViewModel.messages.value)

        verify { getMessagesUseCase(any()) }
        verify { observeMessagesUseCase(Fake_ChatId) }
    }

    @Test
    fun `sendMessage should call use case`() = runTest {
        coEvery { sendMessageUseCase(any(),any()) } just Runs

        chatViewModel.sendMessage(Fake_ChatId, "Hello", Fake_UserId, Fake_ReceiverId)
        testScheduler.advanceUntilIdle()

        coVerify {
            sendMessageUseCase(
                match { it.text == "Hello" && it.chatId == Fake_ChatId },
                Fake_ReceiverId
            )
        }
    }
}