package uz.veolia.cabinet.ui.screen.phone
//
//import android.Manifest
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.VerticalDivider
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.arkivanov.decompose.extensions.compose.subscribeAsState
//import kotlinproject.composeapp.generated.resources.Res
//import kotlinproject.composeapp.generated.resources.ic_back
//import kotlinproject.composeapp.generated.resources.ic_cancel
//import kotlinproject.composeapp.generated.resources.ic_upload
//import uz.veolia.cabinet.presenter.phone.CommonPresenter
//import uz.veolia.cabinet.ui.companent.ButtonApp
//import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
//import uz.veolia.cabinet.ui.companent.IconApp
//import uz.veolia.cabinet.ui.companent.OtpInputField
//import uz.veolia.cabinet.ui.companent.TextApp
//import uz.veolia.cabinet.ui.companent.ToastApp
//import uz.veolia.cabinet.ui.companent.ToastError
//import uz.veolia.cabinet.ui.intent.SendCounterIntent
//import uz.veolia.cabinet.ui.theme.appDark
//import uz.veolia.cabinet.ui.theme.backgroundColor
//import uz.veolia.cabinet.ui.theme.gray20Color
//import uz.veolia.cabinet.ui.theme.gray50Color
//import uz.veolia.cabinet.ui.theme.gray15Color
//import uz.veolia.cabinet.ui.theme.gray70Color
//import uz.veolia.cabinet.ui.theme.lato_bold
//import uz.veolia.cabinet.ui.theme.primaryColor
//import uz.veolia.cabinet.ui.theme.whiteColor
//import uz.veolia.cabinet.ui.uistate.SendCounterUiState
//import uz.veolia.cabinet.util.extension.dashedBorder
//
//@Composable
//fun SendCounterScreenPhone(presenter: CommonPresenter<SendCounterIntent, SendCounterUiState>) {
//    SendCounterContentPhone(
//        uiState = presenter.uiState.subscribeAsState().value,
//        presenter::onEventDispatcher
//    )
//}
//
//@Composable
//fun SendCounterContentPhone(uiState: SendCounterUiState, intent: (SendCounterIntent) -> Unit) {
//    Box(
//        modifier = Modifier
//            .systemBarsPadding()
//            .imePadding()
//    ) {
//        val permissionError = remember {
//            mutableStateOf(false)
//        }
//        Column(
//            Modifier
//                .fillMaxSize()
//                .background(backgroundColor)
//        ) {
//            Column(
//                Modifier.background(Color.White)
//            ) {
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(64.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(
//                        onClick = { intent(SendCounterIntent.Back) },
//                        Modifier.padding(horizontal = 4.dp)
//                    ) {
//                        IconApp(Res.drawable.ic_back, tint = appDark)
//                    }
//                    Column {
//                        TextApp(
//                            text = "Потребитель",
//                            fontSize = 22.sp,
//                            fontFamily = lato_bold,
//                            lineHeight = 26.4.sp
//                        )
//                        TextApp(
//                            text = uiState.name ?: "",
//                            color = gray50Color,
//                            fontSize = 13.sp,
//                            lineHeight = 15.6.sp
//                        )
//                    }
//                }
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(54.dp)
//                        .padding(horizontal = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column(Modifier.weight(0.5f)) {
//                        TextApp(
//                            text = "Последнее показание",
//                            fontSize = 13.sp,
//                            color = gray70Color,
//                            lineHeight = 15.6.sp,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//                        TextApp(
//                            text = "${uiState.lastMeter ?: "0"} м3",
//                            fontSize = 13.sp,
//                            color = gray70Color,
//                            fontFamily = lato_bold,
//                        )
//                    }
//                    VerticalDivider(
//                        Modifier
//                            .padding(vertical = 9.dp)
//                            .padding(horizontal = 20.dp)
//                    )
//                    Column(Modifier.weight(0.5f)) {
//                        TextApp(
//                            text = "Текущее показание",
//                            fontSize = 13.sp,
//                            color = gray70Color,
//                            lineHeight = 15.6.sp,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//                        TextApp(
//                            text = "${uiState.list.sumOf { it.meter?.toIntOrNull() ?: 0 }} м3",
//                            fontSize = 13.sp,
//                            color = gray70Color,
//                            fontFamily = lato_bold,
//                        )
//                    }
//                }
//            }
//
//            LazyColumn(
//                Modifier.padding(bottom = 3.dp),
//                contentPadding = PaddingValues(bottom = 85.dp)
//            ) {
//                item {
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp)
//                            .padding(top = 20.dp, bottom = 4.dp)
//                            .background(gray15Color, RoundedCornerShape(10.dp))
//                            .padding(horizontal = 16.dp, vertical = 6.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        IconApp(id = R.drawable.ic_about, tint = appDark)
//                        TextApp(
//                            text = "Введите показания и добавьте фото счетчика. Если счетчиков несколько, то нажмите на Добавить счетчик",
//                            modifier = Modifier.padding(horizontal = 16.dp),
//                            fontSize = 14.sp,
//                            lineHeight = 18.85.sp
//                        )
//                    }
//                }
//                itemsIndexed(uiState.list) { index, item ->
//                    var openPicker by remember { mutableStateOf(false) }
//                    val image = remember { mutableStateOf<Uri?>(null) }
//                    val imageCropLauncher =
//                        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
//                            if (result.isSuccessful) {
//                                result.uriContent?.let { uri ->
//                                    val inputStream =
//                                        context.contentResolver.openInputStream(uri)
//                                    val imageByteArray = inputStream?.readBytes()
//                                    intent(
//                                        SendCounterIntent.ImageUpload(
//                                            imageByteArray,
//                                            uri,
//                                            index
//                                        )
//                                    )
//                                    image.value = uri
//                                }
//                            }
//                        }
//
//                    val permission = rememberPermissionState(
//                        Manifest.permission.CAMERA
//                    ) {
//                        if (it) {
//                            openPicker = true
//                        } else {
//                            permissionError.value = false
//                        }
//                    }
//
//                    Box {
//                        Column(
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(top = 16.dp)
//                                .padding(horizontal = 16.dp)
//                                .background(
//                                    whiteColor,
//                                    RoundedCornerShape(10.dp)
//                                )
//                                .padding(16.dp)
//                        ) {
//                            TextApp(
//                                text = "Счетчик ${index + 1}",
//                                fontSize = 13.sp,
//                                color = appDark,
//                                lineHeight = 15.6.sp,
//                                fontFamily = lato_bold,
//                                modifier = Modifier.padding(bottom = 4.dp)
//                            )
//                            val focusManager = LocalFocusManager.current
//                            val value = remember { mutableStateOf(item.meter!!) }
//                            OtpInputField(
//                                otp = value,
//                                count = 5,
//                                modifierBox = Modifier.fillMaxWidth(),
//                                otpTextType = KeyboardType.Number
//                            )
//                            LaunchedEffect(key1 = value.value) {
//                                intent(SendCounterIntent.OnInputChange(value.value, index))
//                            }
////                            OtpTextFieldPhone(
////                                modifier = Modifier
////                                    .fillMaxWidth(),
//////                                            scanClicked = {
//////                                            permissionError.value = false
//////                                            if (permission.status == PermissionStatus.Granted) {
//////                                                intent(SendCounterIntent.OpenCameraScreen)
//////                                            } else {
//////                                                permission.launchPermissionRequest()
//////                                            }
//////                                            },
////                                title = "Показание",
////                                otpText = item.meter!!,
////                                otpCount = 5
////                            ) { data, _ ->
////                                intent(SendCounterIntent.OnInputChange(data, index))
////                            }
//                            LaunchedEffect(key1 = Unit) {
//                                focusManager.clearFocus(true)
//                            }
//                            TextApp(
//                                text = "Введите цифры только черного цвета с счетчика",
//                                fontSize = 12.sp,
//                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
//                            )
//
//                            TextApp(
//                                modifier = Modifier.padding(top = 4.dp),
//                                text = "Фото",
//                                color = gray70Color,
//                                fontSize = 13.sp
//                            )
//                            if (item.fileLink != null) {
//                                Box {
//                                    LoadImageAsync(
//                                        Modifier
//                                            .padding(top = 4.dp)
//                                            .clip(RoundedCornerShape(10.dp))
//                                            .size(68.dp),
//                                        placeholderId = R.drawable.place_holder,
//                                        url = image.value.toString()
//                                    )
//                                    Box(
//                                        modifier = Modifier
//                                            .align(Alignment.TopEnd)
//                                            .clip(CircleShape)
//                                            .clickable {
//                                                intent(
//                                                    SendCounterIntent.DeleteItem(
//                                                        index
//                                                    )
//                                                )
//                                            }
//                                            .size(24.dp)
//                                            .background(appDark),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        IconApp(
//                                            resource = Res.drawable.ic_cancel,
//                                            tint = whiteColor
//                                        )
//                                    }
//                                }
//                            } else {
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(top = 4.dp)
//                                        .height(50.dp)
//                                        .dashedBorder(
//                                            color = gray20Color,
//                                            shape = RoundedCornerShape(12.dp)
//                                        )
//                                        .clip(RoundedCornerShape(10.dp))
//                                        .clickable {
//                                            permissionError.value = false
//                                            permission.launchPermissionRequest()
//                                        },
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    horizontalArrangement = Arrangement.Center
//                                ) {
//                                    IconApp(
//                                        resource = Res.drawable.ic_upload,
//                                        tint = primaryColor
//                                    )
//                                    Column(modifier = Modifier.padding(start = 11.dp)) {
//                                        TextApp(
//                                            text = "Загрузить",
//                                            color = primaryColor,
//                                            fontSize = 13.sp,
//                                            modifier = Modifier.padding(bottom = 2.dp)
//                                        )
//                                        TextApp(
//                                            text = ".jpg .png до 5 Мб",
//                                            color = gray50Color,
//                                            fontSize = 12.sp
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
//
//                        if (index != 0)
//                            Box(
//                                modifier = Modifier
//                                    .align(Alignment.TopEnd)
//                                    .padding(end = 11.dp, top = 14.dp)
//                                    .size(30.dp)
//                                    .clip(CircleShape)
//                                    .background(gray15Color)
//                                    .clickable {
//                                        intent(SendCounterIntent.Delete(index))
//                                    }) {
//                                IconApp(
//                                    resource = Res.drawable.ic_cancel,
//                                    Modifier
//                                        .size(8.12.dp)
//                                        .align(Alignment.Center)
//                                )
//                            }
//
//                        if (openPicker) {
//                            PickerDialogPhone(imageCropLauncher = imageCropLauncher) {
//                                openPicker = false
//                            }
//                        }
//                    }
//                }
//
//
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .height(50.dp)
//                            .dashedBorder(
//                                color = gray20Color,
//                                shape = RoundedCornerShape(12.dp),
//                                strokeWidth = 2.dp
//                            )
//                            .clip(RoundedCornerShape(10.dp))
//                            .clickable { intent(SendCounterIntent.Add) },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        TextApp(text = "Добавить счетчик")
//                    }
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .background(Color.White)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(1.dp)
//                    .background(gray15Color)
//            )
//            ButtonApp(
//                text = "Добавить",
//                enabled = uiState.list.count { it.fileLink != null } == uiState.list.size && uiState.list.size == uiState.list.count { it.meter?.isNotEmpty() == true },
//                onClick = {
//                    intent(SendCounterIntent.Send(uiState.list))
//                },
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(Alignment.BottomCenter)
//                    .fillMaxWidth()
//            )
//        }
//
//        if (uiState.serverError) {
//            ToastError { intent(SendCounterIntent.ToastHide) }
//        }
//
//        if (uiState.loading) {
//            CircularProgressIndicatorApp()
//        }
//
//        if (permissionError.value) {
//            ToastApp(text = "Доступ запрещен") {
//                permissionError.value = false
//            }
//        }
//
////        val navigation = LocalNavigator.current
//        uiState.success?.let {
//            ToastApp(text = it) { intent(SendCounterIntent.ToastHide) }
////            LaunchedEffect(true) {
////                delay(1500)
//////                onBack()
////                navigation?.pop()
////            }
//        }
//
//        uiState.message?.let {
//            ToastError(text = it) { intent(SendCounterIntent.ToastHide) }
//        }
//    }
//}
////
////@Preview
////@Composable
////private fun SendCounterPreview() {
////    SendCounterContentPhone(SendCounterUiState()) {}
////}