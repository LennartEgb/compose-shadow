package dev.lennartegb.shadows

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShapeSelector(
    selected: Shape,
    shapes: List<Shape>,
    onShapeSelect: (Shape) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    strokeWidth: Dp = 1.dp,
) {
    Row(
        modifier = modifier
            .background(color = color.copy(alpha = .1f), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = SpaceEvenly,
    ) {
        shapes.forEachIndexed { index, shape ->
            ShapeItem(
                modifier = Modifier.size(24.dp).clip(shape).clickable { onShapeSelect(shape) },
                shape = shape,
                color = if (shape == selected) MaterialTheme.colorScheme.primary else color,
                selected = shape == selected,
                strokeWidth = strokeWidth,
            )
            if (index != shapes.lastIndex) Box(
                modifier = Modifier.width(strokeWidth).height(24.dp).background(color)
            )
        }
    }
}

@Composable
private fun ShapeItem(
    shape: Shape,
    color: Color,
    strokeWidth: Dp,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val layoutDirection = LocalLayoutDirection.current
    Canvas(modifier = modifier) {
        val density = Density(density = density, fontScale = fontScale)
        val outline = shape.createOutline(size, layoutDirection = layoutDirection, density = density)
        val style: DrawStyle = if (selected) Fill else Stroke((strokeWidth * 2).toPx())
        drawOutline(outline = outline, color = color, style = style)
    }
}