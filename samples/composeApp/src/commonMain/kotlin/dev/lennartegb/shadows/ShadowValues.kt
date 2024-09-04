package dev.lennartegb.shadows

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

internal data class ShadowValues(
    val blurRadius: Dp,
    val color: Color,
    val spreadRadius: Dp,
    val offset: DpOffset,
    val shape: Shape,
    val clip: Boolean,
    val inset: Boolean,
)
