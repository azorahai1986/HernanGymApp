package com.hernan.gymapp.routins_and_excercises.domain.repository

import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.routins_and_excercises.domain.model.RoutinService
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getRoutineVideos(): Flow<ResourceFlow<List<RoutinService>>>
}