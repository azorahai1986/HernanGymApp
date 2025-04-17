package com.hernan.gymapp.main_screen.common.presentation.ui.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.hernan.gymapp.main_screen.admin_qr.domain.model.QrAdminInfo

@Composable
fun QrImage(qrData: List<QrAdminInfo>) {
    val checkInQr = qrData.singleOrNull()?.checkInQr
    val qrBitmap = remember(checkInQr) {
        checkInQr?.let { generateQRCode(it) }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        qrBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.size(128.dp)
            )
        }
    }
}

fun generateQRCode(content: String, size: Int = 512): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
    val bmp = createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp[x, y] = if (bitMatrix.get(
                    x,
                    y
                )
            ) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bmp
}

/**
 * TODO feature/Generate_QR_and_QrName -> Por ahora crearlo en esta Screen -: "Finalizado"
 *   feature/create_Admin_info_Screen
 *   feature/create_Admin_qr_acces
 *   feature/create_admin_clients_screen
 *   feature/create_admin_screen
 *   feature/create_loggin_screen
 *   feature/create_user_person_info
 *
 *  TODO Crear una tarea para incorporar Medios de pago. Probar con
 *   "notification_url": "https://us-central1-TU_PROYECTO.cloudfunctions.net/webhookPago" en FireBse
 *    usar webhooks. Probar si es posible obtener una notificacion cuando se recibe un págo y este queda registrado.
 *    La idea seria generar un QR con el url del Administrador. para que el pago valla a su cuenta.
 *    Al quedar registrado el pago, Firebase avisará que el usuario está al día cn el pago
 *
 * **/