package com.hernan.gymapp.user_type.presentation.ui.toolbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import com.hernan.gymapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarUsersType(text: String?) {

    TopAppBar(
        title = {
            text?.let {
                Text(text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(id = R.color.white)
                )
            }

        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.scrim)

    )

}