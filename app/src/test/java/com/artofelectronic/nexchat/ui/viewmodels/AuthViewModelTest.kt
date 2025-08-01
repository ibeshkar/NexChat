package com.artofelectronic.nexchat.ui.viewmodels

import android.app.Activity
import com.artofelectronic.nexchat.domain.usecases.auth.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithFacebookUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithGoogleUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithTwitterUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignupWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CheckUserLoggedInUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CreateOrUpdateUserUseCase
import com.artofelectronic.nexchat.ui.state.UiState
import com.artofelectronic.nexchat.utils.Facebook_Access_Token
import com.artofelectronic.nexchat.utils.INVALID_EMAIL
import com.artofelectronic.nexchat.utils.INVALID_PASSWORD
import com.artofelectronic.nexchat.utils.MainDispatcherRule
import com.artofelectronic.nexchat.utils.VALID_EMAIL
import com.artofelectronic.nexchat.utils.VALID_PASSWORD
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class AuthViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var checkUserLoggedInUseCase: CheckUserLoggedInUseCase
    private lateinit var signupWithEmailUseCase: SignupWithEmailUseCase
    private lateinit var signInWithGoogleUseCase: SignInWithGoogleUseCase
    private lateinit var createOrUpdateUserUseCase: CreateOrUpdateUserUseCase
    private lateinit var signInWithTwitterUseCase: SignInWithTwitterUseCase
    private lateinit var signInWithFacebookUseCase: SignInWithFacebookUseCase
    private lateinit var signInWithEmailUseCase: SignInWithEmailUseCase
    private lateinit var sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase

    private lateinit var authViewModel: AuthViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        checkUserLoggedInUseCase = mockk()
        signupWithEmailUseCase = mockk()
        signInWithGoogleUseCase = mockk()
        createOrUpdateUserUseCase = mockk()
        signInWithTwitterUseCase = mockk()
        signInWithFacebookUseCase = mockk()
        signInWithEmailUseCase = mockk()
        sendPasswordResetEmailUseCase = mockk()

        coEvery { checkUserLoggedInUseCase() } returns false

        authViewModel = AuthViewModel(
            checkUserLoggedInUseCase,
            signupWithEmailUseCase,
            signInWithGoogleUseCase,
            createOrUpdateUserUseCase,
            signInWithTwitterUseCase,
            signInWithFacebookUseCase,
            signInWithEmailUseCase,
            sendPasswordResetEmailUseCase
        )
    }

    @Test
    fun `signupWithEmail invalid email sets error`() = runTest {
        authViewModel.onEmailChanged(INVALID_EMAIL)
        authViewModel.onPasswordChanged(VALID_PASSWORD)
        authViewModel.onConfirmPasswordChanged(VALID_PASSWORD)

        authViewModel.signupWithEmail()

        val state = authViewModel.authFormData.value
        assertTrue(state.emailError != null)
        assertTrue(state.passwordError == null)
        assertTrue(state.confirmPasswordError == null)

        // Ensures that the AuthViewModel didn't call the use case despite the invalid input.
        coVerify(exactly = 0) { signupWithEmailUseCase(any(), any()) }
    }

    @Test
    fun `signupWithEmail invalid password sets error`() = runTest {
        authViewModel.onEmailChanged(VALID_EMAIL)
        authViewModel.onPasswordChanged(INVALID_PASSWORD)
        authViewModel.onConfirmPasswordChanged(INVALID_PASSWORD)

        authViewModel.signupWithEmail()

        val state = authViewModel.authFormData.value
        assertTrue(state.emailError == null)
        assertTrue(state.passwordError != null)
        assertTrue(state.confirmPasswordError == null)

        // Ensures that the AuthViewModel didn't call the use case despite the invalid input.
        coVerify(exactly = 0) { signupWithEmailUseCase(any(), any()) }
    }

    @Test
    fun `signupWithEmail with mismatched passwords sets error`() = runTest {
        authViewModel.onEmailChanged(VALID_EMAIL)
        authViewModel.onPasswordChanged(VALID_PASSWORD)
        authViewModel.onConfirmPasswordChanged(INVALID_PASSWORD)

        authViewModel.signupWithEmail()

        val state = authViewModel.authFormData.value
        assertTrue(state.emailError == null)
        assertTrue(state.passwordError == null)
        assertTrue(state.confirmPasswordError != null)

        // Ensures that the AuthViewModel didn't call the use case despite the invalid input.
        coVerify(exactly = 0) { signupWithEmailUseCase(any(), any()) }
    }


    @Test
    fun `signupWithEmail with valid inputs sets success state`() = runTest {
        val fakeUser = mockk<FirebaseUser> {
            every { uid } returns "fake-uid"
            every { displayName } returns "fake-name"
            every { photoUrl } returns mockk(relaxed = true)
            every { email } returns VALID_EMAIL
        }
        val fakeAuthResult = mockk<AuthResult> {
            every { user } returns fakeUser
        }
        coEvery { signupWithEmailUseCase(any(), any()) } returns fakeAuthResult
        coEvery { createOrUpdateUserUseCase(any()) } just Runs

        authViewModel.onEmailChanged(VALID_EMAIL)
        authViewModel.onPasswordChanged(VALID_PASSWORD)
        authViewModel.onConfirmPasswordChanged(VALID_PASSWORD)

        authViewModel.signupWithEmail()
        testScheduler.advanceUntilIdle()

        val uiState = authViewModel.uiState.value
        val formData = authViewModel.authFormData.value

        assertEquals(UiState.Success, uiState)
        assertEquals(false, formData.isLoading)

        coVerify(exactly = 1) { signupWithEmailUseCase(VALID_EMAIL, VALID_PASSWORD) }
        coVerify(exactly = 1) { createOrUpdateUserUseCase(any()) }
    }


    @Test
    fun `signupWithEmail when use cases throws sets error`() = runTest {
        coEvery { signupWithEmailUseCase(any(), any()) } throws Exception("Network error")

        authViewModel.onEmailChanged(VALID_EMAIL)
        authViewModel.onPasswordChanged(VALID_PASSWORD)
        authViewModel.onConfirmPasswordChanged(VALID_PASSWORD)

        authViewModel.signupWithEmail()
        testScheduler.advanceUntilIdle()

        val uiState = authViewModel.uiState.value
        val formData = authViewModel.authFormData.value

        assertTrue(uiState is UiState.Error)
        assertTrue((uiState as UiState.Error).message.contains("Network error"))
        assertEquals(false, formData.isLoading)

        coVerify(exactly = 1) { signupWithEmailUseCase(any(), any()) }
    }

    @Test
    fun `signupWithGoogle sets success`() = runTest {
        val fakeUser = mockk<FirebaseUser> {
            every { uid } returns "fake-uid"
            every { displayName } returns "fake-name"
            every { photoUrl } returns mockk(relaxed = true)
            every { email } returns VALID_EMAIL
        }
        val fakeAuthResult = mockk<AuthResult> {
            every { user } returns fakeUser
        }

        coEvery { signInWithGoogleUseCase() } returns fakeAuthResult
        coEvery { createOrUpdateUserUseCase(any()) } just Runs

        authViewModel.signupWithGoogle()
        testScheduler.advanceUntilIdle()

        assertEquals(UiState.Success, authViewModel.uiState.value)

        coVerify { signInWithGoogleUseCase() }
        coVerify { createOrUpdateUserUseCase(any()) }
    }

    @Test
    fun `signupWithGoogle failure sets error`() = runTest {
        coEvery { signInWithGoogleUseCase() } throws kotlin.Exception("Google error")

        authViewModel.signupWithGoogle()
        testScheduler.advanceUntilIdle()

        assertTrue(authViewModel.uiState.value is UiState.Error)

        coVerify { signInWithGoogleUseCase() }
    }

    @Test
    fun `handleFacebookAccessToken sets success`() = runTest {
        val fakeUser = mockk<FirebaseUser> {
            every { uid } returns "fake-uid"
            every { displayName } returns "fake-name"
            every { photoUrl } returns mockk(relaxed = true)
            every { email } returns VALID_EMAIL
        }
        val fakeAuthResult = mockk<AuthResult> {
            every { user } returns fakeUser
        }

        coEvery { signInWithFacebookUseCase(any()) } returns fakeAuthResult
        coEvery { createOrUpdateUserUseCase(any()) } just Runs

        authViewModel.handleFacebookAccessToken(Facebook_Access_Token)
        testScheduler.advanceUntilIdle()

        assertEquals(UiState.Success, authViewModel.uiState.value)

        coVerify { signInWithFacebookUseCase(any()) }
        coVerify { createOrUpdateUserUseCase(any()) }
    }

    @Test
    fun `handleFacebookAccessToken failure sets error`() = runTest {
        coEvery { signInWithFacebookUseCase(any()) } throws Exception("Facebook error")

        authViewModel.handleFacebookAccessToken(Facebook_Access_Token)
        testScheduler.advanceUntilIdle()

        assertTrue(authViewModel.uiState.value is UiState.Error)

        coVerify { signInWithFacebookUseCase(any()) }
        coVerify(exactly = 0) { createOrUpdateUserUseCase(any()) }
    }

    @Test
    fun `signInWithTwitter sets success`() = runTest {
        val fakeUser = mockk<FirebaseUser> {
            every { uid } returns "fake-uid"
            every { displayName } returns "fake-name"
            every { photoUrl } returns mockk(relaxed = true)
            every { email } returns VALID_EMAIL
        }
        val fakeAuthResult = mockk<AuthResult> {
            every { user } returns fakeUser
        }

        coEvery { signInWithTwitterUseCase(any()) } returns fakeAuthResult
        coEvery { createOrUpdateUserUseCase(any()) } just Runs

        val fakeActivity = mockk<Activity>()
        authViewModel.signInWithTwitter(fakeActivity)
        testScheduler.advanceUntilIdle()

        assertEquals(UiState.Success, authViewModel.uiState.value)

        coVerify { signInWithTwitterUseCase(fakeActivity) }
        coVerify { createOrUpdateUserUseCase(any()) }
    }


    @Test
    fun `signInWithTwitter failure sets error`() = runTest {
        val fakeActivity = mockk<Activity>()
        coEvery { signInWithTwitterUseCase(any()) } throws kotlin.Exception("Twitter error")

        authViewModel.signInWithTwitter(fakeActivity)
        testScheduler.advanceUntilIdle()

        assertTrue(authViewModel.uiState.value is UiState.Error)

        coVerify { signInWithTwitterUseCase(fakeActivity) }
        coVerify(exactly = 0) { createOrUpdateUserUseCase(any()) }
    }


    @Test
    fun `signInWithEmail invalid email sets error`() = runTest {
        authViewModel.onEmailChanged(INVALID_EMAIL)
        authViewModel.onPasswordChanged(VALID_PASSWORD)

        authViewModel.signInWithEmail()

        val state = authViewModel.authFormData.value
        assertTrue(state.emailError != null)
        assertTrue(state.passwordError == null)

        // Ensures that the AuthViewModel didn't call the use case despite the invalid input.
        coVerify(exactly = 0) { signInWithEmailUseCase(any(), any()) }
    }

    @Test
    fun `signInWithEmail invalid password sets error`() = runTest {
        authViewModel.onEmailChanged(VALID_EMAIL)
        authViewModel.onPasswordChanged(INVALID_PASSWORD)

        authViewModel.signInWithEmail()

        val state = authViewModel.authFormData.value
        assertTrue(state.emailError == null)
        assertTrue(state.passwordError != null)

        // Ensures that the AuthViewModel didn't call the use case despite the invalid input.
        coVerify(exactly = 0) { signInWithEmailUseCase(any(), any()) }
    }

    @Test
    fun `signInWithEmail with valid inputs sets success state`() = runTest {
        val fakeUser = mockk<FirebaseUser> {
            every { uid } returns "fake-uid"
            every { displayName } returns "fake-name"
            every { photoUrl } returns mockk(relaxed = true)
            every { email } returns VALID_EMAIL
        }
        val fakeAuthResult = mockk<AuthResult> {
            every { user } returns fakeUser
        }
        coEvery { signInWithEmailUseCase(any(), any()) } returns fakeAuthResult

        authViewModel.onEmailChanged(VALID_EMAIL)
        authViewModel.onPasswordChanged(VALID_PASSWORD)

        authViewModel.signInWithEmail()
        testScheduler.advanceUntilIdle()

        val uiState = authViewModel.uiState.value
        val formData = authViewModel.authFormData.value

        assertEquals(UiState.Success, uiState)
        assertEquals(false, formData.isLoading)

        coVerify(exactly = 1) { signInWithEmailUseCase(VALID_EMAIL, VALID_PASSWORD) }
    }

    @Test
    fun `signInWithEmail when use case throws sets error`() = runTest {
        coEvery { signInWithEmailUseCase(any(), any()) } throws Exception("Network error")

        authViewModel.onEmailChanged(VALID_EMAIL)
        authViewModel.onPasswordChanged(VALID_PASSWORD)

        authViewModel.signInWithEmail()
        testScheduler.advanceUntilIdle()

        val uiState = authViewModel.uiState.value
        val formData = authViewModel.authFormData.value

        assertTrue(uiState is UiState.Error)
        assertTrue((uiState as UiState.Error).message.contains("Network error"))
        assertEquals(false, formData.isLoading)

        coVerify(exactly = 1) { signInWithEmailUseCase(any(), any()) }
    }

    @Test
    fun `sendPasswordResetEmail with invalid email sets error`() = runTest {
        authViewModel.onEmailChanged(INVALID_EMAIL)

        authViewModel.sendPasswordResetEmail()

        assertTrue(authViewModel.authFormData.value.emailError != null)

        coVerify(exactly = 0) { sendPasswordResetEmailUseCase(any()) }
    }

    @Test
    fun `sendPasswordResetEmail with valid email sets success`() = runTest {
        coEvery { sendPasswordResetEmailUseCase(any()) } just Runs

        authViewModel.onEmailChanged(VALID_EMAIL)

        authViewModel.sendPasswordResetEmail()
        testScheduler.advanceUntilIdle()

        assertEquals(UiState.Success, authViewModel.uiState.value)

        coVerify(exactly = 1) { sendPasswordResetEmailUseCase(VALID_EMAIL) }
    }

    @Test
    fun `sendPasswordResetEmail when use case throws sets error`() = runTest {
        coEvery { sendPasswordResetEmailUseCase(any()) } throws kotlin.Exception("Server error")

        authViewModel.onEmailChanged(VALID_EMAIL)

        authViewModel.sendPasswordResetEmail()
        testScheduler.advanceUntilIdle()

        assertTrue(authViewModel.uiState.value is UiState.Error)

        coVerify(exactly = 1) { sendPasswordResetEmailUseCase(VALID_EMAIL) }
    }

}