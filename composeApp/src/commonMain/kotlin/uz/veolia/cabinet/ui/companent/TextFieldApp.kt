@file:OptIn(ExperimentalMaterial3Api::class)

package uz.veolia.cabinet.ui.companent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.gray20Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_regular
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_calendar
import kotlinproject.composeapp.generated.resources.ic_password_hide
import kotlinproject.composeapp.generated.resources.ic_password_showon
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextFieldApp(
    modifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    title: String,
    value: String,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    hint: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text
    ),
    onValueChange: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val coroutineScope = rememberCoroutineScope()
        TextApp(text = title, fontSize = 13.sp, color = gray70Color)
        TextField(
            modifier = innerModifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .padding(top = 4.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, gray20Color, RoundedCornerShape(10.dp)),
            placeholder = { TextApp(text = hint, color = gray70Color) },
            value = value,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = if (readOnly) backgroundColor else Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = if (readOnly) backgroundColor else Color.White,
                focusedIndicatorColor = backgroundColor
            ),
            textStyle = TextStyle(fontSize = 13.sp, color = appDark, fontFamily = lato_regular),
            maxLines = maxLines,
            readOnly = readOnly,
            visualTransformation = visualTransformation
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextFieldPasswordApp(
    modifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    title: String,
    value: String,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    onOk: () -> Unit = {},
    onValueChange: (String) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val coroutineScope = rememberCoroutineScope()
        TextApp(text = title, fontSize = 13.sp, color = gray70Color)
        TextField(
            modifier = innerModifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .padding(top = 4.dp)
                .height(50.dp)
                .border(1.dp, gray20Color, RoundedCornerShape(10.dp)),
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = if (readOnly) backgroundColor else Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = if (readOnly) backgroundColor else Color.White,
                focusedIndicatorColor = backgroundColor
            ),

            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 13.sp, color = appDark, fontFamily = lato_regular),
            maxLines = maxLines,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onOk() }),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(Res.drawable.ic_password_showon)
                else painterResource(Res.drawable.ic_password_hide)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, tint = Color(0xFF460000), contentDescription = null)
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTextFieldApp(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    readOnly: Boolean = false,
    onChoose: (String) -> Unit
) {
    var state by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        TextApp(text = title, fontSize = 13.sp, color = gray70Color)
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(50.dp)
                .border(1.dp, gray20Color, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable(enabled = !readOnly) { state = true }
                .background(if (readOnly) backgroundColor else Color.White)
        ) {
            TextApp(
                text = value,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(start = 14.dp, end = 65.dp)
                    .align(Alignment.CenterStart)
            )
            if (!readOnly)
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .height(1.dp)
                            .background(gray20Color)
                    )
                    IconButton(
                        onClick = { state = true },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        IconApp(resource = Res.drawable.ic_calendar, tint = gray70Color)
                    }
                }
        }
    }


    if (state)
        DatePickerDialogApp(onDateSelected = {
            onChoose(it)
            state = false
        }, onDismiss = { state = false })
}

@Preview
@Composable
private fun TextFieldPreview() {
    var state by remember {
        mutableStateOf("dfds")
    }
    DateTextFieldApp(
        value = state,
        readOnly = false,
        title = "fsdf",
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .padding(horizontal = 16.dp)
    ) { state = it }
}

//@Preview
//@Composable
//private fun OtpView() {
//    var counter by remember { mutableStateOf("") }
//    Row(modifier = Modifier.fillMaxWidth()) {
//        OtpTextField(title = "Показание", otpText = counter, otpCount = 5) { data, _ ->
//            counter = data
//        }
//    }
//}


