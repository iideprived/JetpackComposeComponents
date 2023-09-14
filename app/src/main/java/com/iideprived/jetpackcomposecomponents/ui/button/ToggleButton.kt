package com.iideprived.jetpackcomposecomponents.ui.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FiberManualRecord
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.PersonAddAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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
    enabled: Boolean = true,
    iconSize: Dp = 32.dp,
    iconPadding: PaddingValues = PaddingValues(8.dp),
    textPadding: PaddingValues = PaddingValues(end = 8.dp),
    onClick: (isSelected: Boolean) -> Unit = { },
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

@Composable
@Preview
private fun ToggleButtonPreview(){
    MaterialTheme {
        var selectedItem by remember { mutableIntStateOf(0) }

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF128ADF),
                            Color(0xFF639AAF),
                        )
                    )
                )
        ) {

            Text(
                text = "Overview",
                color = Color.White,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 32.dp, top = 24.dp)
            )
            Box(
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 4.dp)
                    .height(680.dp)
                    .offset(y = -(12).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {

            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ToggleButton(
                    isSelected = selectedItem == 1,
                    text = "Friends",
                    contentSelectedColor = Color.White,
                    icon = Icons.Rounded.PersonAddAlt
                ) { selectedItem = if (selectedItem == 1) 0 else 1}

                ToggleButton(
                    isSelected = selectedItem == 2,
                    text = "Messages",
                    contentSelectedColor = Color.White,
                    icon = Icons.Rounded.Message
                ) { selectedItem = if (selectedItem == 2) 0 else 2}

                ToggleButton(
                    isSelected = selectedItem == 3,
                    text = "Explore",
                    contentSelectedColor = Color.White,
                    icon = Icons.Rounded.Search
                ) { selectedItem = if (selectedItem == 3) 0 else 3}
            }
        }
    }
}