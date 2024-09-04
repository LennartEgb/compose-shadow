package dev.lennartegb.shadows

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

internal expect fun Density.createBlurPaint(
    blurRadius: Dp,
    color: Color,
): Paint
