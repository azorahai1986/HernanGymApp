package com.hernan.gymapp.user_type_selected.domain.repository

import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import kotlinx.coroutines.flow.Flow

interface UserTypeSelectedRepository {
    fun getUserByType(userTypeId: String): Flow<ResourceFlow<List<UserTypeSelected>>>
    fun getAllUsers(): Flow<ResourceFlow<List<UserTypeSelected>>>
    suspend fun getUserById(documentId: String): ResourceFlow<UserTypeSelected>
    suspend fun updateSchedule(
        documentId: String,
        preferredDays: List<Int>,
        preferredHours: Map<String, String>,
        weeklyClassLimit: Int,
        scheduleMode: String
    ): ResourceFlow<Unit>
}
