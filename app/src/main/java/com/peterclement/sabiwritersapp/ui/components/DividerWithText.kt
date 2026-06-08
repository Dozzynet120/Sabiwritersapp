package com.peterclement.sabiwritersapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DividerWithText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        Text("  $text  ", color = Color.Gray)
        Divider(modifier = Modifier.weight(1f))
    }
}
