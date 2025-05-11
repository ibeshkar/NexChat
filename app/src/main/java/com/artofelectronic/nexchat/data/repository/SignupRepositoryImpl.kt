package com.artofelectronic.nexchat.data.repository

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.artofelectronic.nexchat.BuildConfig
import com.artofelectronic.nexchat.domain.repository.SignUpRepository
import com.artofelectronic.nexchat.utils.FirebaseUtil
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : SignUpRepository {
    override suspend fun signup(email: String, password: String) =
        FirebaseUtil.createUserInFirebase(email, password)

    override suspend fun signInWithGoogle(context: Context): Result<String> {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.Firebase_Web_Client_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        return try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                if (idToken.isNotBlank()) {
                    Result.success(idToken)
                } else {
                    Result.failure(Exception("ID token missing in credential"))
                }
            } else {
                Result.failure(Exception("Unexpected credential type: ${credential::class.simpleName}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithTwitter(activity: Activity): Result<FirebaseUser> =
        try {
            val twitterProvider = OAuthProvider.newBuilder("twitter.com")
            val pendingResultTask = auth.pendingAuthResult

            val authResult = if (pendingResultTask != null) {
                // If a previous auth result is pending, wait for it
                pendingResultTask.await()
            } else {
                // Start the sign-in process
                auth.startActivityForSignInWithProvider(activity, twitterProvider.build()).await()
            }

            val user = authResult.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun signInWithFacebook(token: String): Result<FirebaseUser> =
        try {
            val cred = FacebookAuthProvider.getCredential(token)
            val result = auth.signInWithCredential(cred).await()
            result.user?.let { Result.success(it) }
                ?: Result.failure(Exception("No Firebase user"))
        } catch (e: Exception) {
            Result.failure(e)
        }
}