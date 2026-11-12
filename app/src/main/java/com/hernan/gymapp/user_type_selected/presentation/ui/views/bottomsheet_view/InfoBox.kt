package com.hernan.gymapp.user_type_selected.presentation.ui.views.bottomsheet_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.R
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected

@Composable
fun InfoBox(user: UserTypeSelected, clientState: String) {
    val title = when (clientState) {
        Constants.personalData -> "Datos personales"
        Constants.health -> "Salud y objetivos"
        Constants.payment -> "Clases y pagos"
        else -> return
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
            when (clientState) {
                Constants.personalData -> {
                    InfoRow("Nombre: ${user.name}", R.drawable.user)
                    InfoRow("Teléfono: ${user.phoneNumber}", R.drawable.smartphone)
                    InfoRow("Tipo de socio: ${user.typeId}", R.drawable.avatar)
                }
                Constants.health -> {
                    InfoRow("Peso: ${user.weight}", R.drawable.perdida_de_peso)
                    InfoRow("Edad: ${user.edad}", R.drawable.age_icon)
                    InfoRow("Objetivo: ${user.objetivo}", R.drawable.objetivos_icon)
                    InfoRow("Estado de salud: ${user.estadoSalud}", R.drawable.condicion_fisica_femenina)
                }
                Constants.payment -> {
                    InfoRow("Clases restantes: ${user.classes}", R.drawable.classes_icon)
                    InfoRowPayment("Estado de pago", user.pay, R.drawable.pay_icon)
                }
            }
        }
    }
}
