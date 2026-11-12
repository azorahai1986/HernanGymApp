package com.hernan.gymapp.calendary.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.presentation.view.SCHEDULE_FIXED
import com.hernan.gymapp.calendary.presentation.view.WeeklyScheduleSelector
import com.hernan.gymapp.calendary.presentation.viewmodel.ShiftViewModel
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.navigation.Screens
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserDetailViewModel

@Composable
fun CalendaryScreen(
    navController: NavHostController,
    userDetailViewModel: UserDetailViewModel = hiltViewModel(),
    shiftViewModel: ShiftViewModel = hiltViewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val userLoad by userDetailViewModel.userLoad.collectAsState()
    val user by userDetailViewModel.selectedUser.collectAsState()
    val saveState by userDetailViewModel.scheduleSave.collectAsState()
    val shiftsState by shiftViewModel.shift.collectAsState()
    val shifts = (shiftsState as? ResourceFlow.Success)?.data.orEmpty()

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            userDetailViewModel.loadUserById(userId)
            shiftViewModel.getAllShifts()
        }
    }

    Scaffold(
        topBar = { ToolbarUsersType("Calendario") },
        containerColor = MaterialTheme.colorScheme.background
    ) { insets ->
        CalendarEditor(
            user = user?.takeIf { it.documentId == userId },
            userId = userId,
            userLoad = userLoad,
            shifts = shifts,
            shiftsState = shiftsState,
            saveState = saveState,
            viewModel = userDetailViewModel,
            shiftViewModel = shiftViewModel,
            onHome = { goHome(navController) },
            modifier = Modifier.fillMaxSize().padding(insets)
        )
    }
}

@Composable
private fun CalendarEditor(
    user: UserTypeSelected?,
    userId: String,
    userLoad: ResourceFlow<UserTypeSelected>,
    shifts: List<Shift>,
    shiftsState: ResourceFlow<List<Shift>>,
    saveState: ResourceFlow<Unit>,
    viewModel: UserDetailViewModel,
    shiftViewModel: ShiftViewModel,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDays by remember(user?.documentId) { mutableStateOf(user?.preferredDays?.toSet().orEmpty()) }
    var selectedHours by remember(user?.documentId) { mutableStateOf(user?.preferredHours.orEmpty()) }
    var weeklyClassLimit by remember(user?.documentId) { mutableStateOf(user?.weeklyClassLimit ?: 2) }
    var scheduleMode by remember(user?.documentId) { mutableStateOf(user?.scheduleMode?.ifBlank { SCHEDULE_FIXED } ?: SCHEDULE_FIXED) }
    val unchanged = user != null && selectedDays == user.preferredDays.toSet() &&
        selectedHours == user.preferredHours && weeklyClassLimit == user.weeklyClassLimit &&
        scheduleMode == user.scheduleMode

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    user?.name?.takeIf { it.isNotBlank() }?.let { "Hola, $it" } ?: "Calendario semanal",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("Elegí tus días y horarios", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            TextButton(onClick = onHome) { Text("Inicio") }
        }

        when {
            userId.isBlank() -> Text(
                "Iniciá sesión para guardar tus preferencias.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            userLoad is ResourceFlow.Loading || userLoad is ResourceFlow.Idle -> Text(
                "Cargando tus preferencias guardadas…",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            userLoad is ResourceFlow.Error -> Text(
                "Todavía no hay preferencias guardadas. Elegí tus días y horarios para empezar.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        when (shiftsState) {
            is ResourceFlow.Loading, is ResourceFlow.Idle -> Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) { CircularProgressIndicator() }
            is ResourceFlow.Error -> Column {
                Text("No pudimos cargar los horarios disponibles.", color = MaterialTheme.colorScheme.error)
                TextButton(onClick = { shiftViewModel.getAllShifts() }) { Text("Reintentar") }
            }
            is ResourceFlow.Success -> Unit
        }

        WeeklyScheduleSelector(
            selectedDays = selectedDays,
            onSelectedDaysChange = { days ->
                selectedDays = days
                selectedHours = selectedHours.filterKeys { key ->
                    key.toIntOrNull()?.let { it in days } == true
                }
                viewModel.clearScheduleStatus()
            },
            selectedHours = selectedHours,
            onSelectedHoursChange = {
                selectedHours = it
                viewModel.clearScheduleStatus()
            },
            weeklyClassLimit = weeklyClassLimit,
            onWeeklyClassLimitChange = { limit ->
                weeklyClassLimit = limit
                val limitedDays = selectedDays.sorted().take(limit).toSet()
                selectedDays = limitedDays
                selectedHours = selectedHours.filterKeys { key ->
                    key.toIntOrNull()?.let { it in limitedDays } == true
                }
                viewModel.clearScheduleStatus()
            },
            scheduleMode = scheduleMode,
            onScheduleModeChange = {
                scheduleMode = it
                viewModel.clearScheduleStatus()
            },
            shifts = shifts
        )

        if (saveState is ResourceFlow.Error) {
            Text(saveState.message, color = MaterialTheme.colorScheme.error)
        }
        if (saveState is ResourceFlow.Success && unchanged) {
            Text("Tus turnos quedaron guardados.", color = MaterialTheme.colorScheme.tertiary)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    viewModel.saveSchedule(
                        selectedDays.sorted(), selectedHours, weeklyClassLimit, scheduleMode
                    )
                },
                enabled = user != null && shiftsState is ResourceFlow.Success &&
                    selectedDays.size == weeklyClassLimit &&
                    selectedDays.all { it.toString() in selectedHours } &&
                    !unchanged && saveState !is ResourceFlow.Loading,
                modifier = Modifier.weight(1f)
            ) {
                if (saveState is ResourceFlow.Loading) CircularProgressIndicator()
                else Text("Guardar turnos")
            }
            TextButton(onClick = onHome) { Text("Ir a inicio") }
        }
    }
}

private fun goHome(navController: NavHostController) {
    navController.navigate(Screens.MainScreen.routes) {
        popUpTo(Screens.CalendaryScreen.routes) { inclusive = true }
        launchSingleTop = true
    }
}
