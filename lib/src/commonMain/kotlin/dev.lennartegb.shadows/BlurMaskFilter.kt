package dev.lennartegb.shadows

import androidx.compose.ui.graphics.NativePaint

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

internal expect fun NativePaint.setMaskFilter(blurRadius: Float, blurMode: BlurMode)