//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun OtpTextFieldPhone(
//    modifier: Modifier = Modifier,
//    title: String = "",
//    otpText: String,
//    otpCount: Int = 6,
////    scanClicked: () -> Unit = {},
//    onOtpTextChange: (String, Boolean) -> Unit
//) {
////    var focusRequest by remember { mutableStateOf(false) }
//    Column(modifier) {
//        TextApp(text = title, fontSize = 13.sp, color = gray70Color)
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 4.dp)
//                .height(50.dp)
//                .border(1.dp, gray20Color, RoundedCornerShape(10.dp))
//                .clip(RoundedCornerShape(10.dp))
//                .background(backgroundColor)
//                .padding(horizontal = 15.dp)
//        ) {
////            BasicTextField(
////                modifier = Modifier
////                    .align(Alignment.CenterVertically)
////                    .onFocusEvent {
////                        focusRequest = it.hasFocus
////                    },
////                value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
////                onValueChange = {
////                    if (it.text.length <= otpCount) {
////                        onOtpTextChange(it.text, it.text.length == otpCount)
////                    }
////                },
////                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
////                decorationBox = {
////                    Row(
////                        verticalAlignment = Alignment.CenterVertically,
////                        horizontalArrangement = Arrangement.spacedBy(2.dp)
////                    ) {
////                        repeat(otpCount) { index ->
////                            val isFocused = otpText.length == index
////                            val char = when {
////                                index >= otpText.length -> "0"
////                                else -> otpText[index].toString()
//////                                else -> "0"
////                            }
////                            Box(
////                                modifier = modifier
////                                    .size(28.dp)
////                                    .border(
////                                        1.dp,
////                                        when {
////                                            isFocused && focusRequest -> gray70Color
////                                            else -> gray20Color
////                                        },
////                                        RoundedCornerShape(10.dp)
////                                    )
////                                    .background(Color.White)
////                            ) {
////                                TextApp(
////                                    modifier = Modifier.align(Alignment.Center),
////                                    text = char,
////                                    color = if (otpText.isNotEmpty() && otpText.length == index) appDark else gray70Color,
////                                )
////                            }
////                        }
////                    }
////                }
////            )
//
//            val defaultCellConfig = OhTeePeeCellConfiguration.withDefaults(
//                borderColor = gray20Color,
//                borderWidth = 1.dp,
//                shape = RoundedCornerShape(6.dp),
//                textStyle = TextStyle(
//                    color = appDark,
//                    fontFamily = lato_regular,
//                    fontSize = 13.sp
//                ),
//                placeHolderTextStyle = TextStyle(
//                    color = appDark.copy(0.4f),
//                    fontFamily = lato_regular,
//                    fontSize = 13.sp
//                )
//            )
//            val bringIntoViewRequester = remember { BringIntoViewRequester() }
//            val coroutineScope = rememberCoroutineScope()
//            OhTeePeeInput(
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//                    .onFocusEvent { focusState ->
//                        if (focusState.isFocused) {
//                            coroutineScope.launch {
//                                bringIntoViewRequester.bringIntoView()
//                            }
//                        }
//                    },
//                value = otpText,
//                onValueChange = onOtpTextChange,
//                configurations = OhTeePeeConfigurations.withDefaults(
//                    cellsCount = otpCount,
//                    emptyCellConfig = defaultCellConfig,
//                    activeCellConfig = defaultCellConfig.copy(
//                        borderColor = gray70Color,
//                        borderWidth = 1.dp
//                    ),
//                    cellModifier = Modifier
//                        .padding(end = 2.dp)
//                        .size(28.dp),
//                    placeHolder = "0"
//                ),
//            )
//            TextApp(
//                text = ",",
//                modifier = Modifier
//                    .align(Alignment.Bottom)
//                    .padding(bottom = 11.dp, start = 2.dp, end = 3.dp)
//            )
//            Row(
//                modifier = Modifier.align(Alignment.CenterVertically),
//                horizontalArrangement = Arrangement.spacedBy(2.dp)
//            ) {
//                repeat(3) {
//                    Box(
//                        modifier = Modifier
//                            .size(28.dp)
//                            .border(
//                                1.dp,
//                                gray20Color,
//                                RoundedCornerShape(6.dp)
//                            )
//                            .background(Color.White, RoundedCornerShape(6.dp))
//                    ) {
//                        TextApp(
//                            modifier = Modifier.align(Alignment.Center),
//                            text = "0",
//                            color = primaryColor.copy(0.5f)
//                        )
//                    }
//                }
//            }
//            TextApp(
//                text = "м3", modifier = Modifier
//                    .align(Alignment.Bottom)
//                    .padding(bottom = 11.dp, start = 5.dp, end = 3.dp)
//            )
////            Spacer(modifier = Modifier.weight(1f))
////            VerticalDivider(
////                modifier = Modifier
////                    .fillMaxHeight()
////                    .height(1.dp)
////                    .background(gray20Color)
////            )
////            IconButton(
////                onClick = scanClicked,
////                modifier = Modifier.padding(horizontal = 2.dp)
////            ) {
////                IconApp(id = R.drawable.ic_scan, tint = primaryColor)
////            }
//        }
//    }
//}


