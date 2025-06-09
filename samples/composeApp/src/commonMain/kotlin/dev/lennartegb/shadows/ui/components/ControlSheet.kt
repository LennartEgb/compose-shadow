package dev.lennartegb.shadows.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lennartegb.shadows.AppState
import dev.lennartegb.shadows.rememberAppState
import org.jetbrains.compose.ui.tooling.preview.Preview

private val shapes: List<Shape> = listOf(RectangleShape, CircleShape, RoundedCornerShape(8.dp))

@Composable
internal fun ControlsSheet(
    state: AppState,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = spacedBy(4.dp),
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    onBoxColorChange: () -> Unit,
    onShadowColorChange: () -> Unit,
    maxBoxSize: Dp = 200.dp,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        state = listState,
        contentPadding = contentPadding,
    ) {
        title(text = "Box")
        with(state.boxValues) {
            item {
                DualDpSliderSection(
                    title = "Size",
                    firstTitle = "Width",
                    firstDp = size.width,
                    onFirstDpChange = state::boxWidth,
                    secondTitle = "Height",
                    secondDp = size.height,
                    onSecondDpChange = state::boxHeight,
                    min = 10.dp,
                    max = maxBoxSize,
                )
            }
            item {
                ShapeSelectorSection(
                    "Shape",
                    selected = shape,
                    shapes = shapes,
                    onShapeSelect = state::boxShape,
                )
            }

            colorPicker(color, onBoxColorChange)
            item {
                Slider(
                    value = color.alpha,
                    onValueChange = state::boxAlpha,
                )
            }
        }

        divider()

        title(text = "Shadow")
        with(state.shadowValues) {
            dpSliderSection(
                "Blur Radius",
                dp = blurRadius,
                onDpChange = state::blur,
                max = maxBoxSize,
                min = 0.dp,
            )
            dpSliderSection(
                "Spread Radius",
                dp = spreadRadius,
                onDpChange = state::spread,
                max = maxBoxSize,
                min = 0.dp,
            )

            colorPicker(color, onShadowColorChange)
            item {
                Slider(
                    value = color.alpha,
                    onValueChange = state::shadowAlpha,
                )
            }

            item {
                val maxOffset = 50.dp
                DualDpSliderSection(
                    title = "Offset",
                    firstTitle = "X",
                    firstDp = offset.x,
                    onFirstDpChange = state::offsetX,
                    secondTitle = "Y",
                    secondDp = offset.y,
                    onSecondDpChange = state::offsetY,
                    min = -maxOffset,
                    max = maxOffset,
                )
            }

            item {
                ShapeSelectorSection(
                    title = "Shape",
                    selected = shape,
                    shapes = shapes,
                    onShapeSelect = state::shadowShape,
                )
            }
            item {
                SwitchSection(
                    "Clip",
                    checked = clip,
                    onCheckedChange = state::switchClip,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                SwitchSection(
                    "Inset",
                    checked = inset,
                    onCheckedChange = state::switchInset,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private fun LazyListScope.title(text: String) = item { Title(text) }
private fun LazyListScope.divider() = item { HorizontalDivider() }
private fun LazyListScope.colorPicker(color: Color, onColorChange: () -> Unit) = item {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onColorChange),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween,
    ) {
        Text("Color")
        Box(Modifier.size(24.dp).background(color))
    }
}

private fun LazyListScope.dpSliderSection(
    title: String,
    dp: Dp,
    onDpChange: (Dp) -> Unit,
    min: Dp,
    max: Dp,
) = item {
    DpSliderSection(title = title, dp = dp, onDpChange = onDpChange, min = min, max = max)
}

@Preview
@Composable
private fun ControlSheetPreview() {
    ControlsSheet(state = rememberAppState(), onBoxColorChange = {}, onShadowColorChange = {})
}
