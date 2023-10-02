package com.iideprived.jetpackcomposecomponents.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.data.Message
import com.iideprived.jetpackcomposecomponents.data.Message.Companion.MESSAGE_GROUP_BUFFER_MILLIS
import com.iideprived.jetpackcomposecomponents.data.Message.Companion.MESSAGE_TIMESTAMP_BUFFER_MILLIS
import com.iideprived.jetpackcomposecomponents.utils.time.compareAndFormatTimestamp
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

@Composable
fun MessageLayout(
    userID: String,
    loadedMessages: List<Message>,
    modifier: Modifier = Modifier,
    scrollAnimationDuration: Int = 1000,
    systemMessageColor: Color = MaterialTheme.colors.onBackground,
    systemMessageFontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    spaceBetweenMessageGroups: Dp = 16.dp,
    ownedMessageBackgroundColor: Color = MaterialTheme.colors.primary,
    ownedMessageTextColor: Color = MaterialTheme.colors.onPrimary,
    otherMessageBackgroundColor: Color = Color(80, 80, 80),
    otherMessageTextColor: Color = Color.White,
    messageRoundnessPercentage: Int = 50,

) {
    val state = rememberLazyListState()
    var clickedMessage: Message? by remember { mutableStateOf(null)}
    var scrollPosition by remember { mutableFloatStateOf(0f) }
    LazyColumn(
        modifier,
        state = state
    ) {
        itemsIndexed(loadedMessages) {index, msg ->
            msg.attach(loadedMessages.getOrNull(index + 1))
            msg.Message(
                userID,
                loadedMessages.getOrNull(index - 1),
                clickedMessage == msg,
                systemMessageColor,
                systemMessageFontSize,
                spaceBetweenMessageGroups,
                ownedMessageBackgroundColor,
                ownedMessageTextColor,
                otherMessageBackgroundColor,
                otherMessageTextColor,
                messageRoundnessPercentage,
            ) { clickedMessage = if (msg == clickedMessage) null else msg }
        }

        item {
            Spacer(
                Modifier
                    .height(32.dp)
                    .onGloballyPositioned { scrollPosition = it.positionInParent().y }
            )
        }
    }



    LaunchedEffect(loadedMessages.lastOrNull()){
        if (scrollPosition == 0f){
            state.animateScrollToItem(loadedMessages.lastIndex)
        } else {
            coroutineScope {
                state.animateScrollBy(scrollPosition, tween(scrollAnimationDuration))
            }
        }

    }
}

@Composable
private fun Message.Message(
    userID: String,
    previous: Message?,
    isClicked: Boolean,
    systemMessageColor: Color = MaterialTheme.colors.onBackground,
    systemMessageFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    spaceBetweenMessageGroups: Dp = 16.dp,
    ownedMessageBackgroundColor: Color = MaterialTheme.colors.primary,
    ownedMessageTextColor: Color = MaterialTheme.colors.onPrimary,
    otherMessageBackgroundColor: Color = Color(80, 80, 80),
    otherMessageTextColor: Color = Color.White,
    messageRoundnessPercentage: Int = 50,
    onClick: () -> Unit,
) {
    val timestamp = remember(previous) {
        when(previous){
            null -> this.cal.compareAndFormatTimestamp()
            else -> this.cal.compareAndFormatTimestamp(previous.cal)
        }
    }
    val showTimestamp = isFirstOfGroup() && isMuchAfter(previous) || isClicked

    val isMyMessage = this.sender == userID
    Column(
        Modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalAlignment = if(isMyMessage) Alignment.End else Alignment.Start){
        AnimatedVisibility(showTimestamp){
            Text(
                text = timestamp.uppercase(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .alpha(0.5f),
                color = systemMessageColor,
                fontSize = systemMessageFontSize,
                textAlign = TextAlign.Center
            )
        }
        when (this@Message) {
            is Message.Text -> {
                Text(
                    text = this@Message.body,
                    modifier = Modifier
                        .padding(
                            top = 1.dp,
                            bottom = if (isLastOfGroup()) spaceBetweenMessageGroups else 1.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .background(
                            color = if (isMyMessage) ownedMessageBackgroundColor else
                                otherMessageBackgroundColor,
                            shape = when (placement) {
                                Message.Placement.Only -> RoundedCornerShape(messageRoundnessPercentage)
                                Message.Placement.First -> RoundedCornerShape(messageRoundnessPercentage,  messageRoundnessPercentage, messageRoundnessPercentage / 2, messageRoundnessPercentage / 2)
                                Message.Placement.Middle -> RoundedCornerShape(messageRoundnessPercentage / 2)
                                Message.Placement.Last -> RoundedCornerShape(messageRoundnessPercentage / 2, messageRoundnessPercentage / 2, messageRoundnessPercentage, messageRoundnessPercentage)
                            }
                        )
                        .padding(8.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            role = androidx.compose.ui.semantics.Role.Checkbox,
                            onClick = onClick
                        )
                    ,
                    color = if (isMyMessage) ownedMessageTextColor else otherMessageTextColor
                )
            }
            is Message.Link -> {}
            is Message.Media -> {}
            is Message.System -> {
                Text(
                    text = this@Message.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = if (isFirstOfGroup()) 64.dp else 4.dp,
                            bottom = if (isLastOfGroup()) 64.dp else 4.dp,
                            start = 16.dp, end = 16.dp
                        )
                        .alpha(0.8f),
                    color = systemMessageColor,
                    fontSize = systemMessageFontSize,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview
@Composable
private fun MessageLayoutPreview() {
    MaterialTheme {
        val messages = remember {
            val currentTime = System.currentTimeMillis()
            mutableStateListOf(
                Message.Text("1", "Real live tired of this class", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 351 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 5),
                Message.Text("2", "Ts definitely not cool lmfao", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 351 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 4),
                Message.Text("1", "Don't forget to email that homework in bro", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 193 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 5),
                Message.Text("1", "Bro, did you make it to the viewing last night?", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 5),
                Message.Text("1", "Ts was crazy ngl", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 4),
                Message.Text("2", "Nah", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 3),
                Message.Text("2", "What happen lmfao", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 2),
                Message.Text("2", "😂"    , currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 1),
                Message.Text("2", "Yeah, you good with the suspense...", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS - 2),
                Message.Text("2", "You always doing that", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS - 1),
                Message.Text("2", "... \uD83E\uDEF5😠ur done", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 ),
                Message.Text("1", "😂 Bro chill it really wasn't even that deep", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS - 1),
                Message.Text("1", "It was cool bra", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS),
                Message.System("Chad Blackson has left the chat", currentTime - 2),
                Message.System("You added Chad Blackson to the chat", currentTime - 1),
                Message.Text("2", "I saw all the snaps atp", currentTime),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            MessageLayout("1", messages, Modifier.weight(1f))
            Row{
                var newText by remember { mutableStateOf("") }
                TextField(value = newText, onValueChange = {newText = it})
                IconButton(
                    onClick = {
                        if (newText.isBlank()) return@IconButton
                        val msg = Message.Text(
                            "${Random(0).nextInt(2)}",
                            newText,
                            System.currentTimeMillis()
                        )
                        messages.add(msg)
                        newText = ""
                    }
                ) {
                    Icon(Icons.Rounded.Send, "Send")
                }
            }
        }
    }
}