package com.artofelectronic.nexchat.utils

import com.google.firebase.auth.FirebaseAuth

object FirebaseUtil {

    /**
     * Checks if the user is signed in.
     */
    fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null


}