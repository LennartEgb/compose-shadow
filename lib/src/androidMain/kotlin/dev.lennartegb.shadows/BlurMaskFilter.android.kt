package dev.lennartegb.shadows

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

internal actual fun createBlurPaint(
    density: Density,
    blurRadius: Dp,
    color: Color,
): Paint {
    return Paint().asFrameworkPaint().apply {
        if (blurRadius.value > 0f) {
            val style = Blur.NORMAL
            val radiusPx = with(density) { blurRadius.toPx() }
            this.maskFilter = BlurMaskFilter(radiusPx, style)
        }
        this.color = color.toArgb()
    }.asComposePaint()
}
