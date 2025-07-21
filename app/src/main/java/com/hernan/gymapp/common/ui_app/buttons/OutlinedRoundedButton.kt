package com.hernan.gymapp.common.ui_app.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.common.ui.theme.disableGrey
import com.hernan.gymapp.common.ui.theme.orange_primary

@Composable
fun OutlinedRoundedButton(
    enabled:Boolean ,
    onClick: () -> Unit,
    text: String) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 55.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = Color.White,
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = MaterialTheme.colorScheme.background
        ),
        border = if (enabled) BorderStroke(1.dp, orange_primary) else BorderStroke(1.dp, disableGrey)
    ) {
        Text(text = text)
    }
}