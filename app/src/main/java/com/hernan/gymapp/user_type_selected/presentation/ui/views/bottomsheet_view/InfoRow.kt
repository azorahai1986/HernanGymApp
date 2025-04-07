package com.hernan.gymapp.user_type_selected.presentation.ui.views.bottomsheet_view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage

/**
 * Elementos dentro de la lista. Se muestran dentro de cada caja segun su categoria
 * **/
@Composable
fun InfoRow(text: String, image: Int) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        GlideImage(
            imageModel = { image },
            modifier = Modifier
                .size(36.dp)
                .clip(AbsoluteRoundedCornerShape(16.dp))
                .padding(start = 2.dp, bottom = 2.dp)
                .align(alignment = Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(12.dp))


        Text(
            text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .padding(start = 2.dp, bottom = 2.dp)
                .align(Alignment.CenterVertically)
        )
    }
}