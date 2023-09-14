package com.iideprived.jetpackcomposecomponents.data

import android.net.Uri
import com.iideprived.jetpackcomposecomponents.utils.time.millisToCalendar
import kotlin.math.abs

sealed class Message(
    val sender: String? = null,
    val timestampMillis: Long
)
{
    companion object {
        const val MESSAGE_GROUP_BUFFER_MILLIS = 10000L
        const val MESSAGE_TIMESTAMP_BUFFER_MILLIS = 3600000L
    }

    var cal = millisToCalendar(timestampMillis)

    var next: Message? = null
        private set(other){
            field = other
            placement = if (prev == null) Placement.First else Placement.Middle
        }
    var prev: Message? = null
        private set(other){
            field = other
            placement = if (next == null) Placement.Last else Placement.Middle
        }

    var placement: Placement = Placement.Only
        private set

    data class Text(
        val from: String,
        val body: String,
        val timestamp: Long
    ) : Message(from, timestamp)

    data class System(
        val message: String,
        val timestamp: Long
    ) : Message(null, timestamp)

    data class Media(
        val from: String,
        val mediaItems: List<Uri>,
        val timestamp: Long
    ) : Message(from, timestamp)

    data class Link(
        val from: String,
        val link: Uri,
        val message: String,
        val timestamp: Long
    ) : Message(from, timestamp)

    fun isFirstOfGroup() : Boolean = when (placement){
        Placement.Only, Placement.First -> true
        else -> false
    }

    fun isLastOfGroup() : Boolean = when (placement){
        Placement.Only, Placement.Last -> true
        else -> false
    }

    fun isMuchAfter(other: Message?) : Boolean = other == null || abs(this.timestampMillis - other.timestampMillis) >= MESSAGE_TIMESTAMP_BUFFER_MILLIS

    private fun attachToPrev(other: Message) { prev = other; other.next = this }
    private fun attachToNext(other: Message) { next = other; other.prev = this }

    fun attach(other: Message?){
        if (other == null) return
        if (sender != other.sender) return
        if (this is System != other is System) return

        val timeDifference = this.timestampMillis - other.timestampMillis
        if (abs(timeDifference) > MESSAGE_GROUP_BUFFER_MILLIS) return

        when {
            timeDifference < 0 -> attachToNext(other)
            else -> attachToPrev(other)
        }
    }

    enum class Placement { Only, First, Middle, Last, }
}
