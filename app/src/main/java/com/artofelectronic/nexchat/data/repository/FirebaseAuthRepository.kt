package com.artofelectronic.nexchat.data.repository

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.artofelectronic.nexchat.BuildConfig
import com.artofelectronic.nexchat.core.NexChatApp
import com.artofelectronic.nexchat.domain.repository.AuthRepository
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        val currentUser = firebaseAuth.currentUser ?: return false

        return try {
            Tasks.await(currentUser.getIdToken(true))
            true
        } catch (e: ExecutionException) {
            if (e.cause is FirebaseAuthInvalidUserException) {
                firebaseAuth.signOut()
            }
            false
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            false
        }
    }

    override suspend fun signupWithEmail(email: String, password: String): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }


    override suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInWithGoogle(): AuthResult {
        val context = NexChatApp.getAppContext()
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.Firebase_Web_Client_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        val result = credentialManager.getCredential(
            context = context,
            request = request
        )

        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            if (idToken.isNotBlank()) {
                val idTokenCredential = GoogleAuthProvider.getCredential(idToken, null)
                return firebaseAuth.signInWithCredential(idTokenCredential).await()
            } else {
                throw IllegalStateException("Google ID Token is blank.")
            }
        } else {
            throw IllegalStateException("Invalid credential type returned.")
        }
    }

    override suspend fun signInWithFacebook(token: String): AuthResult {
        val credential = FacebookAuthProvider.getCredential(token)
        return firebaseAuth.signInWithCredential(credential).await()
    }

    override suspend fun signInWithTwitter(activity: Activity): AuthResult {
        val twitterProvider = OAuthProvider.newBuilder("twitter.com")
        val pendingResultTask = firebaseAuth.pendingAuthResult

        return if (pendingResultTask != null) {
            // If a previous auth result is pending, wait for it
            pendingResultTask.await()
        } else {
            // Start the sign-in process
            firebaseAuth.startActivityForSignInWithProvider(activity, twitterProvider.build()).await()
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }
}