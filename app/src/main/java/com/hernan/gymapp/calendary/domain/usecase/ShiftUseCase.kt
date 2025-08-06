package com.hernan.gymapp.calendary.domain.usecase

import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.domain.repository.GetShiftRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShiftUseCase @Inject constructor(
    private val getShiftRepository: GetShiftRepository
) {
    suspend operator fun invoke(): Flow<ResourceFlow<List<Shift>>> =
        getShiftRepository.getAllShift()
}