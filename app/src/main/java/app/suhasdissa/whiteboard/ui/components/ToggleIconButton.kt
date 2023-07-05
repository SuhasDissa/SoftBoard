package app.suhasdissa.whiteboard.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun ToggleIconButton(
    onClick: () -> Unit,
    isToggled: Boolean,
    imageVector: ImageVector,
    @StringRes contentDescription: Int
) {
    IconToggleButton(checked = isToggled, onCheckedChange = { onClick() }) {
        Icon(imageVector, contentDescription = stringResource(contentDescription))
    }
}

@Composable
fun ToggleIconButton(
    onClick: () -> Unit,
    isToggled: Boolean,
    painter: Painter,
    @StringRes contentDescription: Int
) {
    IconToggleButton(checked = isToggled, onCheckedChange = { onClick() }) {
        Icon(painter, contentDescription = stringResource(contentDescription))
    }
}