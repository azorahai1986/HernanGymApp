package com.hernan.gymapp.user_type.domain.repository

import com.hernan.gymapp.user_type.domain.model.Client
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getClient(clientId: String): Flow<ResourceFlow<List<Client>>>
}