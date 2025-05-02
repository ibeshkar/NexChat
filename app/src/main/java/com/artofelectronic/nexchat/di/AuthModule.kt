package com.artofelectronic.nexchat.di

import com.artofelectronic.nexchat.data.repository.FirebaseAuthRepository
import com.artofelectronic.nexchat.domain.CheckUserSignInStatusUseCase
import com.artofelectronic.nexchat.domain.SignInRepository
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
}