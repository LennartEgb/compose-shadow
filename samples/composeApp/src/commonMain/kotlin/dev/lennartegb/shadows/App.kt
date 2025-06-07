package dev.lennartegb.shadows

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val state = rememberAppState()
    MaterialTheme(
        colorScheme = if (state.isDarkTheme) darkColorScheme() else lightColorScheme(),
        shapes = MaterialTheme.shapes,
        typography = MaterialTheme.typography,
    ) {
        DetailScreen(modifier = Modifier.fillMaxSize(), appState = state)
    }
}
