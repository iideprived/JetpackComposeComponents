package com.iideprived.jetpackcomposecomponents.utils.string

import android.util.Patterns
import androidx.compose.ui.text.TextRange
import kotlin.math.min

fun String.toTitleCase(
    nonCapitalizedWords: List<String>? = listOf("as", "at", "but", "by", "for", "and", "in"),
) : String {
    val components = split("\\W+".toRegex())
    return buildString {
        components.forEachIndexed { index, word ->
            append(when (index) {
                in 1..components.size - 2 -> word.capitalizeMiddleWord(nonCapitalizedWords)
                else -> word.replaceFirstChar(Char::uppercaseChar)
            })
            if (index < components.size - 1) append(" ")
        }
    }
}

fun String.isPhoneNumber(): Boolean {
    val phonePattern = "^[+]?\\d{1,3}?[-\\s.]?[(]?\\d{1,4}[)]?[-\\s.]?\\d{1,4}[-\\s.]?\\d{1,4}[-\\s.]?\\d{1,4}$"
    return this.matches(Regex(phonePattern))
}

private fun String.capitalizeMiddleWord(
    nonCapitalizedWords: List<String>?
): String =
    if (length > 3 || this !in (nonCapitalizedWords ?: emptyList())) replaceFirstChar(Char::uppercaseChar) else this

fun String.isEmail() : Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isUsername(): Boolean = matches("^(?=.{4,20}\$)(?![_.])(?!.*[_.]{2})[a-z\\d._]+(?<![_.])\$".toRegex())

fun String.getWordAt(index: Int): Pair<String, TextRange>{
    buildString {
        if (this@getWordAt[index].isWhitespace()) return "" to TextRange(index)
        append(this@getWordAt[index])

        var left = index
        while (left-- > 0 && !this@getWordAt[left].isWhitespace()){
            append(0, this@getWordAt[left])
        }
        var right = index
        while (++right < this@getWordAt.length && !this@getWordAt[right].isWhitespace()){
            append(this@getWordAt[right])
        }

        return this.toString() to TextRange(left + 1, right)
    }
}