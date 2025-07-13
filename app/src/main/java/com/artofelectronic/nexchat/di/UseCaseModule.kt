package com.artofelectronic.nexchat.di

import com.artofelectronic.nexchat.domain.repository.AuthRepository
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import com.artofelectronic.nexchat.domain.repository.IChatRoomRepository
import com.artofelectronic.nexchat.domain.repository.MessageRepository
import com.artofelectronic.nexchat.domain.repository.UserRepository
import com.artofelectronic.nexchat.domain.usecases.CheckUserLoggedInUseCase
import com.artofelectronic.nexchat.domain.usecases.CreateUserInFirestoreUseCase
import com.artofelectronic.nexchat.domain.usecases.GetInitialChatRoomInformation
import com.artofelectronic.nexchat.domain.usecases.LoadConversationsUseCase
import com.artofelectronic.nexchat.domain.usecases.LoadUsersUseCase
import com.artofelectronic.nexchat.domain.usecases.RetrieveMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.SendTextMessageUseCase
import com.artofelectronic.nexchat.domain.usecases.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignupWithEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCheckUserSignInStatusUseCase(authRepository: AuthRepository): CheckUserLoggedInUseCase {
        return CheckUserLoggedInUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignupWithEmailUseCase(authRepository: AuthRepository): SignupWithEmailUseCase {
        return SignupWithEmailUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignInWithEmailUseCase(authRepository: AuthRepository): SignInWithEmailUseCase {
        return SignInWithEmailUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSendPasswordResetEmailUseCase(authRepository: AuthRepository): SendPasswordResetEmailUseCase {
        return SendPasswordResetEmailUseCase(authRepository)
    }

    @Provides
    fun provideLoadUsersUseCase(repository: UserRepository): LoadUsersUseCase {
        return LoadUsersUseCase(repository)
    }

    @Provides
    fun provideCreateUserInFirestoreUseCase(repository: UserRepository): CreateUserInFirestoreUseCase {
        return CreateUserInFirestoreUseCase(repository)
    }

    @Provides
    fun provideLoadConversationsUseCase(repository: ChatRepository): LoadConversationsUseCase {
        return LoadConversationsUseCase(repository)
    }

    @Provides
    fun provideRetrieveMessagesUseCase(repository: MessageRepository): RetrieveMessagesUseCase {
        return RetrieveMessagesUseCase(repository)
    }

    @Provides
    fun provideSendMessageUseCase(repository: MessageRepository): SendTextMessageUseCase {
        return SendTextMessageUseCase(repository)
    }

    @Provides
    fun provideGetInitialChatRoomInformation(repository: IChatRoomRepository): GetInitialChatRoomInformation {
        return GetInitialChatRoomInformation(repository)
    }
}