package com.hernan.gymapp.user_type_selected.presentation.ui.views.user_screen_views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import kotlinx.coroutines.CoroutineScope

private enum class MemberFilter { ALL, PAID, PENDING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnToUserList(
    data: List<UserTypeSelected>,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf(MemberFilter.ALL) }

    val filteredUsers = data.filter { user ->
        val matchesQuery = query.isBlank() ||
            user.name.contains(query.trim(), ignoreCase = true) ||
            user.phoneNumber.contains(query.trim(), ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            MemberFilter.ALL -> true
            MemberFilter.PAID -> user.pay
            MemberFilter.PENDING -> !user.pay
        }
        matchesQuery && matchesFilter
    }
    val pendingCount = data.count { !it.pay }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("SOCIOS", style = MaterialTheme.typography.labelSmall)
                        Text(data.size.toString(), style = MaterialTheme.typography.headlineMedium)
                    }
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                        Text("Pagos pendientes", style = MaterialTheme.typography.labelSmall)
                        Text(pendingCount.toString(), style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Buscar socio") },
                placeholder = { Text("Nombre o teléfono") },
                shape = MaterialTheme.shapes.large,
                colors = TextFieldDefaults.colors()
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selectedFilter == MemberFilter.ALL, { selectedFilter = MemberFilter.ALL }, label = { Text("Todos") })
                FilterChip(selectedFilter == MemberFilter.PAID, { selectedFilter = MemberFilter.PAID }, label = { Text("Al día") })
                FilterChip(selectedFilter == MemberFilter.PENDING, { selectedFilter = MemberFilter.PENDING }, label = { Text("Pendiente") })
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Socios", style = MaterialTheme.typography.titleMedium)
                Text("${filteredUsers.size} resultados", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (filteredUsers.isEmpty()) {
            item {
                Text(
                    text = if (data.isEmpty()) "Todavía no hay socios para mostrar." else "No encontramos socios con esos criterios.",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(filteredUsers) { user ->
                UserList(user, bottomSheetScaffoldState, coroutineScope)
            }
        }
    }
}
