package com.hernan.gymapp.user_type_selected.presentation.ui.views.bottomsheet_view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.R
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected

/**
 * Caja donde se inflan los elementos de la lista Lista de info del cliente
 * **/
@Composable
fun InfoBox(user: UserTypeSelected, cliesnState: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(18.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary, // Cambiá este color como quieras
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {

        user.apply {
            Column {
                when(cliesnState) {
                    Constants.personalData -> {
                        InfoRow("Nombre: ${user.name}", R.drawable.user)
                        InfoRow("Telefono: ${user.phoneNumber}", R.drawable.smartphone)
                        InfoRow("Tipo de cliente: ${user.typeId}", R.drawable.avatar)
                    }
                    Constants.health -> {
                        InfoRow("Peso: ${user.weight}kg", R.drawable.perdida_de_peso)
                        InfoRow("Edad: ${user.edad}", R.drawable.age_icon)
                        InfoRow("Metas: ${user.objetivo}", R.drawable.objetivos_icon)
                        InfoRow("Estado Salud: ${user.estadoSalud}", R.drawable.condicion_fisica_femenina)
                    }
                    Constants.payment -> {
                        InfoRow("Clases restantes: ${user.classes}", R.drawable.classes_icon)
                        InfoRowPayment("Estado de Pago:", user.pay, R.drawable.pay_icon)
                    }

                }

            }
        }

    }

}