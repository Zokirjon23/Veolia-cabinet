package uz.veolia.cabinet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import network.chaintech.cmpimagepickncrop.CMPImagePickNCropDialog
import network.chaintech.cmpimagepickncrop.imagecropper.rememberImageCropper
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import uz.veolia.cabinet.di.koinConfiguration
import uz.veolia.cabinet.ui.AppTheme
import uz.veolia.cabinet.ui.navigation.RootComponentPhone
import uz.veolia.cabinet.ui.navigation.RootComponentPhoneImpl
import uz.veolia.cabinet.ui.screen.SplashScreen
import uz.veolia.cabinet.ui.screen.phone.AboutConsumerPhoneScreen
import uz.veolia.cabinet.ui.screen.phone.AddConsumerScreenPhone
import uz.veolia.cabinet.ui.screen.phone.ConsumerInfoScreenPhone
import uz.veolia.cabinet.ui.screen.phone.DashBoardScreenPhone
import uz.veolia.cabinet.ui.screen.phone.HistoryCounterPhoneScreen
import uz.veolia.cabinet.ui.screen.phone.HistoryPaymentPhoneScreen
import uz.veolia.cabinet.ui.screen.phone.LoginScreenPhone
import uz.veolia.cabinet.ui.screen.phone.accrual.AccrualPhoneScreen
import uz.veolia.cabinet.ui.screen.phone.accrual.CreateAccrualPhoneScreen


@Composable
@Preview
fun App(root: RootComponentPhoneImpl) {
    AppTheme {
        KoinApplication(::koinConfiguration) {
//            Children(
//                stack = root.stack,
//                modifier = Modifier.fillMaxSize(),
//                animation = backAnimation(
//                    backHandler = root.backHandler,
//                    onBack = root::onBackPressed,
//                ),
//            ) {
//                when (val instance = it.instance) {
//                    is RootComponentPhone.Child.ConsumerChild -> ConsumerInfoScreenPhone(
//                        instance.presenter
//                    )
//
//                    is RootComponentPhone.Child.CreateConsumerChild -> AddConsumerScreenPhone(
//                        instance.presenter
//                    )
//
//                    is RootComponentPhone.Child.DashboardChild -> DashBoardScreenPhone(
//                        instance.presenter
//                    )
//
//                    is RootComponentPhone.Child.LoginChild -> LoginScreenPhone(instance.presenter)
//                    is RootComponentPhone.Child.SendCounterChild -> {
////                        SendCounterScreenPhone(
////                            instance.presenter
////                        )
//                    }
//
//                    is RootComponentPhone.Child.CreateAccrualChild -> CreateAccrualPhoneScreen(
//                        instance.presenter
//                    )
//
//                    is RootComponentPhone.Child.SplashChild -> SplashScreen()
//                    is RootComponentPhone.Child.AboutConsumer -> {
//                        AboutConsumerPhoneScreen(
//                            data = instance.data,
//                            presenter = instance.presenter
//                        )
//                    }
//
//                    is RootComponentPhone.Child.Accrual -> {
//                        AccrualPhoneScreen(presenter = instance.presenter)
//                    }
//
//                    is RootComponentPhone.Child.HistoryCounter -> {
//                        HistoryCounterPhoneScreen(
//                            presenter = instance.presenter,
//                            name = instance.name
//                        )
//                    }
//
//                    is RootComponentPhone.Child.HistoryPayment -> {
//                        HistoryPaymentPhoneScreen(
//                            presenter = instance.presenter
//                        )
//                    }
//
//                    is RootComponentPhone.Child.PersonalData -> {
////                        PersonalDataPhoneScreen(
////                            instance.login,
////                            instance.password,
////                            instance.passport,
////                            instance.pinfl,
////                            instance.inn,
////                            instance.cadastre,
////                            instance.name,
////                        ) { instance.presenter.onEventDispatcher(PersonalDataIntent.Back) }
//                    }
//                }
//            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                val imageCropper = rememberImageCropper()
                var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
                var openImagePicker by remember { mutableStateOf(value = false) }

                CMPImagePickNCropDialog(
                    imageCropper = imageCropper,
                    openImagePicker = openImagePicker,
                    imagePickerDialogHandler = {
                        openImagePicker = it
                    },
                    selectedImageCallback = {
                        selectedImage = it
                    })

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .safeContentPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    selectedImage?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier.weight(1f))
                    }
                    if (selectedImage == null)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("No image selected !", color = Color.Black)
                        }

                    Button(
                        onClick = {
                            openImagePicker = true
                        },
                    ) { Text("Choose Image") }
                }
            }
        }
    }
}
