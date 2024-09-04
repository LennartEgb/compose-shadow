package dev.lennartegb.shadows

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun DetailScreen(modifier: Modifier = Modifier) {
    val appState = rememberAppState()
    val listState = rememberLazyListState()
    PaneScaffold(
        modifier = modifier,
        controls = {
            ControlsSheet(
                state = appState,
                listState = listState,
                contentPadding = PaddingValues(16.dp),
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Center) {
            Box(
                modifier = Modifier
                    .size(appState.boxValues.size)
                    .boxShadow(appState.shadowValues)
                    .background(appState.boxValues.color, shape = appState.boxValues.shape)
            )
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
                    color = controlsBackground
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
                Surface(modifier = Modifier.fillMaxHeight().widthIn(max = maxWidthControls), color = controlsBackground) {
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
                )
            }
            item {
                ShapeSelectorSection(
                    "Shape",
                    selected = shape,
                    shapes = shapes,
                    onShapeSelect = state::boxShape
                )
            }

            item { Text("Color") }
        }

        divider()

        title(text = "Shadow")
        with(state.shadowValues) {
            dpSliderSection("Blur Radius", dp = blurRadius, onDpChange = state::blur)
            dpSliderSection("Spread Radius", dp = spreadRadius, onDpChange = state::spread)

            colorPicker()

            item {
                DualDpSliderSection(
                    title = "Offset",
                    firstTitle = "X",
                    firstDp = offset.x,
                    onFirstDpChange = state::offsetX,
                    secondTitle = "Y",
                    secondDp = offset.y,
                    onSecondDpChange = state::offsetY,
                )
            }

            item {
                ShapeSelectorSection(
                    title = "Shape",
                    selected = shape,
                    shapes = shapes,
                    onShapeSelect = state::shadowShape
                )
            }
            item {
                SwitchSection(
                    "Clip switch",
                    checked = clip,
                    onCheckedChange = state::switchClip,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                SwitchSection(
                    "Inset",
                    checked = inset,
                    onCheckedChange = state::switchInset,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun LazyListScope.title(text: String) = item { Title(text) }
private fun LazyListScope.divider() = item { HorizontalDivider() }
private fun LazyListScope.colorPicker() = item { Text("Color") }
private fun LazyListScope.dpSliderSection(title: String, dp: Dp, onDpChange: (Dp) -> Unit, min: Dp = 0.dp) = item {
    DpSliderSection(title = title, dp = dp, onDpChange = onDpChange, min = min)
}

@Composable
fun ShapeSelectorSection(
    title: String,
    selected: Shape,
    shapes: List<Shape>,
    onShapeSelect: (Shape) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(title)
        ShapeSelector(
            modifier = Modifier.fillMaxWidth(),
            selected = selected,
            onShapeSelect = onShapeSelect,
            shapes = shapes
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
fun DpSliderSection(title: String, dp: Dp, onDpChange: (Dp) -> Unit, modifier: Modifier = Modifier, min: Dp = 0.dp) {
    Column(modifier) {
        Text(title)
        DpSlider(dp, onDpChange, min = min)
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
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        val (locked, setLocked) = remember { mutableStateOf(true) }

        val lockedChange: (Dp) -> Unit = { dp ->
            onFirstDpChange(dp)
            onSecondDpChange(dp)
        }

        Row(verticalAlignment = CenterVertically, horizontalArrangement = spacedBy(8.dp)) {
            Text(text = title)
            Spacer(Modifier.weight(1f))
            Text("Locked", style = MaterialTheme.typography.labelSmall)
            Switch(checked = locked, onCheckedChange = setLocked)
        }
        Row(verticalAlignment = CenterVertically) {
            Text(firstTitle)
            DpSlider(dp = firstDp, onDpChange = lockedChange.takeIf { locked } ?: onFirstDpChange)
        }
        Row(verticalAlignment = CenterVertically) {
            Text(secondTitle)
            DpSlider(dp = secondDp, onDpChange = lockedChange.takeIf { locked } ?: onSecondDpChange)
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