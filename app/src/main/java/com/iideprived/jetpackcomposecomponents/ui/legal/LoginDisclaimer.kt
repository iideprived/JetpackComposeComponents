package com.iideprived.jetpackcomposecomponents.ui.legal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoginDisclaimer(
    onTermsClicked: () -> Unit,
    onPrivacyClicked: () -> Unit
){
    Column(Modifier.padding(bottom = 32.dp)) {
        Text(
            text = "By signing up, you're agreeing to our",
            style = MaterialTheme.typography.body2,
            color = Color.White
        )
        Row(horizontalArrangement = Arrangement.Center){
            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onTermsClicked() },
                text = "Terms & Conditions",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = " and ",
                style = MaterialTheme.typography.body2,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onPrivacyClicked() },
                text = "Privacy Policy",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary
            )
        }
    }
}