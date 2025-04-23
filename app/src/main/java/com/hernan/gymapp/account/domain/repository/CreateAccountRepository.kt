package com.hernan.gymapp.account.domain.repository

import com.google.firebase.auth.AuthResult
import com.hernan.gymapp.account.domain.model.AccountData
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface CreateAccountRepository {
    fun createAccount(email: String, password: String): Flow<ResourceFlow<AccountData>>
}