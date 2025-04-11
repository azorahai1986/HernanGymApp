package com.hernan.gymapp.routins_and_excercises.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.routins_and_excercises.data.repository.RoutineRepositoryImpl
import com.hernan.gymapp.routins_and_excercises.domain.repository.RoutineRepository
import com.hernan.gymapp.routins_and_excercises.domain.usecase.RoutineUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoutineModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object RoutineRepositoryModule {
        @Provides
        @Singleton
        fun provideRoutineSelected(
            firestore: FirebaseFirestore
        ): RoutineRepository {
            return RoutineRepositoryImpl(firestore)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object RoutineUseCaseModule {
        @Provides
        fun provideRoutineUsecase(
            repository: RoutineRepository
        ): RoutineUseCase {
            return RoutineUseCase(repository)
        }
    }
}