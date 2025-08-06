package com.hernan.gymapp.calendary.domain.repository

import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface GetShiftRepository {
    fun getAllShift(): Flow<ResourceFlow<List<Shift>>>
}