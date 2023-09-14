package com.iideprived.jetpackcomposecomponents.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iideprived.jetpackcomposecomponents.data.Message
import com.iideprived.jetpackcomposecomponents.data.Message.Companion.MESSAGE_GROUP_BUFFER_MILLIS
import com.iideprived.jetpackcomposecomponents.data.Message.Companion.MESSAGE_TIMESTAMP_BUFFER_MILLIS
import com.iideprived.jetpackcomposecomponents.utils.time.compareAndFormatTimestamp

@Composable
fun MessageLayout(
    userID: String,
    loadedMessages: List<Message>,
) {

    var clickedMessage: Message? by remember { mutableStateOf(null)}
    val state = rememberLazyListState()
    LazyColumn(
        state = state
    ) {
        itemsIndexed(loadedMessages) {index, msg ->
            msg.attach(loadedMessages.getOrNull(index + 1))
            msg.Message(userID, loadedMessages.getOrNull(index - 1), clickedMessage == msg) { clickedMessage = if (msg == clickedMessage) null else msg }
        }
    }

    LaunchedEffect(loadedMessages){
        state.animateScrollToItem(loadedMessages.lastIndex)
    }
}

@Composable
fun Message.Message(
    userID: String,
    previous: Message?,
    isClicked: Boolean,
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
        Modifier.fillMaxWidth().animateContentSize(),
        horizontalAlignment = if(isMyMessage) Alignment.End else Alignment.Start){
        AnimatedVisibility(showTimestamp){
            Text(
                text = timestamp.uppercase(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .alpha(0.5f),
                color = MaterialTheme.colors.onBackground,
                fontSize = 14.sp,
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
                            bottom = if (isLastOfGroup()) 8.dp else 1.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .background(
                            color = if (isMyMessage) MaterialTheme.colors.primary else Color(
                                80,
                                80,
                                80
                            ),
                            shape = when (placement) {
                                Message.Placement.Only -> RoundedCornerShape(50)
                                Message.Placement.First -> RoundedCornerShape(50, 50, 25, 25)
                                Message.Placement.Middle -> RoundedCornerShape(25)
                                Message.Placement.Last -> RoundedCornerShape(25, 25, 50, 50)
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
                    color = Color.White
                )
            }

            is Message.Link -> {}
            is Message.Media -> {}
            is Message.System -> {}
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
                Message.Text("2", "😂", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS * 3 - 1),
                Message.Text("2", "Yeah, you good with the suspense...", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS - 2),
                Message.Text("2", "You always doing that", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 - MESSAGE_GROUP_BUFFER_MILLIS - 1),
                Message.Text("2", "... \uD83E\uDEF5😠ur done", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS * 49 ),
                Message.Text("1", "😂 Bro chill it really wasn't even that deep", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS - 1),
                Message.Text("1", "It was cool bra", currentTime - MESSAGE_TIMESTAMP_BUFFER_MILLIS),
                Message.Text("2", "I saw all the snaps atp", currentTime),
            )
        }

        MessageLayout("1", messages)
    }
}