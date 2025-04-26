package com.hernan.gymapp.account.data.repository

import com.hernan.gymapp.account.data.model.AccountDataDto
import com.hernan.gymapp.account.domain.model.AccountData
import com.hernan.gymapp.account.domain.repository.CreateAccountRepository
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.data.remote.FirebaseAuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CreateAccountRepositoryImpl @Inject constructor(
    private val authService: FirebaseAuthService
): CreateAccountRepository {
    override fun createAccount(accountData: AccountData): Flow<ResourceFlow<AccountData>> = flow {
        emit(ResourceFlow.Loading)
        try {
            authService.register(accountData)
            val user = authService.getCurrentUser()
            if (user != null) {
                val dto = AccountDataDto(name = user.displayName ?: "", email = user.email ?: "")
                emit(ResourceFlow.Success(dto.toDomain()))
            } else {
                emit(ResourceFlow.Error("User is null"))
            }
        } catch (e: Exception) {
            emit(ResourceFlow.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}