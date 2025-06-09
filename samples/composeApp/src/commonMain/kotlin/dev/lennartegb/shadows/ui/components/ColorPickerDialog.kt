package dev.lennartegb.shadows.ui.components

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColorPickerDialog(
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

@Preview
@Composable
private fun ColorPickerDialogPreview() {
    ColorPickerDialog(Color.Red, onColorChanged = {}, onDismiss = {})
}
