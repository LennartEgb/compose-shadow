package dev.lennartegb.shadows.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lennartegb.shadows.AppState
import dev.lennartegb.shadows.ShadowValues
import dev.lennartegb.shadows.boxShadow
import dev.lennartegb.shadows.icons.DarkMode
import dev.lennartegb.shadows.icons.LightMode
import dev.lennartegb.shadows.icons.ShadowIcons
import dev.lennartegb.shadows.ui.components.ColorPickerDialog
import dev.lennartegb.shadows.ui.components.ControlsSheet
import dev.lennartegb.shadows.ui.components.PaneScaffold
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun DetailScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    var changesBoxColor by remember { mutableStateOf(false) }
    var changesShadowColor by remember { mutableStateOf(false) }

    PaneScaffold(
        modifier = modifier,
        controls = {
            ControlsSheet(
                state = appState,
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

            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onClick = appState::toggleTheme,
            ) {
                Icon(
                    imageVector = if (appState.isDarkTheme) ShadowIcons.DarkMode else ShadowIcons.LightMode,
                    contentDescription = null,
                )
            }
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

private fun Modifier.boxShadow(shadowValues: ShadowValues) = boxShadow(
    blurRadius = shadowValues.blurRadius,
    color = shadowValues.color,
    spreadRadius = shadowValues.spreadRadius,
    offset = shadowValues.offset,
    shape = shadowValues.shape,
    clip = shadowValues.clip,
    inset = shadowValues.inset,
)
