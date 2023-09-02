package com.iideprived.jetpackcomposecomponents.ui.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    isSelected: Boolean = false,
    text: String,
    icon: ImageVector,
    selectedBackgroundColor: Color = MaterialTheme.colors.secondary,
    idleBackgroundColor: Color = Color.Transparent,
    shape: Shape = MaterialTheme.shapes.small,
    contentSelectedColor: Color = MaterialTheme.colors.onSecondary,
    contentIdleColor: Color = MaterialTheme.colors.onPrimary,
    onClick: (isSelected: Boolean) -> Unit = { },
    enabled: Boolean = true,
    iconSize: Dp = 32.dp,
    iconPadding: PaddingValues = PaddingValues(8.dp),
    textPadding: PaddingValues = PaddingValues(end = 8.dp
    )
){
    Row(
        modifier
            .padding(iconPadding)
            .background(
                animateColorAsState(if (isSelected) selectedBackgroundColor else idleBackgroundColor).value,
                shape
            )
            .clip(shape)
            .animateContentSize(if (isSelected) tween(easing = LinearEasing) else tween(delayMillis = 100))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(bounded = false),
                enabled = enabled,
            ) { onClick(!isSelected) },
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .padding(iconPadding),
            imageVector = icon,
            contentDescription = text,
            tint = animateColorAsState(if (isSelected) contentSelectedColor else contentIdleColor).value
        )
        AnimatedVisibility(visible = isSelected) {
            Text(
                modifier = Modifier.padding(textPadding),
                text = text,
                style = MaterialTheme.typography.button,
                color = contentSelectedColor,
            )
        }
    }
}