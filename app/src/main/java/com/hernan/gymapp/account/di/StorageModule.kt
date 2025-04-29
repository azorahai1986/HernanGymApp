package com.hernan.gymapp.account.di

import com.google.firebase.storage.FirebaseStorage
import com.hernan.gymapp.account.data.repository.StorageRepositoryImpl
import com.hernan.gymapp.account.domain.repository.StorageRepository
import com.hernan.gymapp.account.domain.usecase.StorageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideStorageRepository(
        firestore: FirebaseStorage
    ): StorageRepository = StorageRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideStorageUseCase(
        repository: StorageRepository
    ): StorageUseCase = StorageUseCase(repository)
}