package com.artofelectronic.nexchat.domain.usecases

import android.content.Context
import com.artofelectronic.nexchat.domain.repository.SignUpRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {
    suspend operator fun invoke(context: Context): Result<String> =
        signUpRepository.signInWithGoogle(context)

}