package dev.lennartegb.shadows

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter

internal actual fun Density.createBlurPaint(
    blurRadius: Dp,
    color: Color,
    blurMode: BlurMode,
): Paint = Paint().asFrameworkPaint().apply {
    if (blurRadius.value > 0f) {
        val mode = when (blurMode) {
            BlurMode.NORMAL -> FilterBlurMode.NORMAL
            BlurMode.SOLID -> FilterBlurMode.SOLID
            BlurMode.OUTER -> FilterBlurMode.OUTER
            BlurMode.INNER -> FilterBlurMode.INNER
        }
        this.maskFilter = MaskFilter.makeBlur(mode = mode, sigma = blurRadius.toPx() / 2)
    }
    this.color = color.toArgb()
}.asComposePaint()