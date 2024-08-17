package dev.lennartegb.shadows

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

internal enum class BlurMode {
    /** fuzzy inside and outside  */
    NORMAL,

    /** solid inside, fuzzy outside  */
    SOLID,

    /** nothing inside, fuzzy outside  */
    OUTER,

    /** fuzzy inside, nothing outside  */
    INNER;
}

internal expect fun Density.createBlurPaint(
    blurRadius: Dp,
    color: Color,
    blurMode: BlurMode = BlurMode.NORMAL,
): Paint