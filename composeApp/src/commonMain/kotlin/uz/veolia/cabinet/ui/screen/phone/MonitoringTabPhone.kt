package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.check_circle
import kotlinproject.composeapp.generated.resources.danger_circle
import kotlinproject.composeapp.generated.resources.ic_person
import kotlinproject.composeapp.generated.resources.ic_reload
import kotlinproject.composeapp.generated.resources.ic_search
import kotlinproject.composeapp.generated.resources.ic_setting
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.remote.response.MonitoringItem
import uz.veolia.cabinet.data.remote.response.PayService
import uz.veolia.cabinet.presenter.phone.MonitoringTabPhonePresenter
import uz.veolia.cabinet.ui.companent.BoxWithSwipeRefresh
import uz.veolia.cabinet.ui.companent.ButtonIcon
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.ImageApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.companent.ToastError
import uz.veolia.cabinet.ui.intent.MonitoringTabIntent
import uz.veolia.cabinet.ui.screen.dialog.LogOutDialogPhone
import uz.veolia.cabinet.ui.screen.dialog.MonitoringPhoneBottomSheet
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.errorColor
import uz.veolia.cabinet.ui.theme.gray15Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.primaryColor
import uz.veolia.cabinet.ui.theme.successColor
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.ui.uistate.MonitoringTabUiState
import uz.veolia.cabinet.util.extension.isToday
import uz.veolia.cabinet.util.extension.toCurrencyFormat

