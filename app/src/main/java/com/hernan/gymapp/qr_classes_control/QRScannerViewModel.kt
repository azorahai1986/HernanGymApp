package com.hernan.gymapp.qr_classes_control

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class QRScannerViewModel : ViewModel() {

    // Opciones: Solo escanea QR codes
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

    private val _qrCodeData = MutableStateFlow("Escanea un código QR")
    val qrCodeData: StateFlow<String> = _qrCodeData

    // Evitar múltiples lecturas del mismo QR
    private var alreadyScanned = false

    suspend fun scanQRCode(image: InputImage) {
        if (alreadyScanned) return

        try {
            val barcodes = scanner.process(image).await()
            if (barcodes.isNotEmpty()) {
                val barcode = barcodes.first()
                _qrCodeData.value = barcode.displayValue ?: "QR vacío"
                alreadyScanned = true // ← evita nuevas lecturas
            } else {
                _qrCodeData.value = "No se detectó ningún código"
            }
        } catch (e: Exception) {
            _qrCodeData.value = "Error al leer QR: ${e.message}"
        }
    }
}