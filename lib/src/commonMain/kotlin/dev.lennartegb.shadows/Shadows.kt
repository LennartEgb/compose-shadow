package dev.lennartegb.shadows

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
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
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified

// NOTE: Default color is used from default settings of drop shadow in Figma
private val defaultColor: Color get() = Color.Black.copy(alpha = .25f)

public fun Modifier.boxShadow(
    blurRadius: Dp,
    color: Color = defaultColor,
    spreadRadius: Dp = 0.dp,
    offset: DpOffset = DpOffset.Zero,
    shape: Shape = RectangleShape,
    clip: Boolean = true,
    inset: Boolean = false,
): Modifier {
    require(blurRadius.isSpecified) { "blurRadius must be specified." }
    require(blurRadius.value >= 0f) { "blurRadius can't be negative." }
    require(color.isSpecified) { "color must be specified." }
    require(spreadRadius.isSpecified) { "spreadRadius must be specified." }
    require(offset.isSpecified) { "offset must be specified." }

    val shadowModifier = drawWithCache {
        onDrawWithContent {
            if (inset) drawContent()
            drawIntoCanvas { canvas ->
                val spreadSize = spreadRadius.toPx()
                    .times(other = 2)
                    .let { if (inset) -it else it }
                    .let(::Size)

                canvas.withSave {
                    if (inset) canvas.inset(outline = shape.createOutline(size), color = color)

                    canvas.translate((offset - spreadRadius).toOffset())

                    val shadowOutline = shape.createOutline(size = size + spreadSize)
                    canvas.drawShadow(
                        outline = shadowOutline,
                        blurRadius = blurRadius,
                        color = color
                    )
                }
            }
            if (!inset) drawContent()
        }
    }
    return if (clip) shadowModifier.clip(shape) else shadowModifier
}

context(Density)
private fun Canvas.drawShadow(outline: Outline, blurRadius: Dp, color: Color) {
    drawOutline(outline = outline, paint = createBlurPaint(blurRadius, color))
}

context(CacheDrawScope)
private fun Shape.createOutline(size: Size): Outline {
    val density = Density(density, fontScale)
    return createOutline(size = size, layoutDirection = layoutDirection, density = density)
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

private operator fun Size.plus(size: Size): Size =
    Size(this.width + size.width, this.height + size.height)

private fun Size(all: Float): Size = Size(width = all, height = all)

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

context(Density)
private fun DpOffset.toOffset(): Offset = Offset(x.toPx(), y.toPx())

private fun Canvas.translate(offset: Offset) {
    translate(dx = offset.x, dy = offset.y)
}

private operator fun DpOffset.minus(value: Dp): DpOffset {
    return DpOffset(x = x - value, y = y - value)
}