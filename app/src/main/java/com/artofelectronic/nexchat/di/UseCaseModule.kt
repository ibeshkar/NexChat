package com.artofelectronic.nexchat.di

import com.artofelectronic.nexchat.domain.repository.AuthRepository
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import com.artofelectronic.nexchat.domain.repository.UserRepository
import com.artofelectronic.nexchat.domain.usecases.auth.GetCurrentUserIdUseCase
import com.artofelectronic.nexchat.domain.usecases.users.CheckUserLoggedInUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.FetchChatsOnceIfNeededUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUserProfileUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.GetMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ObserveChatsUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.ObserveMessagesUseCase
import com.artofelectronic.nexchat.domain.usecases.users.ObserveUsersUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.RefreshChatsUseCase
import com.artofelectronic.nexchat.domain.usecases.users.UserChangeListenerUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.RetryPendingUpdatesUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.auth.SignupWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.chats.CreateOrContinueChatUseCase
import com.artofelectronic.nexchat.domain.usecases.users.FetchUsersUseCase
import com.artofelectronic.nexchat.domain.usecases.users.SignOutUseCase
import com.artofelectronic.nexchat.domain.usecases.users.UpdateAvatarUrlUseCase
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
    fun provideObserveChatsUseCase(repository: ChatRepository): ObserveChatsUseCase {
        return ObserveChatsUseCase(repository)
    }

    @Provides
    fun provideFetchChatsOnceIfNeededUseCase(repository: ChatRepository): FetchChatsOnceIfNeededUseCase {
        return FetchChatsOnceIfNeededUseCase(repository)
    }

    @Provides
    fun provideRefreshChatsUseCase(repository: ChatRepository): RefreshChatsUseCase {
        return RefreshChatsUseCase(repository)
    }

    @Provides
    fun provideRetryPendingUpdatesUseCase(repository: ChatRepository): RetryPendingUpdatesUseCase {
        return RetryPendingUpdatesUseCase(repository)
    }

    @Provides
    fun provideObserveUsersUseCase(repository: UserRepository): ObserveUsersUseCase {
        return ObserveUsersUseCase(repository)
    }

    @Provides
    fun provideRefreshUsersUseCase(repository: UserRepository): UserChangeListenerUseCase {
        return UserChangeListenerUseCase(repository)
    }

    @Provides
    fun provideStartOrGetChatUseCase(repository: ChatRepository): CreateOrContinueChatUseCase {
        return CreateOrContinueChatUseCase(repository)
    }

    @Provides
    fun provideObserveMessagesUseCase(repository: ChatRepository): ObserveMessagesUseCase {
        return ObserveMessagesUseCase(repository)
    }

    @Provides
    fun provideFetchUserProfileUseCase(repository: UserRepository): FetchUserProfileUseCase {
        return FetchUserProfileUseCase(repository)
    }

    @Provides
    fun provideGetMessagesUseCase(repository: ChatRepository): GetMessagesUseCase {
        return GetMessagesUseCase(repository)
    }

    @Provides
    fun provideFetchUsersUseCase(repository: UserRepository): FetchUsersUseCase {
        return FetchUsersUseCase(repository)
    }

    @Provides
    fun provideUpdateAvatarUrlUseCase(repository: UserRepository): UpdateAvatarUrlUseCase {
        return UpdateAvatarUrlUseCase(repository)
    }

    @Provides
    fun provideGetCurrentUserIdUseCase(repository: AuthRepository): GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(repository)
    }

    @Provides
    fun provideSignOutUseCase(repository: UserRepository): SignOutUseCase {
        return SignOutUseCase(repository)
    }

}