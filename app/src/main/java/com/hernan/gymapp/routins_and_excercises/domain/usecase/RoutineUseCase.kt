package com.hernan.gymapp.routins_and_excercises.domain.usecase

import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.routins_and_excercises.domain.model.RoutinService
import com.hernan.gymapp.routins_and_excercises.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    operator fun invoke(): Flow<ResourceFlow<List<RoutinService>>> = routineRepository.getRoutineVideos()
}