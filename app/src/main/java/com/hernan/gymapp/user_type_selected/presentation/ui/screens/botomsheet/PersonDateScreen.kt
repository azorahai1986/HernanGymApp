package com.hernan.gymapp.user_type_selected.presentation.ui.screens.botomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hernan.gymapp.R
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.presentation.ui.views.bottomsheet_view.InfoBox
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserDetailViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PersonDateScreen(
    onClose: () -> Unit,
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {
    val user by userDetailViewModel.selectedUser.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val selectedUser = user
        if (selectedUser == null) {
            Text("Seleccioná un socio para ver su ficha.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            MemberDetails(user = selectedUser, onClose = onClose)
        }
    }
}

@Composable
private fun MemberDetails(user: UserTypeSelected, onClose: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ficha del socio", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = onClose) { Text("Cerrar") }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            GlideImage(
                imageModel = { if (user.userImage.isBlank()) R.drawable.avatar else user.userImage },
                modifier = Modifier.size(82.dp).clip(CircleShape)
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(user.name.ifBlank { "Socio sin nombre" }, style = MaterialTheme.typography.titleLarge)
                Text(user.typeId.ifBlank { "Socio" }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    if (user.pay) "Pago al día" else "Pago pendiente",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (user.pay) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { InfoBox(user, "PersonalData") }
            item { InfoBox(user, "Health") }
            item { InfoBox(user, "Payment") }
            item { ScheduleSummary(user) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ScheduleSummary(user: UserTypeSelected) {
    val days = listOf("", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val selected = user.preferredDays.sorted().mapNotNull { day ->
        val dayName = days.getOrNull(day) ?: return@mapNotNull null
        val hour = user.preferredHours[day.toString()].orEmpty()
        "$dayName${if (hour.isNotBlank()) " · $hour" else ""}"
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("Plan de turnos", style = MaterialTheme.typography.titleMedium)
            Text("${user.weeklyClassLimit} días por semana", style = MaterialTheme.typography.bodyMedium)
            Text(
                selected.takeIf { it.isNotEmpty() }?.joinToString(" · ") ?: "Todavía no eligió días y horarios.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                if (user.scheduleMode == "FLEXIBLE") "Horario flexible" else "Horario fijo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
