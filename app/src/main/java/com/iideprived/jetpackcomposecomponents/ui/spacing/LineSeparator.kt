package com.iideprived.jetpackcomposecomponents.ui.spacing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LineSeparator(
    modifier: Modifier = Modifier,
    spaceAroundText: Boolean = false,
    lineHeight: Dp = 1.dp,
    lineColor: Color = Color.LightGray,
    lineSpacing: Dp = 8.dp,
    lineShape: Shape = RectangleShape,
    lineWidth: Dp? = null,
    text: String = "",
    textColor: Color = Color.LightGray,
    textStyle: TextStyle = MaterialTheme.typography.body1,
){
    if (spaceAroundText){
        Row(modifier,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            val lineModifier = if (lineWidth == null) Modifier.weight(1f)
                else Modifier.width(lineWidth)
            Spacer(modifier = lineModifier
                .height(lineHeight)
                .background(lineColor, lineShape))
            Text(
                modifier = Modifier.padding(horizontal = lineSpacing),
                text = text,
                color = textColor,
                style = textStyle,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = lineModifier
                .height(lineHeight)
                .background(lineColor, lineShape))
        }
        return
    }
    Box(modifier = modifier.wrapContentHeight()){
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ){
            val lineModifier = if (lineWidth == null) Modifier.weight(1f)
            else Modifier.width(lineWidth)
            Spacer(modifier = lineModifier
                .height(lineHeight)
                .background(color = lineColor, lineShape))
            Spacer(modifier = Modifier.width(lineSpacing))
            Spacer(modifier = lineModifier
                .height(lineHeight)
                .background(color = lineColor, lineShape)
                )
        }
        Box(Modifier.fillMaxWidth()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                color = textColor,
                style = textStyle,
                textAlign = TextAlign.Center

            )
        }
    }
}

@Composable
@Preview
private fun LineSpacingPreview(){
    MaterialTheme {
        Column(modifier = Modifier) {
            LineSeparator(
                text = "Or"
            )
            LineSeparator(
                spaceAroundText = true,
                text = "Or",
                lineShape = CircleShape
            )
        }
    }
}