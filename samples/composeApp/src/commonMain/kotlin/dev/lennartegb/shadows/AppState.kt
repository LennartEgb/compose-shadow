package dev.lennartegb.shadows

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
internal fun rememberAppState(): AppState {
    return remember { AppState() }
}

@Stable
internal class AppState(shadowValues: ShadowValues = DefaultShadowValues) {

    private companion object {
        val DefaultShadowValues = ShadowValues(
            blurRadius = 0.dp,
            color = Color.Black,
            spreadRadius = 0.dp,
            offset = androidx.compose.ui.unit.DpOffset.Zero,
            shape = RectangleShape,
            clip = false,
            inset = false,
        )

        val DefaultBoxValues = BoxValues(
            size = DpSize(80.dp, 80.dp),
            shape = RectangleShape,
            color = Color.LightGray,
        )

    }

    var boxValues by mutableStateOf(DefaultBoxValues)
        private set
    var shadowValues by mutableStateOf(shadowValues)
        private set

    fun blur(dp: Dp) {
        shadowValues = shadowValues.copy(blurRadius = dp)
    }

    fun spread(dp: Dp) {
        shadowValues = shadowValues.copy(spreadRadius = dp)
    }

    fun shadowShape(shape: Shape) {
        shadowValues = shadowValues.copy(shape = shape)
    }

    fun switchClip(checked: Boolean) {
        shadowValues = shadowValues.copy(clip = checked)
    }

    fun switchInset(checked: Boolean) {
        shadowValues = shadowValues.copy(inset = checked)
    }

    fun offsetX(offset: Dp) {
        shadowValues = shadowValues.copy(offset = shadowValues.offset.copy(x = offset))
    }

    fun offsetY(offset: Dp) {
        shadowValues = shadowValues.copy(offset = shadowValues.offset.copy(y = offset))
    }

    fun shadowColor(color: Color) {
        shadowValues = shadowValues.copy(color = color)
    }

    fun boxWidth(dp: Dp) {
        boxValues = boxValues.copy(size = boxValues.size.copy(width = dp))
    }

    fun boxHeight(dp: Dp) {
        boxValues = boxValues.copy(size = boxValues.size.copy(height = dp))
    }

    fun boxShape(shape: Shape) {
        boxValues = boxValues.copy(shape = shape)
    }

    fun boxColor(color: Color) {
        boxValues = boxValues.copy(color = color)
    }
}