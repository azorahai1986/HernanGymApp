package com.hernan.gymapp.user_type_selected.domain.repository

import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import kotlinx.coroutines.flow.Flow

interface UserTypeSelectedRepository {
    fun getUserByType(userTypeId: String): Flow<ResourceFlow<List<UserTypeSelected>>>
}