package com.hernan.gymapp.user_type_selected.domain.usecase

import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.domain.repository.UserTypeSelectedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserTypeSelectedUseCase @Inject constructor(
    private val userTypeSeledRepository: UserTypeSelectedRepository
) {
    operator fun invoke(userTypeId: String): Flow<ResourceFlow<List<UserTypeSelected>>> {
        return userTypeSeledRepository.getUserByType(userTypeId)
    }

    fun getAllUsers(): Flow<ResourceFlow<List<UserTypeSelected>>> = userTypeSeledRepository.getAllUsers()

    suspend fun getUserById(documentId: String): ResourceFlow<UserTypeSelected> =
        userTypeSeledRepository.getUserById(documentId)

    suspend fun updateSchedule(
        documentId: String,
        preferredDays: List<Int>,
        preferredHours: Map<String, String>,
        weeklyClassLimit: Int,
        scheduleMode: String
    ): ResourceFlow<Unit> = userTypeSeledRepository.updateSchedule(
        documentId, preferredDays, preferredHours, weeklyClassLimit, scheduleMode
    )
}
