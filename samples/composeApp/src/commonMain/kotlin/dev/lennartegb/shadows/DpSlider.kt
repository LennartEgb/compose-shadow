package dev.lennartegb.shadows

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DpSlider(
    dp: Dp,
    onDpChange: (Dp) -> Unit,
    max: Dp,
    min: Dp,
    modifier: Modifier = Modifier,
) {
    Slider(
        modifier = modifier,
        value = dp.value,
        valueRange = min.value..max.value,
        onValueChange = { onDpChange(it.dp) },
    )
}
