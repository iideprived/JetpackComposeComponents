package com.iideprived.jetpackcomposecomponents.ui.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExtendedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
){
    val color by animateColorAsState(if (enabled) MaterialTheme.colors.primary else Color.Gray,
        label = "Extended Text Button Enabled"
    )
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

@Composable
@Preview
private fun Preview(){
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Example",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Log In",
                style = MaterialTheme.typography.h5,
            )

            OutlinedTextField(
                value = "",
                label = { Text("Email") },
                onValueChange = {},
            )

            OutlinedTextField(
                value = "",
                label = { Text("Password") },
                onValueChange = {},
            )

            Button(
                onClick = {},
                Modifier.align(Alignment.End)
            ) {
                Text("Sign In")
            }

            Spacer(Modifier.height(120.dp))
            ExtendedTextButton(text = "I Don't Have An Account") { }
        }
    }
}
