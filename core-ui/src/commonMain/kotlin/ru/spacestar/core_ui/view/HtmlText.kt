package ru.spacestar.core_ui.view

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.spacestar.core.utils.AnnotatedStringHelper
import ru.spacestar.core.utils.HtmlHelper
import ru.spacestar.core_ui.theme.AppTheme

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    html: String,
) {
    val uriHandler = LocalUriHandler.current
    val htmlHelper = HtmlHelper(linkColor = MaterialTheme.colorScheme.primary)
    val annotatedString = htmlHelper.fromHtml(html)
    ClickableText(
        modifier = modifier,
        text = annotatedString,
    ) {
        annotatedString.getStringAnnotations(AnnotatedStringHelper.URL_TAG, it, it)
            .firstOrNull()?.item?.let { uri ->
                uriHandler.openUri(uri)
            }
    }
}

@Preview
@Composable
fun HtmlTextPreview() {
    AppTheme {
        HtmlText(html = "Hello <a href=\"http://google.com\">from</a> " +
                "my <a href=\"https://vk.com\">brother</a> Bob")
    }
}