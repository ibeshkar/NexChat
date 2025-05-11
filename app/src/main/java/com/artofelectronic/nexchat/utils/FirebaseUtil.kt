package com.artofelectronic.nexchat.utils

import com.artofelectronic.nexchat.data.models.User
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutionException

object FirebaseUtil {

    /**
     * Checks if the user is signed in by attempting to refresh the ID token.
     */
    fun isUserSignedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false

        return try {
            Tasks.await(currentUser.getIdToken(true))
            true
        } catch (e: ExecutionException) {
            if (e.cause is FirebaseAuthInvalidUserException) {
                signOutFromFirebase()
            }
            false
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            false
        }
    }

    /**
     * Creates a new user in Firebase with the provided email and password.
     */
    fun createUserInFirebase(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    }

    /**
     * Signs in a user in Firebase with the provided email and password.
     */
    fun signInUserInFirebase(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }

    /**
     * Sends a password reset email to the provided email address.
     */
    fun sendPasswordResetEmail(email: String): Task<Void> {
        return FirebaseAuth.getInstance().sendPasswordResetEmail(email)
    }

    /**
     * Signs in a user with Google credentials using the provided ID token.
     */
    fun signInWithFirebase(idToken: String): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return FirebaseAuth.getInstance().signInWithCredential(credential)
    }

    /**
     * Signs out the current user from Firebase.
     */
    private fun signOutFromFirebase() {
        FirebaseAuth.getInstance().signOut()
    }

    /**
     * Returns a DocumentReference for the current user's details in Firestore.
     */
    fun currentUserDetails(): DocumentReference {
        return FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    /**
     * Creates a new user in Firestore with the provided User object.
     */
    fun createUserInFirestore(user: User):Task<Void> {
        return currentUserDetails().set(user)
    }
}