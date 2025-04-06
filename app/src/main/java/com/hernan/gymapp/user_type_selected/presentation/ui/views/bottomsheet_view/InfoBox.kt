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

        // TODO Dentro de este when hay que emprolijar la condicion.
        //  los PersonalData, Health y Payment. Deberian estar en la base de datos como  categorias.
        //  Proximo paso a seguir: ver con el cliente sobre que valores se deberian sumar para filtrarlos luego


        //TODO Va a ser mejor que los Iconos no vengan de firebase. que esten dentro de la app.
        user.apply {
            Column {
                when(cliesnState) {
                    "PersonalData" -> {
                        InfoRow("Nombre: ${user.name}", user.userImage)
                        InfoRow("Telefono: ${user.phoneNumber}", user.phoneIcon)
                        InfoRow("Tipo de cliente: ${user.typeId}", user.typeUserIcon)
                    }
                    "Health" -> {
                        InfoRow("Peso: ${user.weight}kg", user.weightIcon)
                        InfoRow("Edad: ${user.edad}", user.edadIcon)
                        InfoRow("Metas: ${user.objetivo}", user.objetivoIcon)
                        InfoRow("Estado Salud: ${user.estadoSalud}", user.estadoSaludIcon)
                    }
                    "Payment" -> {
                        InfoRow("Clases restantes: ${user.classes}", user.classesIcon)
                        InfoRowPayment("Estado de Pago:", user.pay, user.payIcon)
                    }

                }

            }
        }

    }

}