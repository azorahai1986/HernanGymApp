package com.hernan.gymapp.account.domain.usecase

import com.hernan.gymapp.account.domain.model.AccountData
import com.hernan.gymapp.account.domain.repository.CreateAccountRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val createAccountRepository: CreateAccountRepository
) {
    operator fun invoke(email: String, password: String): Flow<ResourceFlow<AccountData>> =
        createAccountRepository.createAccount(email, password)
}