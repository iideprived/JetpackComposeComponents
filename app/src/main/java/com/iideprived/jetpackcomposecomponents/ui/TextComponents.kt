package com.iideprived.jetpackcomposecomponents.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt


object TextStyles {
    val Header = TextStyle(
        fontSize = 24.sp,
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp
    )
    val Subheader = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.3.sp
    )
    val Content = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.2.sp
    )
    val RoundedButton = TextStyle(
        fontSize = 16.sp,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.3.sp,
    )
}

@Composable
fun String.Header(
    color: Color = Color.White,
    isSubheader: Boolean = false
) {
    Text(
        text = this,
        style = if (isSubheader) TextStyles.Subheader else TextStyles.Header,
        color = color
    )
}

@Composable
fun String.Content(
    color: Color = Color.White,
    isSubtle: Boolean = false
){
    Text(
        text = this,
        style = TextStyles.Content,
        color = color.copy(alpha = if (isSubtle) 0.5f else 1f)
    )
}

@Composable
fun String.Styled(
    modifier: Modifier = Modifier,
    with: TextStyle = LocalTextStyle.current,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
){
    Text(
        text = this,
        style = with,
        modifier = modifier,
        textDecoration = textDecoration,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = onTextLayout
    )
}

@Composable
fun String.RoundedButton(
    onClick: () -> Unit = {},
    icon: ImageVector? = null,
    iconSize: Dp = 24.dp,
    padding: PaddingValues = PaddingValues(8.dp),
    backgroundColor : Color = Color(0xFFEFEFEF),
    borderColor: Color = Color(0xFFAAAAAA),
    contentColor: Color = Color.Black,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
){
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection){
        Row (
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = CircleShape
                )
                .border(BorderStroke(1.dp, borderColor), CircleShape)
                .clip(CircleShape)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(bounded = false, color = Color.Black)
                ) { onClick() }
                .padding(padding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = this@RoundedButton,
                    tint = contentColor,
                    modifier = Modifier
                        .size(iconSize)
                )
                if (this@RoundedButton.isNotBlank()) Spacer(Modifier.width(8.dp))
            }

            if (this@RoundedButton.isNotEmpty()){
                Text(
                    modifier = Modifier
                        .padding(end = 2.dp),
                    text = this@RoundedButton,
                    style = TextStyles.RoundedButton,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun Number.Rating(
    scale: IntRange = 0 until 5,
    numBars : Int = 5,
    spaceBetweenRatings: Dp = 3.dp,
    ratingFillColor : Color = Color.Cyan,
    ratingNoFillColor: Color = Color.Transparent,
    ratingBackgroundColor: Color = Color.White,
    ratingBorderColor: Color = Color(0xFFCCCCCC),
    ratingShape : Shape = RoundedCornerShape(100),
    ratingHeight: Dp = 10.dp,
    ratingWidth: Dp = 24.dp,
    barShape: Shape = RoundedCornerShape(100),
    barColor: Color = Color.Transparent,
){
    val step = (scale.last + 1).toFloat() / numBars.toFloat()
    val fullBars = this.toFloat() / step
    Row (
        Modifier
            .background(shape = barShape, color = barColor)
            .clip(barShape)
    ) {
        for (i in 0 until numBars){
            Row(
                modifier = Modifier
                    .width(ratingWidth)
                    .height(ratingHeight)
                    .clip(ratingShape)
                    .background(color = ratingBackgroundColor, shape = ratingShape)
                    .border(0.5.dp, ratingBorderColor, shape = ratingShape)
            ) {
                val weight = when {
                    i + 1 <= fullBars.toInt() -> 100
                    i > fullBars.toInt() -> 0
                    else -> ((fullBars - fullBars.toInt()) * 100).roundToInt()
                }
                if (weight > 0){
                    Spacer(
                        Modifier
                            .fillMaxHeight()
                            .weight(weight.toFloat())
                            .background(color = ratingFillColor))
                }
                if (weight < 100){
                    Spacer(
                        Modifier
                            .fillMaxHeight()
                            .weight(100 - weight.toFloat())
                            .background(color = ratingNoFillColor))
                }
            }
            if (i < numBars - 1) Spacer(Modifier.width(spaceBetweenRatings))
        }
    }

}



@Composable
@Preview
private fun HeaderPreview(){
    MaterialTheme {
        Column() {
            "Header".Header()
            Spacer(Modifier.height(2.dp))
            "Subheader".Header(isSubheader = true)
        }
    }
}

@Composable
@Preview
private fun ContentPreview(){
    MaterialTheme {
        Column {
            "Content".Content()
            "Content Unavailable".Content(isSubtle = true)
        }
    }
}

@Composable
@Preview
private fun RoundedButtonPreview() {
    MaterialTheme {
        Column() {
            "Details".RoundedButton(icon = Icons.Rounded.ArrowDropDown)
            Spacer(Modifier.height(2.dp))
            "Location".RoundedButton()
            Spacer(Modifier.height(2.dp))
            "".RoundedButton(icon = Icons.Rounded.Place)
        }
    }
}

@Composable
@Preview
private fun RatingsPreview(){
    MaterialTheme {
        Column {
            2.5f.Rating()
            Spacer(Modifier.height(2.dp))
            2.5f.Rating(
                ratingShape = RectangleShape,
                spaceBetweenRatings = 0.dp,
                ratingBorderColor = Color.Transparent
            )
            Spacer(Modifier.height(2.dp))
            2.5f.Rating(
                spaceBetweenRatings = 2.dp,
                ratingBorderColor = Color.Black,
                ratingHeight = 10.dp,
                ratingWidth = 10.dp,
                ratingFillColor = Color(0xFFEFEFEF),
                ratingNoFillColor = Color.Transparent,
                ratingBackgroundColor = Color.Transparent
            )
        }
    }
}