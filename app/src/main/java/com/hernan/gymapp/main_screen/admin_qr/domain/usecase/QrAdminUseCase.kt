package com.hernan.gymapp.main_screen.admin_qr.domain.usecase

import com.hernan.gymapp.main_screen.admin_qr.domain.repository.QrAdminRepository
import javax.inject.Inject

class QrAdminUseCase @Inject constructor(
    private val qrAdminRepository: QrAdminRepository
) {

    operator fun invoke() = qrAdminRepository.getQrAdminList()
}

/**
 *
 * Para recordar como devolver un Flow<String?>
 * operator fun invoke(): Flow<String?> {
 *         return qrAdminRepository.getQrAdminList()
 *             .mapNotNull { it as? ResourceFlow.Success }
 *             .map { it.data.firstOrNull()?.aliasQr }
 *     }
 *
 * .mapNotNull { it as? ResourceFlow.Success }
 * as?hace un reparto seguro, por lo tanto: si it es un ResourceFlow.Success, lo castea; si no, devuelve null.
 * mapNotNull descarta los null, por lo tanto solo deja pasar losSuccess .
 *
 *
 * .map { it.data.firstOrNull()?.aliasQr }
 * Para cada Success, accede al campo data(que es la List<QrAdminInfo>)
 *  Usa firstOrNull()para tomar el primer elemento de la lista , si existe.
 *  Luego accede a su propiedad aliasQr.
 *  El resultado de esta línea es un Flow<String?>(porque firstOrNull()y aliasQrpueden ser null).
 *
 *  .firstOrNull()
 *  Esta línea suspende la ejecución hasta que el Flow emita el primer valor no nulo (si es que emite algo).
 *  Si no se emite ningún valor válido, retorna null.
 * **/