package dev.lennartegb.shadows

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
internal fun rememberAppState(isDarkTheme: Boolean = isSystemInDarkTheme()): AppState {
    return remember { AppState(isDarkTheme = isDarkTheme) }
}

@Stable
internal class AppState(isDarkTheme: Boolean, shadowValues: ShadowValues = DefaultShadowValues) {

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

    var isDarkTheme: Boolean by mutableStateOf(isDarkTheme)
        private set

    var boxValues by mutableStateOf(DefaultBoxValues)
        private set
    var shadowValues by mutableStateOf(shadowValues)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }

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
