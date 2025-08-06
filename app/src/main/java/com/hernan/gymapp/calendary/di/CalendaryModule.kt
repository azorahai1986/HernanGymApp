package com.hernan.gymapp.calendary.di

import com.hernan.gymapp.calendary.data.repository.GetShiftRepositoryImpl
import com.hernan.gymapp.calendary.domain.repository.GetShiftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CalendaryModule {

    @Binds
    abstract fun bindGetShiftRepository(
        impl: GetShiftRepositoryImpl
    ): GetShiftRepository
}