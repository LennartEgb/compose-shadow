package dev.lennartegb.shadows.ui.components

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun DpSlider(
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

@Preview
@Composable
private fun DpSliderPreview() {
    DpSlider(
        dp = 10.dp,
        onDpChange = {},
        min = 5.dp,
        max = 20.dp,
    )
}
