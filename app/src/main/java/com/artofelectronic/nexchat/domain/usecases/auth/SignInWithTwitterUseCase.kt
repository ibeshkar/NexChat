package com.artofelectronic.nexchat.domain.usecases.auth


import android.app.Activity
import com.artofelectronic.nexchat.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithTwitterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity) = repository.signInWithTwitter(activity)
}