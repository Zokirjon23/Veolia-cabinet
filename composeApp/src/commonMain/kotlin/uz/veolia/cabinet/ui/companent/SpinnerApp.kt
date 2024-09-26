package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_drop_down
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.gray20Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_regular
import uz.veolia.cabinet.ui.theme.whiteColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerApp(
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit = {},
    hint: String,
    title: String,
    list: List<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            TextApp(text = title, fontSize = 13.sp, color = gray70Color)
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, gray20Color, RoundedCornerShape(10.dp)),
                placeholder = { TextApp(text = hint, color = gray70Color) },
                value = selectedOptionText,
                onValueChange = { },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = whiteColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = whiteColor,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 13.sp, color = appDark, fontFamily = lato_regular),
                readOnly = true,
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(50.dp)
                    ) {
                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .height(1.dp)
                                .background(gray20Color)
                        )
                        Box(Modifier.fillMaxSize()) {
                            IconApp(
                                resource = Res.drawable.ic_drop_down,
                                tint = Color(0xFF151515),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .rotate(if (expanded) 180f else 0f)
                            )
                        }
                    }
                },
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }, modifier = Modifier
                .fillMaxWidth()
                .background(whiteColor)
        ) {
            list.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(text = { TextApp(text = selectionOption) }, onClick = {
                    expanded = false
                    selectedOptionText = selectionOption
                    onSelected(index)
                })
            }
        }
    }
}



@Preview
@Composable
private fun SpinnerPreview() {
    Column(
        Modifier
            .fillMaxSize()
            .background(backgroundColor)) {
        SpinnerApp(
            hint = "444444",
            title = "fds",
            list = listOf("sfd", "fsd", "fsdfs")
        )
    }
}
