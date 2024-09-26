package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_back
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.companent.ButtonApp
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.TabApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.companent.TextFieldApp
import uz.veolia.cabinet.ui.companent.ToastApp
import uz.veolia.cabinet.ui.companent.ToastError
import uz.veolia.cabinet.ui.intent.AddConsumerIntent
import uz.veolia.cabinet.ui.theme.gray15Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.ui.uistate.AddConsumerUiState

@Composable
fun AddConsumerScreenPhone(presenter: CommonPresenter<AddConsumerIntent, AddConsumerUiState>) {
    AddConsumerPhoneContent(
        uiState = presenter.uiState.subscribeAsState().value,
        presenter::onEventDispatcher
    )
}


@Composable
private fun AddConsumerPhoneContent(
    uiState: AddConsumerUiState,
    intent: (AddConsumerIntent) -> Unit
) {
    var personType by remember { mutableIntStateOf(if (RoleManager.canAddPhysical()) 0 else 1) }
    var activeSelect by remember { mutableIntStateOf(0) }
    var consumerId by remember { mutableStateOf("") }
    var consumerName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var rooms by remember { mutableStateOf("") }
    var roomer by remember { mutableStateOf("") }
    var cadastre by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(whiteColor)
                .padding(bottom = 95.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { intent(AddConsumerIntent.Back) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    IconApp(resource = Res.drawable.ic_back)
                }
                TextApp(
                    text = "Добавление потребителя",
                    fontFamily = lato_bold,
                    fontSize = 22.sp
                )
            }

            Column(
                modifier = Modifier
//                    .imePadding()
                    .padding(top = 9.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TextApp(text = "Тип потребителя", fontSize = 13.sp, color = gray70Color)
                TabApp(
                    modifier = Modifier.padding(bottom = 22.dp, top = 4.dp),
                    selectedItemIndex = personType,
                    items = listOf("Физ. лицо", "Юр. лицо"),
                    enable = RoleManager.canAddJuridical() && RoleManager.canAddPhysical()
                ) {
                    personType = it
                }
                TextApp(text = "Статус потребителя", fontSize = 13.sp, color = gray70Color)
                TabApp(
                    modifier = Modifier.padding(top = 4.dp),
                    selectedItemIndex = activeSelect,
                    items = listOf("Активный", "Неактивный"),
                    enable = true
                ) {
                    activeSelect = it
                }

                TextFieldApp(
                    modifier = Modifier.padding(top = 22.dp),
                    title = "Лицевой счет",
                    value = consumerId,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ) { consumerId = it }

                TextFieldApp(
                    modifier = Modifier.padding(top = 22.dp),
                    title = "Потребитель",
                    value = consumerName,
                ) { consumerName = it }

                TextFieldApp(
                    modifier = Modifier.padding(top = 22.dp),
                    title = "Адрес",
                    maxLines = Int.MAX_VALUE,
                    value = address,
                    innerModifier = Modifier.fillMaxSize()
                ) {
                    address = it
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    TextFieldApp(
                        title = "Площадь",
                        value = area,
                        modifier = Modifier.weight(0.333f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    ) {
                        area = it
                    }
                    TextFieldApp(
                        title = "Комнаты",
                        value = rooms,
                        modifier = Modifier.weight(0.333f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    ) {
                        rooms = it
                    }
                    TextFieldApp(
                        title = "Жильцы",
                        value = roomer,
                        modifier = Modifier.weight(0.333f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    ) {
                        roomer = it
                    }
                }

                TextFieldApp(
                    title = "Кадастр",
                    value = cadastre,
                    modifier = Modifier.padding(top = 22.dp)
                ) {
                    cadastre = it
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
        ) {
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
                        AddConsumerIntent.Create(
                            personType,
                            activeSelect,
                            consumerId,
                            consumerName,
                            address,
                            cadastre,
                            area,
                            rooms,
                            roomer
                        )
                    )
                },
                enabled = consumerId.isNotEmpty() && consumerName.isNotEmpty() && address.isNotEmpty()
                        && area.isNotEmpty() && rooms.isNotEmpty() && roomer.isNotEmpty()
                        && cadastre.isNotEmpty() && !uiState.loading,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }

        if (uiState.loading) {
            CircularProgressIndicatorApp()
        }

        uiState.message?.let {
            ToastError(text = it) { intent(AddConsumerIntent.ToastHide) }
        }

        if (uiState.serverError) {
            ToastError { intent(AddConsumerIntent.ToastHide) }
        }

        uiState.success?.let {
            ToastApp(text = it) { intent(AddConsumerIntent.ToastHide) }
        }
    }
}

@Preview
@Composable
private fun AddConsumerPreview() {
    AddConsumerPhoneContent(AddConsumerUiState()) {}
}
