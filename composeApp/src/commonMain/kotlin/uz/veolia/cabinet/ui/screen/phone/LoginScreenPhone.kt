package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.error_connection_server
import kotlinproject.composeapp.generated.resources.login_background
import kotlinproject.composeapp.generated.resources.login_logo
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.companent.ButtonApp
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import uz.veolia.cabinet.ui.companent.ImageApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.companent.TextFieldApp
import uz.veolia.cabinet.ui.companent.TextFieldPasswordApp
import uz.veolia.cabinet.ui.companent.ToastError
import uz.veolia.cabinet.ui.intent.LoginIntent
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.uistate.LoginUiState

@Composable
fun LoginScreenPhone(presenter: CommonPresenter<LoginIntent, LoginUiState>) {
//    val systemUiController = rememberSystemUiController()
//    SideEffect {
//        systemUiController.setStatusBarColor(Color.Transparent)
//        systemUiController.setNavigationBarColor(Color.White)
//        systemUiController.isSystemBarsVisible = true
//    }
    LoginPhoneContent(presenter.uiState.subscribeAsState().value, presenter::onEventDispatcher)
}

@Composable
fun LoginPhoneContent(uiState: LoginUiState, intent: (LoginIntent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ImageApp(
            id = Res.drawable.login_background,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        ImageApp(
            id = Res.drawable.login_logo,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66000000)))
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
                .background(
                    Color.White, RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp)
        ) {
            TextApp(
                text = "Авторизация",
                fontFamily = lato_bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 12.dp, bottom = 15.dp)
            )
            var login by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            TextFieldApp(title = "Логин", value = login) {
                login = it
            }
            TextFieldPasswordApp(
                title = "Пароль",
                value = password,
                modifier = Modifier.padding(top = 10.dp),
                onOk = { intent(LoginIntent.Login(login, password)) }
            ) {
                password = it
            }

            ButtonApp(
                text = "Войти",
                onClick = { intent(LoginIntent.Login(login, password)) },
                enabled = login.isNotEmpty() && password.isNotEmpty() && !uiState.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
        if (uiState.serverError) {
            ToastError(text = stringResource(Res.string.error_connection_server)) {
                intent(LoginIntent.ToastHide)
            }
        }

        uiState.message?.let {
            ToastError(text = it) {
                intent(LoginIntent.ToastHide)
            }
        }

        if (uiState.loading) {
            CircularProgressIndicatorApp()
        }
    }
}

@Preview
@Composable
private fun LoginPhonePreview() {
    LoginPhoneContent(LoginUiState()) {}
}