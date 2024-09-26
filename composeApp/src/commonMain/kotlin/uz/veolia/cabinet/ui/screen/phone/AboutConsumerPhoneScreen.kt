package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import kotlinproject.composeapp.generated.resources.error_connection_server
import kotlinproject.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.stringResource
import uz.veolia.cabinet.data.remote.response.ConsumerDetailsBody
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.companent.ButtonApp
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.SwitchApp
import uz.veolia.cabinet.ui.companent.TabApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.companent.TextFieldApp
import uz.veolia.cabinet.ui.companent.ToastApp
import uz.veolia.cabinet.ui.companent.ToastError
import uz.veolia.cabinet.ui.intent.AboutConsumerIntent
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.gray15Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.lato_regular
import uz.veolia.cabinet.ui.uistate.AboutConsumerUiState

@Composable
fun AboutConsumerPhoneScreen(
    data: ConsumerDetailsBody?,
    presenter: CommonPresenter<AboutConsumerIntent, AboutConsumerUiState>,
) {
    AboutConsumerContent(
        data,
        presenter.uiState.subscribeAsState().value,
        presenter::onEventDispatcher
    )
}

@Composable
private fun AboutConsumerContent(
    data: ConsumerDetailsBody?,
    uiState: AboutConsumerUiState,
    intent: (AboutConsumerIntent) -> Unit
) {
    var activeSelect by remember { mutableIntStateOf(if (data?.status == true) 0 else 1) }
    var name by remember { mutableStateOf(data?.consumerProfiles?.name ?: "") }
    var address by remember { mutableStateOf(data?.consumerAddress?.address ?: "") }
    var area by remember { mutableStateOf(data?.consumerObjects?.area ?: "") }
    var rooms by remember { mutableStateOf(data?.consumerObjects?.room ?: "") }
    var roomer by remember { mutableStateOf(data?.consumerObjects?.roomer ?: "") }
    var cadastre by remember { mutableStateOf(data?.consumerObjects?.cadastre ?: "") }
    Box(
        Modifier
            .systemBarsPadding()
            .imePadding()
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Row(
                Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { intent(AboutConsumerIntent.Back) },
                    Modifier.padding(horizontal = 4.dp)
                ) {
                    IconApp(resource = Res.drawable.ic_back, tint = appDark)
                }
                TextApp(
                    text = "О потребителе",
                    fontSize = 22.sp,
                    fontFamily = lato_bold,
                    lineHeight = 26.4.sp
                )
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 82.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (RoleManager.canEdit(data?.type))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(gray15Color)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextApp(text = "Редактирование", fontFamily = lato_bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        SwitchApp(
                            selected = uiState.switch,
                            enable = !uiState.loading
                        ) { intent(AboutConsumerIntent.SwitchChange(it)) }
                    }
                Column(Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
                    Row(Modifier.padding(bottom = 5.dp)) {
                        Title(text = "Тип потребителя:")
//                    Spacer(modifier = Modifier.weight(1f))
                        TextApp(
                            text = if (data?.type == "1") "Физ. лицо" else "Юр. лицо",
                            Modifier.padding(bottom = 15.dp, start = 5.dp)
                        )
                    }

                    Title(text = "Статус потребителя")
                    TabApp(
                        modifier = Modifier.padding(top = 4.dp),
                        selectedItemIndex = activeSelect,
                        items = listOf("Активный", "Неактивный"),
                        enable = uiState.switch
                    ) {
                        activeSelect = it
                    }

                    TextFieldApp(
                        modifier = Modifier.padding(top = 22.dp),
                        title = "Лицевой счет",
                        value = data?.consumer ?: "",
                        readOnly = true
                    ) { }
                    TextFieldApp(
                        modifier = Modifier.padding(top = 22.dp),
                        title = "Потребитель",
                        value = name,
                        readOnly = !uiState.switch
                    ) { name = it }

                    TextFieldApp(
                        title = "Адрес", value = address, modifier = Modifier
                            .padding(top = 22.dp), innerModifier = Modifier.fillMaxSize(),
                        readOnly = !uiState.switch
                    ) {
                        address = it
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 22.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        TextFieldApp(
                            title = "Площадь",
                            value = area,
                            modifier = Modifier.weight(0.333f),
                            readOnly = !uiState.switch
                        ) {
                            area = it
                        }
                        TextFieldApp(
                            title = "Комнаты",
                            value = rooms,
                            modifier = Modifier.weight(0.333f),
                            readOnly = !uiState.switch
                        ) {
                            rooms = it
                        }
                        TextFieldApp(
                            title = "Жильцы",
                            value = roomer,
                            modifier = Modifier.weight(0.333f),
                            readOnly = !uiState.switch
                        ) {
                            roomer = it
                        }
                    }

                    TextFieldApp(
                        title = "Кадастр",
                        value = cadastre,
                        readOnly = !uiState.switch,
                        modifier = Modifier.padding(top = 22.dp)
                    ) {
                        cadastre = it
                    }
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
                text = "Сохранить",
                onClick = {
                    intent(
                        AboutConsumerIntent.UpdateDetails(
                            data?.consumer,
                            activeSelect,
                            name,
                            address,
                            area,
                            rooms,
                            roomer,
                            cadastre,
                            data?.type
                        )
                    )
                },
                enabled = name != (data?.consumerProfiles?.name
                    ?: "") || address != (data?.consumerAddress?.address ?: "")
                        || area != (data?.consumerObjects?.area
                    ?: "") || rooms != (data?.consumerObjects?.room
                    ?: "") || roomer != (data?.consumerObjects?.roomer ?: "")
                        || cadastre != (data?.consumerObjects?.cadastre
                    ?: "") || activeSelect != (if (data?.status == true) 0 else 1) && !uiState.loading,
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
            ToastError(text = it) {
                intent(AboutConsumerIntent.ToastHide)
            }
        }

        if (uiState.serverError) {
            ToastError(text = stringResource(Res.string.error_connection_server)) {
                intent(AboutConsumerIntent.ToastHide)
            }
        }

        if (uiState.success) {
            ToastApp { intent(AboutConsumerIntent.ToastHide) }
        }
    }
}

@Composable
fun Title(text: String) {
    Text(text = text, color = gray70Color, fontSize = 13.sp, fontFamily = lato_regular)
}

//@Preview
//@Composable
//private fun AboutConsumerPreview() {
//    AboutConsumerContent(ConsumerDetailsBody(status = true, type = ""), AboutConsumerUiState()) {}
////    AboutConsumerContent(switch = false, loading = false, data = null) {}
//}