//@Composable
//fun OtpTextField(
//    modifier: Modifier = Modifier,
//    title: String = "",
//    otpText: String,
//    otpCount: Int = 6,
////    scanClicked: () -> Unit = {},
//    onOtpTextChange: (String, Boolean) -> Unit
//) {
////    var focusRequest by remember { mutableStateOf(false) }
//    Column(modifier) {
//        TextApp(text = title, fontSize = 13.sp, color = gray70Color)
//        Row(
//            modifier = Modifier
//                .padding(top = 4.dp)
//                .height(50.dp)
//                .border(1.dp, gray20Color, RoundedCornerShape(10.dp))
//                .clip(RoundedCornerShape(10.dp))
//                .background(backgroundColor)
//                .padding(horizontal = 15.dp)
//        ) {
////            BasicTextField(
////                modifier = Modifier
////                    .align(Alignment.CenterVertically)
////                    .onFocusEvent {
////                        focusRequest = it.hasFocus
////                    },
////                value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
////                onValueChange = {
////                    if (it.text.length <= otpCount) {
////                        onOtpTextChange(it.text, it.text.length == otpCount)
////                    }
////                },
////                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
////                decorationBox = {
////                    Row(
////                        verticalAlignment = Alignment.CenterVertically,
////                        horizontalArrangement = Arrangement.spacedBy(2.dp)
////                    ) {
////                        repeat(otpCount) { index ->
////                            val isFocused = otpText.length == index
////                            val char = when {
////                                index >= otpText.length -> "0"
////                                else -> otpText[index].toString()
//////                                else -> "0"
////                            }
////                            Box(
////                                modifier = modifier
////                                    .size(28.dp)
////                                    .border(
////                                        1.dp,
////                                        when {
////                                            isFocused && focusRequest -> gray70Color
////                                            else -> gray20Color
////                                        },
////                                        RoundedCornerShape(10.dp)
////                                    )
////                                    .background(Color.White)
////                            ) {
////                                TextApp(
////                                    modifier = Modifier.align(Alignment.Center),
////                                    text = char,
////                                    color = if (otpText.isNotEmpty() && otpText.length == index) appDark else gray70Color,
////                                )
////                            }
////                        }
////                    }
////                }
////            )
//
//            val defaultCellConfig = OhTeePeeCellConfiguration.withDefaults(
//                borderColor = gray20Color,
//                borderWidth = 1.dp,
//                shape = RoundedCornerShape(6.dp),
//                textStyle = TextStyle(
//                    color = appDark,
//                    fontFamily = lato_regular,
//                    fontSize = 13.sp
//                ),
//                placeHolderTextStyle = TextStyle(
//                    color = appDark.copy(0.4f),
//                    fontFamily = lato_regular,
//                    fontSize = 13.sp
//                )
//            )
//
//            OhTeePeeInput(
//                modifier = Modifier.align(Alignment.CenterVertically),
//                value = otpText,
//                onValueChange = onOtpTextChange,
//                configurations = OhTeePeeConfigurations.withDefaults(
//                    cellsCount = otpCount,
//                    emptyCellConfig = defaultCellConfig,
//                    activeCellConfig = defaultCellConfig.copy(
//                        borderColor = gray70Color,
//                        borderWidth = 1.dp
//                    ),
//                    cellModifier = Modifier
//                        .padding(end = 2.dp)
//                        .size(28.dp),
//                    placeHolder = "0"
//                ),
//            )
//            TextApp(
//                text = ",",
//                modifier = Modifier
//                    .align(Alignment.Bottom)
//                    .padding(bottom = 11.dp, start = 2.dp, end = 3.dp)
//            )
//            Row(
//                modifier = Modifier.align(Alignment.CenterVertically),
//                horizontalArrangement = Arrangement.spacedBy(2.dp)
//            ) {
//                repeat(3) {
//                    Box(
//                        modifier = modifier
//                            .size(28.dp)
//                            .border(
//                                1.dp,
//                                gray20Color,
//                                RoundedCornerShape(6.dp)
//                            )
//                            .background(Color.White, RoundedCornerShape(6.dp))
//                    ) {
//                        TextApp(
//                            modifier = Modifier.align(Alignment.Center),
//                            text = "0",
//                            color = primaryColor.copy(0.5f)
//                        )
//                    }
//                }
//            }
//            TextApp(
//                text = "м3", modifier = Modifier
//                    .align(Alignment.Bottom)
//                    .padding(bottom = 11.dp, start = 5.dp, end = 3.dp)
//            )
////            Spacer(modifier = Modifier.weight(1f))
////            VerticalDivider(
////                modifier = Modifier
////                    .fillMaxHeight()
////                    .height(1.dp)
////                    .background(gray20Color)
////            )
////            IconButton(
////                onClick = scanClicked,
////                modifier = Modifier.padding(horizontal = 2.dp)
////            ) {
////                IconApp(id = R.drawable.ic_scan, tint = primaryColor)
////            }
//        }
//    }
//}