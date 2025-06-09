package dev.lennartegb.shadows.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun DpSliderSection(
    title: String,
    dp: Dp,
    onDpChange: (Dp) -> Unit,
    min: Dp,
    max: Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(title)
        DpSlider(dp, onDpChange, min = min, max = max)
    }
}

@Preview
@Composable
private fun DpSliderSectionPreview() {
    DpSliderSection(
        title = "Title",
        dp = 7.dp,
        onDpChange = {},
        min = 5.dp,
        max = 10.dp,
    )
}
