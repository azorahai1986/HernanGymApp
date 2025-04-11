package com.hernan.gymapp.common.state.module

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.main_screen.data.repository.FirstStateRepositoryImpl
import com.hernan.gymapp.user_type.domain.repository.ClientRepository
import com.hernan.gymapp.main_screen.domain.repository.FirstStateRepository
import com.hernan.gymapp.main_screen.domain.usecase.FirstStateUseCase
import com.hernan.gymapp.routins_and_excercises.data.repository.RoutineRepositoryImpl
import com.hernan.gymapp.routins_and_excercises.domain.repository.RoutineRepository
import com.hernan.gymapp.routins_and_excercises.domain.usecase.RoutineUseCase
import com.hernan.gymapp.user_type.domain.usecase.GetClientUsecase
import com.hernan.gymapp.user_type.data.repository.ClientRepositoryImpl
import com.hernan.gymapp.user_type_selected.data.repository.UserTypeSelectedRepositoryImpl
import com.hernan.gymapp.user_type_selected.domain.repository.UserTypeSelectedRepository
import com.hernan.gymapp.user_type_selected.domain.usecase.UserTypeSelectedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideClientRepository(
        firestore: FirebaseFirestore
    ): ClientRepository {
        return ClientRepositoryImpl(firestore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseClientModule {
    @Provides
    fun provideGetClientUsecase(repository: ClientRepository): GetClientUsecase {
        return GetClientUsecase(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
object FirstTateRepositoryModule {
    @Provides
    @Singleton
    fun provideFirstState(
        firestore: FirebaseFirestore
    ): FirstStateRepository {
        return FirstStateRepositoryImpl(firestore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FirstStateUseCaseModule {
    @Provides
    fun provideFirstStateUsecase(repository: FirstStateRepository): FirstStateUseCase {
        return FirstStateUseCase(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UserTypeSelectedRepositoryModule {
    @Provides
    @Singleton
    fun provideUserTypeSelected(
        firestore: FirebaseFirestore
    ): UserTypeSelectedRepository {
        return UserTypeSelectedRepositoryImpl(firestore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UserTypeSelectedUseCaseModule {
    @Provides
    fun provideUseTypeSelectedUsecase(repository: UserTypeSelectedRepository): UserTypeSelectedUseCase {
        return UserTypeSelectedUseCase(repository)
    }
}

