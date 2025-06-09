package dev.lennartegb.shadows.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.lennartegb.shadows.icons.Close
import dev.lennartegb.shadows.icons.Menu
import dev.lennartegb.shadows.icons.ShadowIcons
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun PaneScaffold(
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
                content = content,
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
                                Icon(
                                    if (it) ShadowIcons.Close else ShadowIcons.Menu,
                                    contentDescription = null,
                                )
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
            Row(
                modifier = modifier.background(background),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
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

@Preview
@Composable
private fun PaneScaffoldPreview() {
    PaneScaffold(controls = {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) { repeat(5) { Text("Control $it") } }
    }) {
        Text("Detail", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
    }
}
