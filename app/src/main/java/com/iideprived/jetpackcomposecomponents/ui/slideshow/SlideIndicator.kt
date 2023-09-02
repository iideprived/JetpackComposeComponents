package com.iideprived.jetpackcomposecomponents.ui.slideshow

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> SlideIndicator(
    modifier : Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = Color.Black,
    unselectedAlpha: Float = 0.3f,
    selectedAlpha: Float = 0.8f,
    unselectedSize: Dp = 12.dp,
    selectedSize: Dp = 16.dp,
    shape: Shape = CircleShape,
    animationDurationMillis: Int = 500,
    items: List<T>,
    spacing: Dp = 8.dp,
    selectedItem: T
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEach {
            val size by animateDpAsState(
                if (selectedItem == it) selectedSize else unselectedSize,
                tween(animationDurationMillis)
            )

            val alpha = animateFloatAsState(
                if (selectedItem == it) selectedAlpha else unselectedAlpha,
                tween(animationDurationMillis)
            ).value

            val color = animateColorAsState(
                if (selectedItem == it) selectedColor else unselectedColor,
                tween(animationDurationMillis)
            ).value

            Spacer( Modifier
                .padding(horizontal = spacing)
                .size(size)
                .alpha(alpha)
                .background(
                    color = color,
                    shape = shape)
            )
        }
    }
}