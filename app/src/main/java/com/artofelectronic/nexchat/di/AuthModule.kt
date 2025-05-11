package com.artofelectronic.nexchat.di

import com.artofelectronic.nexchat.data.repository.FirebaseAuthRepository
import com.artofelectronic.nexchat.data.repository.SignupRepositoryImpl
import com.artofelectronic.nexchat.domain.repository.SignInRepository
import com.artofelectronic.nexchat.domain.repository.SignUpRepository
import com.artofelectronic.nexchat.domain.usecases.CheckUserSignInStatusUseCase
import com.artofelectronic.nexchat.domain.usecases.SignupWithEmailUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideSignInRepository(): SignInRepository {
        return FirebaseAuthRepository()
    }

    @Provides
    @Singleton
    fun provideCheckUserSignInStatusUseCase(signInRepository: SignInRepository): CheckUserSignInStatusUseCase {
        return CheckUserSignInStatusUseCase(signInRepository)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSignupRepository(): SignUpRepository {
        return SignupRepositoryImpl(
            auth = provideFirebaseAuth()
        )
    }

    @Provides
    @Singleton
    fun provideSignupWithEmailUseCase(signupRepository: SignUpRepository): SignupWithEmailUseCase {
        return SignupWithEmailUseCase(signupRepository)
    }
}