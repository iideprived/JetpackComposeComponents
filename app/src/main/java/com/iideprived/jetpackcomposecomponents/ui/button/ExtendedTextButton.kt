package com.iideprived.jetpackcomposecomponents.ui.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ExtendedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
){
    val color by animateColorAsState(if (enabled) MaterialTheme.colors.primary else Color.Gray)
    Text(
        modifier = modifier
            .padding(vertical = 24.dp)
            .fillMaxWidth()
            .background(
                color = color,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(
                enabled = enabled
            ) { if (enabled) onClick() }
            .padding(vertical = 16.dp)
        ,
        text = text,
        style = MaterialTheme.typography.button,
        color = MaterialTheme.colors.onPrimary,
        textAlign = TextAlign.Center
    )
}