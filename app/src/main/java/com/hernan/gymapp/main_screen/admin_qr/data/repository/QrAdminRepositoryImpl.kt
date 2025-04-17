package com.hernan.gymapp.main_screen.admin_qr.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.main_screen.admin_qr.data.model.QrAdminInfoDto
import com.hernan.gymapp.main_screen.admin_qr.domain.model.QrAdminInfo
import com.hernan.gymapp.main_screen.admin_qr.domain.repository.QrAdminRepository
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class QrAdminRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : QrAdminRepository {
    override fun getQrAdminList(): Flow<ResourceFlow<List<QrAdminInfo>>> = callbackFlow {
        trySend(ResourceFlow.Loading)

        val listenerRegistration = firestore.collection("QrValuesString")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResourceFlow.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val qrAdminList = snapshot.documents.mapNotNull {
                        it.toObject(QrAdminInfoDto::class.java)?.toDomain()
                    }

                    if (qrAdminList.isNotEmpty()) {
                        trySend(ResourceFlow.Success(qrAdminList))
                    } else {
                        trySend(ResourceFlow.Error("No hay QRs disponibles"))
                    }
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }.distinctUntilChanged()
}