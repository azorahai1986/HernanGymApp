package com.hernan.gymapp.main_screen.list_elements.domain.repository

import com.hernan.gymapp.main_screen.list_elements.domain.model.FirstState
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface FirstStateRepository {
    fun getFirstState(): Flow<ResourceFlow<List<FirstState>>>
}