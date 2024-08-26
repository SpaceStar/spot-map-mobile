package ru.spacestar.core_ui.view

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    hint: String? = null,
    imageVector: ImageVector,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    if (hint != null) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = { PlainTooltip {
                Text(text = hint)
            } },
            state = rememberTooltipState()
        ) {
            Button(modifier, hint, imageVector, tint, onClick)
        }
    } else Button(modifier, hint, imageVector, tint, onClick)
}

@Composable
private fun Button(
    modifier: Modifier = Modifier,
    hint: String? = null,
    imageVector: ImageVector,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    // fixme: ripple size on remove button
    Surface(
        modifier = modifier,
        shape = CircleShape,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            tint = tint,
            contentDescription = hint
        )
    }
}