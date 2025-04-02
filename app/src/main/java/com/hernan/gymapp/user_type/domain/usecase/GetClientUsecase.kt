package com.hernan.gymapp.user_type.domain.usecase

import com.hernan.gymapp.user_type.domain.model.Client
import com.hernan.gymapp.user_type.domain.repository.ClientRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientUsecase @Inject constructor(
    private val repository: ClientRepository
) {
    operator fun invoke(clientId: String): Flow<ResourceFlow<List<Client>>> {
        return repository.getClient(clientId)
    }
}