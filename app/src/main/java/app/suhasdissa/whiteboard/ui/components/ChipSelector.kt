package app.suhasdissa.whiteboard.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> ChipSelector(
    title: String? = null,
    selectedValue: T,
    crossinline onSelectionChanged: (T) -> Unit
) {
    val values = remember { enumValues<T>() }
    title?.let {
        Text(
            text = it,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
    LazyRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(values) { item ->
            ElevatedFilterChip(
                modifier = Modifier
                    .padding(end = 10.dp),
                label = {
                    Text(item.name)
                },
                selected = item == selectedValue,
                onClick = {
                    onSelectionChanged(item)
                },
                leadingIcon = {
                    if (item == selectedValue) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.scale(0.6f)
                        )
                    }
                }
            )
        }
    }
}
