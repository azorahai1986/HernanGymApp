package com.hernan.gymapp.main_screen.admin_qr.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.main_screen.admin_qr.data.repository.QrAdminRepositoryImpl
import com.hernan.gymapp.main_screen.admin_qr.domain.repository.QrAdminRepository
import com.hernan.gymapp.main_screen.admin_qr.domain.usecase.QrAdminUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QrModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object QrAdminRepositoryModule {
        @Provides
        @Singleton
        fun provideQrAdmin(
            firestore: FirebaseFirestore
        ): QrAdminRepository {
            return QrAdminRepositoryImpl(firestore)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object QrAdminUseCaseModule {
        @Provides
        fun provideQrAdminUsecase(
            qrAdminRepository: QrAdminRepository
        ): QrAdminUseCase {
            return QrAdminUseCase(qrAdminRepository)
        }
    }
}