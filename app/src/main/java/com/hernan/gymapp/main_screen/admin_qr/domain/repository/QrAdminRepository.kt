package com.hernan.gymapp.main_screen.admin_qr.domain.repository

import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.main_screen.admin_qr.domain.model.QrAdminInfo
import kotlinx.coroutines.flow.Flow

interface QrAdminRepository {
    fun getQrAdminList(): Flow<ResourceFlow<List<QrAdminInfo>>>
}