package dev.lennartegb.shadows

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter

public actual fun createBlurPaint(
    density: Density,
    blurRadius: Dp,
    color: Color,
): Paint = Paint().asFrameworkPaint().apply {
    if (blurRadius.value > 0f) {
        val mode = FilterBlurMode.NORMAL
        val sigma = with(density) { blurRadius.toPx() / 2 }
        this.maskFilter = MaskFilter.makeBlur(mode = mode, sigma = sigma)
    }
    this.color = color.toArgb()
}.asComposePaint()
