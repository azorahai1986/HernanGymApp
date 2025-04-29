package com.hernan.gymapp.account.domain.repository

import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun uploadProfileImage(userId: String, imageBytes: ByteArray): Flow<ResourceFlow<String>>
}