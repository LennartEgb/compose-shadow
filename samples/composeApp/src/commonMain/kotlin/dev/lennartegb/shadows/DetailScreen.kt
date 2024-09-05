package dev.lennartegb.shadows

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun DetailScreen(modifier: Modifier = Modifier) {
    val appState = rememberAppState()
    val listState = rememberLazyListState()
    var changesBoxColor by remember { mutableStateOf(false) }
    var changesShadowColor by remember { mutableStateOf(false) }

    PaneScaffold(
        modifier = modifier,
        controls = {
            ControlsSheet(
                state = appState,
                listState = listState,
                contentPadding = PaddingValues(16.dp),
                onBoxColorChange = { changesBoxColor = true },
                onShadowColorChange = { changesShadowColor = true },
            )
        },
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Center) {
            Box(
                modifier = Modifier
                    .size(appState.boxValues.size)
                    .boxShadow(appState.shadowValues)
                    .background(appState.boxValues.color, shape = appState.boxValues.shape),
            )
        }
    }

    AnimatedVisibility(changesBoxColor) {
        ColorPickerDialog(
            color = appState.boxValues.color,
            onColorChanged = {
                appState.boxColor(it)
                changesBoxColor = false
            },
            onDismiss = { changesBoxColor = false },
        )
    }

    AnimatedVisibility(changesShadowColor) {
        ColorPickerDialog(
            color = appState.shadowValues.color,
            onColorChanged = {
                appState.shadowColor(it)
                changesShadowColor = false
            },
            onDismiss = { changesShadowColor = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(
    color: Color,
    onColorChanged: (Color) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(modifier = modifier, onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = spacedBy(16.dp),
        ) {
            val (internalColor, setColor) = remember { mutableStateOf(color) }
            val controller = rememberColorPickerController()
            HsvColorPicker(
                modifier = Modifier.size(200.dp),
                controller = controller,
                initialColor = internalColor,
                onColorChanged = { envelope ->
                    setColor(envelope.color)
                },
            )

            Button(onClick = { onColorChanged(internalColor) }) {
                Text("Submit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PaneScaffold(
    controls: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = calculateWindowSizeClass(),
    content: @Composable (PaddingValues) -> Unit,
) {
    var controlsExpanded by remember { mutableStateOf(false) }

    val controlsBackground = MaterialTheme.colorScheme.surfaceContainer
    val background = MaterialTheme.colorScheme.background
    val maxWidthControls = 300.dp
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            BottomSheetScaffold(
                modifier = modifier,
                sheetContent = { controls() },
                content = { content(it) },
                containerColor = background,
                sheetContainerColor = controlsBackground,
            )
        }

        WindowWidthSizeClass.Medium -> {
            Box(modifier = modifier.background(background)) {
                content(PaddingValues())
                Surface(
                    modifier = Modifier.fillMaxHeight().widthIn(max = maxWidthControls),
                    color = controlsBackground,
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        IconButton(onClick = { controlsExpanded = !controlsExpanded }) {
                            AnimatedContent(controlsExpanded) {
                                val icon = if (it) Icons.Default.Close else Icons.Default.Menu
                                Icon(icon, contentDescription = null)
                            }
                        }

                        val enter = expandHorizontally() + fadeIn()
                        val exit = shrinkHorizontally() + fadeOut()
                        AnimatedVisibility(visible = controlsExpanded, enter = enter, exit = exit) {
                            controls()
                        }
                    }
                }
            }
        }

        WindowWidthSizeClass.Expanded -> {
            Row(modifier = modifier.background(background), horizontalArrangement = Arrangement.SpaceEvenly) {
                Surface(
                    modifier = Modifier.fillMaxHeight().widthIn(max = maxWidthControls),
                    color = controlsBackground,
                ) {
                    controls()
                }
                content(PaddingValues())
            }
        }
    }
}

private val shapes: List<Shape> = listOf(RectangleShape, CircleShape, RoundedCornerShape(8.dp))

@Composable
private fun ControlsSheet(
    state: AppState,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = spacedBy(4.dp),
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    onBoxColorChange: () -> Unit,
    onShadowColorChange: () -> Unit,
    maxBoxSize: Dp = 200.dp,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        state = listState,
        contentPadding = contentPadding,
    ) {
        title(text = "Box")
        with(state.boxValues) {
            item {
                DualDpSliderSection(
                    title = "Size",
                    firstTitle = "Width",
                    firstDp = size.width,
                    onFirstDpChange = state::boxWidth,
                    secondTitle = "Height",
                    secondDp = size.height,
                    onSecondDpChange = state::boxHeight,
                    min = 10.dp,
                    max = maxBoxSize,
                )
            }
            item {
                ShapeSelectorSection(
                    "Shape",
                    selected = shape,
                    shapes = shapes,
                    onShapeSelect = state::boxShape,
                )
            }

            colorPicker(color, onBoxColorChange)
        }

        divider()

        title(text = "Shadow")
        with(state.shadowValues) {
            dpSliderSection("Blur Radius", dp = blurRadius, onDpChange = state::blur, max = maxBoxSize, min = 0.dp)
            dpSliderSection(
                "Spread Radius",
                dp = spreadRadius,
                onDpChange = state::spread,
                max = maxBoxSize,
                min = 0.dp
            )

            colorPicker(color, onShadowColorChange)

            item {
                val maxOffset = 50.dp
                DualDpSliderSection(
                    title = "Offset",
                    firstTitle = "X",
                    firstDp = offset.x,
                    onFirstDpChange = state::offsetX,
                    secondTitle = "Y",
                    secondDp = offset.y,
                    onSecondDpChange = state::offsetY,
                    min = -maxOffset,
                    max = maxOffset,
                )
            }

            item {
                ShapeSelectorSection(
                    title = "Shape",
                    selected = shape,
                    shapes = shapes,
                    onShapeSelect = state::shadowShape,
                )
            }
            item {
                SwitchSection(
                    "Clip",
                    checked = clip,
                    onCheckedChange = state::switchClip,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                SwitchSection(
                    "Inset",
                    checked = inset,
                    onCheckedChange = state::switchInset,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private fun LazyListScope.title(text: String) = item { Title(text) }
private fun LazyListScope.divider() = item { HorizontalDivider() }
private fun LazyListScope.colorPicker(color: Color, onColorChange: () -> Unit) = item {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onColorChange),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween,
    ) {
        Text("Color")
        Box(Modifier.size(24.dp).background(color))
    }
}

private fun LazyListScope.dpSliderSection(title: String, dp: Dp, onDpChange: (Dp) -> Unit, min: Dp, max: Dp) = item {
    DpSliderSection(title = title, dp = dp, onDpChange = onDpChange, min = min, max = max)
}

@Composable
fun ShapeSelectorSection(
    title: String,
    selected: Shape,
    shapes: List<Shape>,
    onShapeSelect: (Shape) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(title)
        ShapeSelector(
            modifier = Modifier.fillMaxWidth(),
            selected = selected,
            onShapeSelect = onShapeSelect,
            shapes = shapes,
        )
    }
}

@Composable
fun SwitchSection(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically,
    ) {
        Text(title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun DpSliderSection(title: String, dp: Dp, onDpChange: (Dp) -> Unit, min: Dp, max: Dp, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(title)
        DpSlider(dp, onDpChange, min = min, max = max)
    }
}

@Composable
fun DualDpSliderSection(
    title: String,
    firstTitle: String,
    firstDp: Dp,
    onFirstDpChange: (Dp) -> Unit,
    secondTitle: String,
    secondDp: Dp,
    onSecondDpChange: (Dp) -> Unit,
    min: Dp,
    max: Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        val (locked, setLocked) = remember { mutableStateOf(true) }

        val lockedChange: (Dp) -> Unit = { dp ->
            onFirstDpChange(dp)
            onSecondDpChange(dp)
        }
        val optionalLockedChange = lockedChange.takeIf { locked }

        Row(verticalAlignment = CenterVertically, horizontalArrangement = spacedBy(8.dp)) {
            Text(text = title)
            Spacer(Modifier.weight(1f))
            Text("Locked", style = MaterialTheme.typography.labelSmall)
            Switch(checked = locked, onCheckedChange = setLocked)
        }
        val titleModifier = Modifier.weight(1f)
        val sliderModifier = Modifier.weight(3f)
        Row(verticalAlignment = CenterVertically) {
            Text(modifier = titleModifier, text = firstTitle)
            DpSlider(
                modifier = sliderModifier,
                dp = firstDp,
                onDpChange = optionalLockedChange ?: onFirstDpChange,
                max = max,
                min = min,
            )
        }
        Row(verticalAlignment = CenterVertically) {
            Text(modifier = titleModifier, text = secondTitle)
            DpSlider(
                modifier = sliderModifier,
                dp = secondDp,
                onDpChange = optionalLockedChange ?: onSecondDpChange,
                max = max,
                min = min,
            )
        }
    }
}

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = text, style = MaterialTheme.typography.headlineSmall)
}

private fun Modifier.boxShadow(shadowValues: ShadowValues) = boxShadow(
    blurRadius = shadowValues.blurRadius,
    color = shadowValues.color,
    spreadRadius = shadowValues.spreadRadius,
    offset = shadowValues.offset,
    shape = shadowValues.shape,
    clip = shadowValues.clip,
    inset = shadowValues.inset,
)
