package com.hernan.gymapp.main_screen.common.presentation.ui.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.navigation.Screens
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserDetailViewModel
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserTypeSelectedVM
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.ALL_USERS_FILTER

@Composable
fun MainScreen(
    navController: NavHostController,
    userDetailViewModel: UserDetailViewModel
) {
    val auth = remember { FirebaseAuth.getInstance() }
    var userId by remember { mutableStateOf(auth.currentUser?.uid.orEmpty()) }
    val userLoad by userDetailViewModel.userLoad.collectAsState()
    val loadedUser by userDetailViewModel.selectedUser.collectAsState()
    val member = loadedUser?.takeIf { it.documentId == userId }

    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            userId = firebaseAuth.currentUser?.uid.orEmpty()
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) userDetailViewModel.loadUserById(userId)
    }

    when {
        userId.isNotBlank() && (userLoad is ResourceFlow.Loading || userLoad is ResourceFlow.Idle) -> MainScreenLoading()
        member?.isAdministrator() == true -> AdminHome(navController, resolveAdminName(member.name))
        member != null -> MemberHome(member, navController)
        // Until login provides an explicit role, a missing Members/{uid} profile uses the admin home.
        userId.isBlank() -> AdminHome(navController, resolveAdminName())
        userLoad is ResourceFlow.Error && (userLoad as ResourceFlow.Error).message == "No encontramos el perfil del socio" -> AdminHome(navController, resolveAdminName())
        userLoad is ResourceFlow.Error -> ProfileLoadError { userDetailViewModel.loadUserById(userId) }
        else -> AdminHome(navController, resolveAdminName())
    }
}

@Composable
private fun MainScreenLoading() {
    Scaffold(
        topBar = { ToolbarUsersType("Inicio") },
        containerColor = MaterialTheme.colorScheme.background
    ) { insets ->
        Box(
            modifier = Modifier.fillMaxSize().padding(insets),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    }
}

@Composable
private fun ProfileLoadError(onRetry: () -> Unit) {
    Scaffold(
        topBar = { ToolbarUsersType("Inicio") },
        containerColor = MaterialTheme.colorScheme.background
    ) { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets).padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No pudimos verificar el perfil. Revisá la conexión e intentá de nuevo.")
            androidx.compose.material3.TextButton(onClick = onRetry) { Text("Reintentar") }
        }
    }
}

