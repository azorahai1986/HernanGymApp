package com.hernan.gymapp.account.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.account.data.repository.CreateAdditionalDateRepoImpl
import com.hernan.gymapp.account.domain.repository.CreateAdditionalDateRepo
import com.hernan.gymapp.account.domain.usecase.AddtionalDateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdditionalDateModule {

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore
    ): CreateAdditionalDateRepo = CreateAdditionalDateRepoImpl(firestore)

    @Provides
    @Singleton
    fun provideSetDocumentUseCase(
        repository: CreateAdditionalDateRepo
    ): AddtionalDateUseCase = AddtionalDateUseCase(repository)
}