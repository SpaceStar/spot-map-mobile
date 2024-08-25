package ru.spacestar.core.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration

class AnnotatedStringHelper(
    string: AnnotatedString? = null
) {
    private var builder = string?.let { AnnotatedString.Builder(it) } ?: AnnotatedString.Builder()

    constructor(string: String) : this(AnnotatedString(string))

    fun appendString(string: String) = apply {
        builder.append(string)
    }

    fun setClickableAnnotation(
        startIndex: Int,
        count: Int,
        url: String,
        color: Color,
        underlined: Boolean = false
    ) = apply {
        val endIndex = startIndex + count
        builder.addStyle(
            style = SpanStyle(
                color = color,
                textDecoration = if (underlined) TextDecoration.Underline else null
            ),
            start = startIndex,
            end = endIndex
        )

        builder.addStringAnnotation(
            tag = URL_TAG,
            annotation = url,
            start = startIndex,
            end = endIndex
        )
    }

    fun annotatedString() = builder.toAnnotatedString()

    fun clear() {
        builder = AnnotatedString.Builder()
    }

    companion object {
        const val URL_TAG = "URL"
    }
}