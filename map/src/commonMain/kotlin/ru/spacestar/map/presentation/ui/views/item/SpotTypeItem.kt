package ru.spacestar.map.presentation.ui.views.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.spacestar.map.data.SpotType

@Composable
fun SpotTypeItem(
    modifier: Modifier = Modifier,
    spotType: SpotType,
    onClick: (Int) -> Unit = {}
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clickable { onClick(spotType.id) }
            .background(if (spotType.selected) Color.Blue else Color.Transparent),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(spotType.name)
    }
}