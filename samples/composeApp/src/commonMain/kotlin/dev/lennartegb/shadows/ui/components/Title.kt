package dev.lennartegb.shadows.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Title(text: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = text, style = MaterialTheme.typography.headlineSmall)
}

@Preview
@Composable
private fun TitlePreview() {
    Title("Title")
}