@Composable
private fun AdminHome(navController: NavHostController, adminName: String) {
    Scaffold(
        topBar = { ToolbarUsersType(adminName) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { HomeBottomBar(navController, isAdmin = true) }
    ) { insets ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(insets),
            contentPadding = PaddingValues(
                start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Hola, ${adminName.substringBefore(' ')}", style = MaterialTheme.typography.headlineSmall)
                        Text("Tu club en un vistazo · hoy", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    androidx.compose.material3.Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text("ADMINISTRADOR", modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            item { AdminHeroCard(onClick = { openUsers(navController) }) }
            item {
                DashboardSection(title = "Estado del gimnasio") {
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        DashboardStat("Pagos al día", "103", Icons.Filled.CheckCircle, Modifier.weight(1f))
                        DashboardStat("Pendientes", "18", Icons.Filled.Warning, Modifier.weight(1f), true)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        DashboardStat("Nuevos este mes", "4", Icons.Filled.PersonAdd, Modifier.weight(1f))
                        DashboardStat("Vencen en 7 días", "7", Icons.Filled.Schedule, Modifier.weight(1f), true)
                    }
                    Text("Datos simulados", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item {
                DashboardSection(title = "Atención rápida") {
                    DashboardRow(
                        title = "Pagos pendientes",
                        detail = "Socios con pago pendiente",
                        value = "18 socios",
                        highlight = true,
                        onClick = { openUsers(navController) }
                    )
                    DashboardRow(
                        title = "Vencimientos cercanos",
                        detail = "Entre hoy y los próximos 7 días",
                        value = "7 socios",
                        highlight = true,
                        onClick = { openUsers(navController) }
                    )
                    DashboardRow(
                        title = "Clases por agotarse",
                        detail = "2 clases restantes o menos",
                        value = "5 socios",
                        onClick = { openUsers(navController) }
                    )
                }
            }
            item {
                DashboardSection(title = "Movimiento de hoy") {
                    DashboardRow("08:00", "Mañana", "4 / 5 lugares", onClick = { openCalendar(navController) })
                    DashboardRow("10:00", "Mañana", "Completo · 5 / 5", highlight = true, onClick = { openCalendar(navController) })
                    DashboardRow("18:00", "Tarde", "3 / 5 lugares", onClick = { openCalendar(navController) })
                    DashboardRow("Ver calendario", "Consultar o editar turnos", "›", onClick = { openCalendar(navController) })
                }
            }
            item {
                Text("Accesos", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        HomeAction("Usuarios", "Socios", Icons.Filled.People, Modifier.weight(1f)) { openUsers(navController) }
                        HomeAction("Calendario", "Turnos", Icons.Filled.CalendarMonth, Modifier.weight(1f)) { openCalendar(navController) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        HomeAction("Rutinas", "Ejercicios", Icons.Filled.FitnessCenter, Modifier.weight(1f)) {
                            navController.navigate("${Screens.RoutineScreen.routes}/${Uri.encode("Rutinas")}")
                        }
                        HomeAction("QR Scanner", "Asistencia", Icons.Filled.QrCodeScanner, Modifier.weight(1f)) {
                            navController.navigate("${Screens.QRScannerScreen.routes}/${Uri.encode("QR Scanner")}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardStat(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    highlight: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(74.dp).padding(horizontal = 10.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(34.dp).clip(RoundedCornerShape(11.dp))
                    .background(if (highlight) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (highlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    value,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (highlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AdminHeroCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("SOCIOS REGISTRADOS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                Text("128", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text("Tocá para consultar todos los perfiles ›", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.People, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun DashboardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 7.dp))
            content()
        }
    }
}

@Composable
private fun DashboardRow(
    title: String,
    detail: String,
    value: String,
    highlight: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    androidx.compose.material3.Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth().then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                value,
                style = MaterialTheme.typography.labelLarge,
                color = if (highlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}

@Composable
private fun HomeAction(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(13.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MemberHome(user: UserTypeSelected, navController: NavHostController) {
    val weekdays = listOf("", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val schedule = user.preferredDays.sorted().mapNotNull { day ->
        val name = weekdays.getOrNull(day) ?: return@mapNotNull null
        val hour = user.preferredHours[day.toString()].orEmpty()
        "$name${if (hour.isNotBlank()) " · $hour" else ""}"
    }

    Scaffold(
        topBar = { ToolbarUsersType("Inicio") },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { HomeBottomBar(navController, isAdmin = false) }
    ) { insets ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(insets),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Hola, ${user.name.ifBlank { "socio" }}", style = MaterialTheme.typography.headlineSmall)
            }
            item {
                DashboardSection(title = "Mis datos") {
                    Text("Tipo: ${user.typeId} · Género: ${user.gender}", style = MaterialTheme.typography.bodySmall)
                    Text("Edad: ${user.edad} · Peso: ${user.weight}", style = MaterialTheme.typography.bodySmall)
                    Text("Teléfono: ${user.phoneNumber}", style = MaterialTheme.typography.bodySmall)
                    Text("Salud: ${user.estadoSalud} · Objetivo: ${user.objetivo}", style = MaterialTheme.typography.bodySmall)
                    DashboardRow("Plan", "${user.weeklyClassLimit} días por semana", "${user.classes} clases")
                    DashboardRow("Turnos", schedule.joinToString(" · ").ifBlank { "Aún no elegidos" }, "›") {
                        openCalendar(navController)
                    }
                    DashboardRow(
                        "Pago",
                        if (user.pay) "Tu cuota está al día" else "Tu pago está pendiente",
                        if (user.pay) "Al día" else "Pendiente",
                        highlight = !user.pay,
                    )
                }
            }
            item { HomeAction("Calendario", "Ver o cambiar tus días y horarios", Icons.Filled.CalendarMonth) { openCalendar(navController) } }
            item { HomeAction("Rutinas", "Consultar tus ejercicios", Icons.Filled.FitnessCenter) { navController.navigate("${Screens.RoutineScreen.routes}/${Uri.encode("Rutinas")}") } }
            item { HomeAction("QR Scanner", "Registrar tu asistencia", Icons.Filled.QrCodeScanner) { navController.navigate("${Screens.QRScannerScreen.routes}/${Uri.encode("QR Scanner")}") } }
        }
    }
}

private data class HomeDestination(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
private fun HomeBottomBar(navController: NavHostController, isAdmin: Boolean) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route.orEmpty()
    val destinations = if (isAdmin) {
        listOf(
            HomeDestination("Inicio", Screens.MainScreen.routes, Icons.Filled.Home),
            HomeDestination("Usuarios", "${Screens.UsersScreen.routes}/$ALL_USERS_FILTER", Icons.Filled.People),
            HomeDestination("Calendario", Screens.CalendaryScreen.routes, Icons.Filled.CalendarMonth),
            HomeDestination("Rutinas", "${Screens.RoutineScreen.routes}/${Uri.encode("Rutinas")}", Icons.Filled.FitnessCenter)
        )
    } else {
        listOf(
            HomeDestination("Inicio", Screens.MainScreen.routes, Icons.Filled.Home),
            HomeDestination("Calendario", Screens.CalendaryScreen.routes, Icons.Filled.CalendarMonth),
            HomeDestination("Rutinas", "${Screens.RoutineScreen.routes}/${Uri.encode("Rutinas")}", Icons.Filled.FitnessCenter),
            HomeDestination("Asistencia", "${Screens.QRScannerScreen.routes}/${Uri.encode("QR Scanner")}", Icons.Filled.QrCodeScanner)
        )
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp
    ) {
        destinations.forEach { destination ->
            val selected = when (destination.label) {
                "Inicio" -> currentRoute == Screens.MainScreen.routes
                "Usuarios" -> currentRoute.startsWith(Screens.UsersTypeScreen.routes) || currentRoute.startsWith(Screens.UsersScreen.routes)
                "Calendario" -> currentRoute == Screens.CalendaryScreen.routes
                "Rutinas" -> currentRoute.startsWith(Screens.RoutineScreen.routes)
                "Asistencia" -> currentRoute.startsWith(Screens.QRScannerScreen.routes)
                else -> false
            }
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(Screens.MainScreen.routes) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

private fun openUsers(navController: NavHostController) {
    navController.navigate("${Screens.UsersScreen.routes}/$ALL_USERS_FILTER")
}

private fun openCalendar(navController: NavHostController) {
    navController.navigate(Screens.CalendaryScreen.routes)
}

private fun UserTypeSelected.isAdministrator(): Boolean =
    typeId.equals("admin", ignoreCase = true) || typeId.equals("administrador", ignoreCase = true)

private fun resolveAdminName(profileName: String? = null): String {
    val authUser = FirebaseAuth.getInstance().currentUser
    return profileName?.trim()?.takeIf { it.isNotBlank() && '@' !in it }
        ?: authUser?.displayName?.trim()?.takeIf { it.isNotBlank() && '@' !in it }
        ?: "Administrador"
}
