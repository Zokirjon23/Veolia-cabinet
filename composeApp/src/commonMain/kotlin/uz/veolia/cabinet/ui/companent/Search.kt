package uz.veolia.cabinet.ui.companent

//import androidx.compose.foundation.background
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusEvent
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import uz.veolia.cabinet.R
//import uz.veolia.cabinet.ui.theme.gray5Color
//import uz.veolia.cabinet.ui.theme.primaryColor
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchApp(
//    modifier: Modifier = Modifier,
//    value: String,
//    onPersonClick: () -> Unit,
//    onFilterClick: () -> Unit,
//    onValueChange: (String) -> Unit
//) {
//    var isFocus by remember { mutableStateOf(false) }
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(50.dp)
//            .background(gray5Color, RoundedCornerShape(25.dp))
//            .padding(5.dp),
//        contentAlignment = Alignment.CenterStart
//    ) {
//        ButtonIcon(idRes = R.drawable.ic_person, onClick = onPersonClick)
//        BasicTextField(value = value, onValueChange = onValueChange, modifier = Modifier
//            .padding(horizontal = 60.dp)
//            .onFocusEvent {
//                isFocus = it.hasFocus
//            }
//            .background(Color.Transparent), decorationBox = @Composable { innerTextField ->
//            TextFieldDefaults.DecorationBox(
//                value = value,
//                innerTextField = innerTextField,
//                enabled = true,
//                singleLine = true,
//                visualTransformation = VisualTransformation.None,
//                interactionSource = remember { MutableInteractionSource() },
//                placeholder = {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        IconApp(id = R.drawable.ic_search)
//                        TextApp(text = "Поиск", modifier = Modifier.padding(start = 10.dp))
//                    }
//                },
//                colors = TextFieldDefaults.colors(
//                    cursorColor = primaryColor,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedContainerColor = Color.Transparent,
//                    focusedContainerColor = Color.Transparent
//                ),
//                contentPadding = PaddingValues(0.dp),
//            )
//        })
//        ButtonIcon(
//            idRes = R.drawable.ic_setting,
//            Modifier.align(Alignment.CenterEnd),
//            onFilterClick
//        )
//    }
//}