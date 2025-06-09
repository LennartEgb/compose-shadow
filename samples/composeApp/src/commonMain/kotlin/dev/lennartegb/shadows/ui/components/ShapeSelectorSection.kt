package dev.lennartegb.shadows.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ShapeSelectorSection(
    title: String,
    selected: Shape,
    shapes: List<Shape>,
    onShapeSelect: (Shape) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(title)
        ShapeSelector(
            modifier = Modifier.fillMaxWidth(),
            selected = selected,
            onShapeSelect = onShapeSelect,
            shapes = shapes,
        )
    }
}

@Preview
@Composable
private fun ShapeSelectorSectionPreview() {
    val shapes = listOf(CircleShape, RectangleShape, RoundedCornerShape(8.dp))
    ShapeSelectorSection(
        title = "Shape Selector Section",
        selected = shapes.first(),
        shapes = shapes,
        onShapeSelect = {},
    )
}
