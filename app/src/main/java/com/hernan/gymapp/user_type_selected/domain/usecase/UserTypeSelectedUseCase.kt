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
}