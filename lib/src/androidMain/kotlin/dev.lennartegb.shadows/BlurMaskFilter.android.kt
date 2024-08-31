package dev.lennartegb.shadows

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

internal actual fun Density.createBlurPaint(
    blurRadius: Dp,
    color: Color,
): Paint = Paint().asFrameworkPaint().apply {
    if (blurRadius.value > 0f) {
        val style = Blur.NORMAL
        this.maskFilter = BlurMaskFilter(blurRadius.toPx(), style)
    }
    this.color = color.toArgb()
}.asComposePaint()