@Composable
fun MonitoringTabPhone(presenter: MonitoringTabPhonePresenter) {
    MonitoringTabPhoneContent(
        monitoring = presenter.monitoring.collectAsLazyPagingItems(),
        uiState = presenter.uiState.subscribeAsState().value,
        intent = presenter::onEventDispatcher
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonitoringTabPhoneContent(
    uiState: MonitoringTabUiState,
    monitoring: LazyPagingItems<MonitoringItem>,
    intent: (MonitoringTabIntent) -> Unit
) {
    var showProfileDialog by remember { mutableStateOf(false) }
    var filterDialog by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 9.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column {
            var isFocus by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .padding(bottom = 9.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(gray15Color, RoundedCornerShape(25.dp))
                    .padding(5.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                ButtonIcon(
                    idRes = Res.drawable.ic_person,
                    onClick = {
                        showProfileDialog = true
                    })
                BasicTextField(
                    value = uiState.search,
                    onValueChange = { intent(MonitoringTabIntent.OnSearchChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 60.dp, end = 42.dp)
                        .onFocusEvent {
                            isFocus = it.hasFocus
                        }
                        .background(Color.Transparent),
                    decorationBox = @Composable { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = uiState.search,
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = true,
                            trailingIcon = {
                                if (uiState.search.isNotEmpty()) {
                                    IconButton(onClick = {
                                        intent(
                                            MonitoringTabIntent.OnSearchChange(
                                                ""
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            visualTransformation = VisualTransformation.None,
                            interactionSource = remember { MutableInteractionSource() },
                            placeholder = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconApp(resource = Res.drawable.ic_search)
                                    TextApp(
                                        text = "Поиск",
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                cursorColor = primaryColor,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                        )
                    })
                ButtonIcon(
                    idRes = Res.drawable.ic_setting,
                    Modifier.align(Alignment.CenterEnd),
                    onClick = { filterDialog = true },
                    count = (if (uiState.filterRegion != null) 1 else 0) + (if (uiState.serviceId != null) 1 else 0) + (if (uiState.startDate.isToday() && uiState.endDate.isToday()) 0 else 1))
                LaunchedEffect(uiState.search) {
                    if (uiState.search.length == 1)
                        intent(MonitoringTabIntent.Search)
                    else {
                        delay(1000)
                        intent(MonitoringTabIntent.Search)
                    }
                }
            }

            BoxWithSwipeRefresh(
                onSwipe = { intent(MonitoringTabIntent.MonitoringReload) },
                isRefreshing = false
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 10.dp, top = 11.dp)
                ) {
                    items(monitoring.itemCount) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    whiteColor,
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 15.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextApp(
                                    text = "Л/С: ${monitoring[it]?.consumer ?: ""}",
                                    color = appDark,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                TextApp(
                                    text = monitoring[it]?.amountValue?.let {
                                        it.substring(0, it.length - 2).toCurrencyFormat()
                                    } ?: "",
                                    maxLines = 1,
                                    fontFamily = lato_bold,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 13.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.Top) {
                                TextApp(text = "Тип оплаты:", color = gray70Color)
                                Spacer(
                                    modifier = Modifier
                                        .padding(bottom = 7.dp)
                                        .align(Alignment.Bottom)
                                        .weight(1f)
                                        .background(gray70Color)
                                )
                                TextApp(
                                    text = when (monitoring[it]?.settlementCode) {
                                        "01" -> "Основной долг"
                                        "02" -> "Судебный гос. пошлина и почта расходы"
                                        "03" -> "Пеня"
                                        else -> ""
                                    },
                                    modifier = Modifier.padding(start = 10.dp),
                                    textAlign = TextAlign.End,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(
                                modifier = Modifier.padding(top = 4.dp, bottom = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextApp(
                                    text = "Дата/Состояние:",
                                    color = gray70Color,
                                    fontSize = 12.sp
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(bottom = 7.dp)
                                        .background(gray70Color)
                                )
                                TextApp(
                                    text = monitoring[it]?.createdAt ?: "",
                                    maxLines = 1,
                                    fontSize = 12.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                                VerticalDivider(
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 5.dp)
                                        .width(1.dp)
                                        .height(14.dp)
                                )
                                monitoring[it]?.payService?.status?.let {
                                    IconApp(
                                        resource = if (it) Res.drawable.check_circle else Res.drawable.danger_circle,
                                        tint = if (it) successColor else errorColor
                                    )
                                }
                            }
                        }
                    }
                    item {
                        if (monitoring.loadState.append is LoadState.Loading) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                            ) {
                                CircularProgressIndicatorApp()
                            }
                        }

                        if (monitoring.loadState.append is LoadState.Error || monitoring.loadState.refresh is LoadState.Error && monitoring.itemCount != 0) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton(onClick = { intent(MonitoringTabIntent.MonitoringReload) }) {
                                    ImageApp(id = Res.drawable.ic_reload)
                                }
                                TextApp(
                                    text = "Повторить попытку",
                                    Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (monitoring.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicatorApp()
        }

        if (monitoring.loadState.refresh is LoadState.Error) {
            (monitoring.loadState.refresh as LoadState.Error).apply {
                if (error.message == "Unauthorized") {
                    LaunchedEffect(Unit) {
                        delay(1000)
                        intent(MonitoringTabIntent.LogOut)
                    }
                } else if (error.message != null) {
                    intent(MonitoringTabIntent.Clear)
                    ToastError(text = (monitoring.loadState.refresh as LoadState.Error).error.message!!) {
                        intent(MonitoringTabIntent.ToastHide)
                    }
                }
            }
        }

        if (showProfileDialog) {
            LogOutDialogPhone(name = uiState.name ?: "", onCancel = {
                showProfileDialog = false
            }) {
                showProfileDialog = false
                intent(MonitoringTabIntent.LogOut)
            }
        }

        if (filterDialog) {
            MonitoringPhoneBottomSheet(
                regions = uiState.region,
                selected = uiState.filterRegion,
                service = uiState.serviceId,
                isMustSelect = uiState.isMustSelect,
                startDate = uiState.startDate,
                endDate = uiState.endDate,
                onCancel = { filterDialog = false }) { region, service, startDate, endDate ->
                filterDialog = false
                intent(MonitoringTabIntent.Filter(region, service, startDate, endDate))
            }
        }
    }
}

@Preview
@Composable
private fun MonitoringTabPhonePreview() {
    MonitoringTabPhoneContent(
        monitoring = flowOf(
            PagingData.from(
                listOf(
                    MonitoringItem(
                        1,
                        "4242434",
                        consumer = "234234",
                        settlementCode = "01",
                        paymentCode = "1",
                        amountValue = "23423442",
                        payService = PayService(
                            status = true,
                            createdAt = "2024-08-23T10:47:23.966Z"
                        )
                    ),
                    MonitoringItem(
                        2,
                        "4242434",
                        consumer = "234234",
                        settlementCode = "02",
                        paymentCode = "1",
                        amountValue = "23423442",
                        payService = PayService(
                            status = false,
                            createdAt = "2024-08-23T10:47:23.966Z"
                        )
                    )
                )
            )
        ).collectAsLazyPagingItems(),
        uiState = MonitoringTabUiState(),
    ) {}
}