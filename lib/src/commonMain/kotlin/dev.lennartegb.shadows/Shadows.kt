package dev.lennartegb.shadows

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified

public fun Modifier.boxShadow(
    blurRadius: Dp,
    color: Color = Color.Black.copy(alpha = .2F),
    spreadRadius: Dp = 0.dp,
    offset: DpOffset = DpOffset.Zero,
    shape: Shape = RectangleShape,
    clip: Boolean = true,
    inset: Boolean = false,
): Modifier {
    require(color.isSpecified) { "color must be specified." }
    require(blurRadius.isSpecified) { "blurRadius must be specified." }
    require(spreadRadius.isSpecified) { "spreadRadius must be specified." }
    require(blurRadius.value >= 0f) { "blurRadius can't be negative." }
    require(offset.isSpecified) { "offset must be specified." }

    val shadowModifier = drawWithCache {
        onDrawWithContent {
            if (inset) drawContent()
            drawShadow(
                blurRadius = blurRadius,
                spreadRadius = spreadRadius,
                inset = inset,
                shape = shape,
                color = color,
                offset = offset,
            )
            if (!inset) drawContent()
        }
    }
    return if (clip) shadowModifier.clip(shape) else shadowModifier
}

private fun ContentDrawScope.drawShadow(
    blurRadius: Dp,
    spreadRadius: Dp,
    inset: Boolean,
    shape: Shape,
    color: Color,
    offset: DpOffset,
) {
    drawIntoCanvas { canvas ->
        val spreadRadiusPx = spreadRadius.toPx().let { if (inset) -it else it }
        val density = Density(density = density, fontScale = fontScale)
        val shadowSize = size.plus(width = spreadRadiusPx * 2, height = spreadRadiusPx * 2)

        canvas.withSave {
            if (inset) canvas.inset(
                outline = shape.createOutline(size, layoutDirection, density),
                color = color
            )
            canvas.translate(
                dx = offset.x.toPx() - spreadRadiusPx,
                dy = offset.y.toPx() - spreadRadiusPx
            )
            val outline = shape.createOutline(shadowSize, layoutDirection, density)
            canvas.drawOutline(outline = outline, paint = createBlurPaint(blurRadius, color))
        }
    }
}

private fun Canvas.inset(outline: Outline, color: Color) {
    clip(outline)

    val colorMatrix = ColorMatrix().apply {
        setScale(alphaScale = -1f)
        shiftAlpha(255f * color.alpha)
    }
    val colorFilter = ColorFilter.colorMatrix(colorMatrix)
    val filterPaint = Paint().apply { this.colorFilter = colorFilter }

    saveLayer(outline.bounds, filterPaint)
}

private fun Size.plus(width: Float, height: Float): Size =
    Size(this.width + width, this.height + height)

private fun ColorMatrix.shiftAlpha(value: Float): Unit = set(row = 3, column = 4, v = value)

private fun ColorMatrix.setScale(
    redScale: Float = get(row = 0, column = 0),
    greenScale: Float = get(row = 1, column = 1),
    blueScale: Float = get(row = 2, column = 2),
    alphaScale: Float = get(row = 3, column = 3),
): Unit = setToScale(redScale, greenScale, blueScale, alphaScale)

private fun Canvas.clip(outline: Outline) {
    when (outline) {
        is Outline.Generic -> clipPath(path = outline.path)
        is Outline.Rectangle -> clipRect(rect = outline.rect)
        is Outline.Rounded -> clipPath(path = Path().apply { addRoundRect(outline.roundRect) })
    }
}