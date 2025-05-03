package com.hernan.gymapp.account.di

import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.account.data.repository.CreateAccountRepositoryImpl
import com.hernan.gymapp.account.domain.repository.CreateAccountRepository
import com.hernan.gymapp.account.domain.usecase.AccountUseCase
import com.hernan.gymapp.user_type_selected.data.remote.FirebaseAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuthService(
        firebaseAuth: FirebaseAuth
    ): FirebaseAuthService = FirebaseAuthService(firebaseAuth)

    @Provides
    @Singleton
    fun provideCreateAccountRepository(
        authService: FirebaseAuthService
    ): CreateAccountRepository = CreateAccountRepositoryImpl(authService)

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(
        repository: CreateAccountRepository
    ): AccountUseCase = AccountUseCase(repository)
}