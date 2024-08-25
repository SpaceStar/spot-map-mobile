package ru.spacestar.core.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

class HtmlHelper(
    private val linkColor: Color = Color.Blue,
    private val underlinedLinks: Boolean = false
) {
    private val linkRegex by lazy { Regex("""<a href=["'](.*?)["']>(.*?)</a>""") }

    fun fromHtml(html: String): AnnotatedString {
        return parseLinks(html)
    }

    private fun parseLinks(html: String): AnnotatedString {
        val result = linkRegex.findAll(html)
        val helper = AnnotatedStringHelper(linkRegex.replace(html) { it.groupValues[2] })
        var offset = 0
        result.forEach {
            val startIndex = it.range.first
            val count = it.groupValues[2].length
            helper.setClickableAnnotation(
                startIndex = startIndex - offset,
                count = count,
                url = it.groupValues[1],
                color = linkColor,
                underlined = underlinedLinks
            )
            offset += it.groupValues[0].length - it.groupValues[2].length
        }
        return helper.annotatedString()
    }
}