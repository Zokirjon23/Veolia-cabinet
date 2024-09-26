package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.ui.theme.gray50Color
import uz.veolia.cabinet.ui.theme.primaryColor

@Composable
fun SwitchApp(
    selected: Boolean,
    modifier: Modifier = Modifier,
    enable : Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        selected,
        modifier = modifier,
        enabled = enable,
        onCheckedChange = {
            onCheckedChange(it)
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = primaryColor,
            uncheckedBorderColor = Color.Transparent,
            uncheckedThumbColor = gray50Color,
            uncheckedTrackColor = Color.White
        )
    )
}

@Preview
@Composable
fun AdminProfilePreview() {
    Box(Modifier.fillMaxSize().background(Color.Gray)) {
        SwitchApp(false) {}
    }
}