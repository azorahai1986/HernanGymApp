package com.hernan.gymapp.user_type_selected.presentation.ui.views.user_screen_views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hernan.gymapp.R
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserDetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(
    users: UserTypeSelected,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = {
            userDetailViewModel.setSelectedUser(users)
            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            GlideImage(
                imageModel = { if (users.userImage.isBlank()) R.drawable.avatar else users.userImage },
                modifier = Modifier.size(52.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(users.name.ifBlank { "Socio sin nombre" }, style = MaterialTheme.typography.titleSmall)
                Text(
                    "${users.typeId.ifBlank { "Socio" }} · ${users.classes.ifBlank { "0" }} clases",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            val statusColor = if (users.pay) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
            val statusBackground = if (users.pay) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer
            Surface(shape = CircleShape, color = statusBackground) {
                Text(
                    if (users.pay) "Al día" else "Pendiente",
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor
                )
            }
        }
    }
}
