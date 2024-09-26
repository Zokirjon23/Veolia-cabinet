package uz.veolia.cabinet.ui.screen.phone.accrual

import  androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_about
import kotlinproject.composeapp.generated.resources.ic_back
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.companent.ButtonApp
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.SpinnerApp
import uz.veolia.cabinet.ui.companent.TabApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.companent.TextFieldApp
import uz.veolia.cabinet.ui.companent.ToastApp
import uz.veolia.cabinet.ui.companent.ToastError
import uz.veolia.cabinet.ui.intent.CreateAccrualIntent
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.gray50Color
import uz.veolia.cabinet.ui.theme.gray15Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.uistate.CreateAccrualUiState
import uz.veolia.cabinet.util.extension.toDividedFormat

@Composable
fun CreateAccrualPhoneScreen(presenter: CommonPresenter<CreateAccrualIntent, CreateAccrualUiState>) {
    val uiState = presenter.uiState.subscribeAsState()
    CreateAccrualPhoneContent(uiState = uiState.value, presenter::onEventDispatcher)
}

@Composable
fun CreateAccrualPhoneContent(uiState: CreateAccrualUiState, intent: (CreateAccrualIntent) -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    var accType by remember { mutableIntStateOf(-1) }
    var accSum by remember { mutableStateOf("") }
    var accTiyin by remember { mutableStateOf("") }

//    val navigator = LocalNavigator.current
//    LaunchedEffect(key1 = uiState.success) {
//        if (uiState.success) {
//            delay(1500)
////            block(uiState.balance)
//            navigator?.pop()
//        }
//    }

    Box(modifier = Modifier.systemBarsPadding()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(
                Modifier.background(Color.White)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { intent(CreateAccrualIntent.Back) },
                        Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconApp(resource = Res.drawable.ic_back, tint = appDark)
                    }
                    Column {
                        TextApp(
                            text = "Добавление начисления",
                            fontSize = 22.sp,
                            fontFamily = lato_bold,
                            lineHeight = 26.4.sp
                        )
                        TextApp(
                            text = uiState.name,
                            color = gray50Color,
                            fontSize = 13.sp,
                            lineHeight = 15.6.sp
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(0.5f)) {
                        TextApp(
                            text = "Текущий баланс",
                            fontSize = 13.sp,
                            color = gray70Color,
                            lineHeight = 15.6.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        TextApp(
                            text = uiState.balance.toDividedFormat(),
                            fontSize = 13.sp,
                            fontFamily = lato_bold,
                        )
                    }
                    VerticalDivider(
                        Modifier
                            .padding(vertical = 9.dp)
                            .padding(horizontal = 20.dp)
                    )
                    Column(Modifier.weight(0.5f)) {
                        TextApp(
                            text = "Ожидаемый баланс",
                            fontSize = 13.sp,
                            color = gray70Color,
                            lineHeight = 15.6.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        TextApp(
                            text = "${(if (accSum.isNotEmpty() || accTiyin.isNotEmpty()) (uiState.balance.toFloatOrNull() ?: 0f) - if (tab == 0) ("${accSum.trim()}.${accTiyin.ifEmpty { 0 }}".toFloatOrNull() ?: 0f) else (-("${accSum.trim()}.${accTiyin.ifEmpty { 0 }}".toFloatOrNull() ?: 0f)) else "")}",
                            fontSize = 13.sp,
                            color = gray70Color,
                            fontFamily = lato_bold,
                        )
                    }
                }
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                        .height(55.dp)
                        .background(gray15Color, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconApp(resource = Res.drawable.ic_about, tint = appDark)
                    TextApp(
                        text = "Добавляемое начисление нельзя будет изменить или удалить",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
                TextApp(
                    text = "Тип начисления *",
                    color = gray70Color,
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 4.dp)
                )
                TabApp(
                    selectedItemIndex = tab,
                    items = listOf("Дебит", "Кредит"),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    tab = it
                }


                SpinnerApp(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 22.dp),
                    hint = "Не выбран",
                    title = "Тип начисления *",
                    list = listOf(
                        "Основной долг",
                        "Судебный гос. пошлина и почта расходы",
                        "Пеня"
                    ), onSelected = {
                        accType = it
                    }
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    TextFieldApp(
                        title = "Сумма *",
                        value = accSum,
                        hint = "0",
                        modifier = Modifier.weight(0.75f)
                    ) {
                        accSum = it
                    }
                    TextFieldApp(
                        title = "(тийин) *",
                        value = accTiyin,
                        modifier = Modifier.weight(0.25f),
                        hint = ".00"
                    ) {
                        accTiyin = it
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(Modifier.safeDrawingPadding()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(gray15Color)
                    )
                    ButtonApp(
                        text = "Добавить",
                        onClick = {
                            intent(
                                CreateAccrualIntent.Create(
                                    uiState.consumer,
                                    tab,
                                    accType,
                                    "$accSum${accTiyin.ifEmpty { ".00" }}"
                                )
                            )
                        },
                        enabled = !uiState.loading && accSum.isNotEmpty() && accType != -1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        if (uiState.loading) {
            CircularProgressIndicatorApp()
        }

        if (uiState.serverError) {
            ToastError { intent(CreateAccrualIntent.ToastHide) }
        }

        uiState.message?.let {
            ToastError(text = it) { intent(CreateAccrualIntent.ToastHide) }
        }

        if (uiState.success) {
            ToastApp(text = "Успех") { intent(CreateAccrualIntent.ToastHide) }
        }
    }
}

@Preview
@Composable
private fun CreateAccrualPreview() {
    CreateAccrualPhoneContent(uiState = CreateAccrualUiState()) {}
}