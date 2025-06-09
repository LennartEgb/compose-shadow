package dev.lennartegb.shadows.ui.components

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun DualDpSliderSection(
    title: String,
    firstTitle: String,
    firstDp: Dp,
    onFirstDpChange: (Dp) -> Unit,
    secondTitle: String,
    secondDp: Dp,
    onSecondDpChange: (Dp) -> Unit,
    min: Dp,
    max: Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        val (locked, setLocked) = remember { mutableStateOf(true) }

        val lockedChange: (Dp) -> Unit = { dp ->
            onFirstDpChange(dp)
            onSecondDpChange(dp)
        }
        val optionalLockedChange = lockedChange.takeIf { locked }

        Row(verticalAlignment = CenterVertically, horizontalArrangement = spacedBy(8.dp)) {
            Text(text = title)
            Spacer(Modifier.weight(1f))
            Text("Locked", style = MaterialTheme.typography.labelSmall)
            Switch(checked = locked, onCheckedChange = setLocked)
        }
        val titleModifier = Modifier.weight(1f)
        val sliderModifier = Modifier.weight(3f)
        Row(verticalAlignment = CenterVertically) {
            Text(modifier = titleModifier, text = firstTitle)
            DpSlider(
                modifier = sliderModifier,
                dp = firstDp,
                onDpChange = optionalLockedChange ?: onFirstDpChange,
                max = max,
                min = min,
            )
        }
        Row(verticalAlignment = CenterVertically) {
            Text(modifier = titleModifier, text = secondTitle)
            DpSlider(
                modifier = sliderModifier,
                dp = secondDp,
                onDpChange = optionalLockedChange ?: onSecondDpChange,
                max = max,
                min = min,
            )
        }
    }
}
