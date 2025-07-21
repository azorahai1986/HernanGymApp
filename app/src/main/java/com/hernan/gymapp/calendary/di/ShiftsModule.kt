package com.hernan.gymapp.calendary.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.account.data.repository.CreateAdditionalDateRepoImpl
import com.hernan.gymapp.account.domain.repository.CreateAdditionalDateRepo
import com.hernan.gymapp.account.domain.usecase.AddtionalDateUseCase
import com.hernan.gymapp.calendary.data.repository.CalendarRepositoryImpl
import com.hernan.gymapp.calendary.domain.repository.CalendarRepository
import com.hernan.gymapp.calendary.domain.usecase.LocalDateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShiftsModule {

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore
    ): CalendarRepository  = CalendarRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideSetDocumentUseCase(
        repository: CalendarRepository
    ): LocalDateUseCase = LocalDateUseCase(repository)

}