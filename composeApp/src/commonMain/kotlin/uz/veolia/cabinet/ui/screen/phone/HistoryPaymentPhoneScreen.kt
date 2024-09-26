package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.click
import kotlinproject.composeapp.generated.resources.ic_back
import kotlinproject.composeapp.generated.resources.ic_usd_circle
import kotlinproject.composeapp.generated.resources.munis
import kotlinproject.composeapp.generated.resources.payme
import kotlinproject.composeapp.generated.resources.paynet
import kotlinproject.composeapp.generated.resources.upay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.model.Payment
import uz.veolia.cabinet.data.remote.response.PaymentItem
import uz.veolia.cabinet.data.remote.response.PaymentService
import uz.veolia.cabinet.presenter.phone.HistoryPaymentPhonePresenter
import uz.veolia.cabinet.ui.companent.DashedDivider
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.intent.HistoryPaymentIntent
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.errorColor
import uz.veolia.cabinet.ui.theme.gray50Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.successColor
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.ui.uistate.HistoryPaymentUiState
import uz.veolia.cabinet.util.extension.time
import uz.veolia.cabinet.util.extension.toCurrencyFormat

@Composable
fun HistoryPaymentPhoneScreen(presenter: HistoryPaymentPhonePresenter) {
    HistoryPaymentContent(
        presenter.uiState.subscribeAsState().value,
        presenter.payment.collectAsLazyPagingItems(),
        presenter::onEventDispatcher
    )
}

@Composable
fun HistoryPaymentContent(
    uiState: HistoryPaymentUiState,
    list: LazyPagingItems<PaymentItem>,
    intent: (HistoryPaymentIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .background(backgroundColor)
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
                    onClick = { intent(HistoryPaymentIntent.Back) },
                    Modifier.padding(horizontal = 4.dp)
                ) {
                    IconApp(resource = Res.drawable.ic_back, tint = appDark)
                }
                Column {
                    TextApp(
                        text = "История оплат",
                        fontSize = 22.sp,
                        fontFamily = lato_bold,
                        lineHeight = 26.4.sp
                    )
                    TextApp(
                        text = uiState.name,
                        color = gray50Color,
                        fontSize = 13.sp,
                        lineHeight = 15.6.sp,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                if (list.itemSnapshotList.items.isNotEmpty())
                    itemsIndexed(
                        list.itemSnapshotList,
                        key = { _, d -> d?.id ?: "" }) { index, data ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    whiteColor,
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 20.dp)
                        ) {
                            TextApp(text = "№ ${(index + 1)}", color = appDark, fontSize = 15.sp)
                            Row(
                                modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                            ) {
                                TextApp(
                                    text = "Сумма (сум)",
                                    color = gray70Color,
                                )
                                DashedDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .padding(bottom = 7.dp)
                                        .align(Alignment.Bottom)
                                        .weight(1f)
                                        .background(gray70Color)
                                )
                                TextApp(
                                    text = data?.amountValue?.let { it.substring(0,it.length-2).toCurrencyFormat() } ?: "не найден",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                            Row(
                                modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                            ) {
                                TextApp(
                                    text = "Оплачено через",
                                    color = gray70Color,
                                )
                                DashedDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .padding(bottom = 7.dp)
                                        .align(Alignment.Bottom)
                                        .weight(1f)
                                        .background(gray70Color)
                                )
                                val imageId : DrawableResource? = when (data?.paymentService?.serviceId) {
                                    Payment.PAYME.id -> Res.drawable.payme
                                    Payment.PAYNET.id -> Res.drawable.paynet
                                    Payment.CLICK.id -> Res.drawable.click
                                    Payment.MUNIS.id -> Res.drawable.munis
                                    Payment.UPAY.id -> Res.drawable.upay
                                    else -> null
                                }
                                if (imageId != null) {
                                    Image(
                                        modifier = Modifier.width(64.dp),
                                        painter = painterResource(imageId),
                                        contentDescription = null
                                    )
                                } else {
                                    TextApp(
                                        text = data?.paymentService?.name ?: "",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                            ) {
                                TextApp(
                                    text = "Дата оплаты",
                                    color = gray70Color,
                                )
                                DashedDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .padding(bottom = 7.dp)
                                        .align(Alignment.Bottom)
                                        .weight(1f)
                                        .background(gray70Color)
                                )
                                TextApp(
                                    text = data?.updatedAt?.time() ?: data?.createdAt?.time()
                                    ?: "не найден",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(
                                modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                            ) {
                                TextApp(
                                    text = "Статус платежа",
                                    color = gray70Color,
                                )
                                DashedDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .padding(bottom = 7.dp)
                                        .align(Alignment.Bottom)
                                        .weight(1f)
                                        .background(gray70Color)
                                )
                                TextApp(
//                                text = when (data?.paymentState) {
//                                    "1" -> "Отменен"
//                                    "2" -> "Оплачен"
//                                    else -> "не найдено"
//                                },
                                    maxLines = 1,
                                    color = if (data?.paymentService?.status == true) successColor else errorColor,
                                    overflow = TextOverflow.Ellipsis,
                                    text = if (data?.paymentService?.status == true) "Оплачен" else "Отменен"
                                )
                            }
                        }
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .height(50.dp)
//                            .background(Color.White, RoundedCornerShape(7.5.dp)),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Spacer(modifier = Modifier.padding(start = 16.dp))
//                        TextApp(
//                            text = (index + 1).toString(),
//                            fontSize = 13.sp, modifier = Modifier
//                                .weight(0.116f)
//                                .padding(end = 5.dp),
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                        TextApp(
//                            text = data?.amountValue?.toCurrencyFormat() ?: "",
//                            fontSize = 13.sp,
//                            modifier = Modifier
//                                .weight(0.223f)
//                                .padding(end = 5.dp)
//                        )
//                        TextApp(
//                            text = data?.paymentService?.name ?: "",
//                            fontSize = 13.sp,
//                            modifier = Modifier
//                                .weight(0.222f)
//                                .padding(end = 5.dp),
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                        TextApp(
//                            text = data?.updatedAt?.time()
//                                ?: data?.createdAt?.time() ?: "",
//                            Modifier
//                                .weight(0.222f)
//                                .padding(end = 5.dp),
//                            fontSize = 13.sp,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                        TextApp(
////                        text = when (data?.paymentState) {
////                            "1" -> "Отменен"
////                            "2" -> "Оплачен"
////                            else -> "не найдено"
////                        },
//                            modifier = Modifier
//                                .weight(0.217f)
//                                .padding(end = 5.dp),
//                            fontSize = 13.sp,
//                            maxLines = 1,
//                            color = if (data?.paymentService?.status == true) successColor else errorColor,
//                            overflow = TextOverflow.Ellipsis,
//                            text = if (data?.paymentService?.status == true) "Оплачен" else "Отменен"
//                        )
//
//                        Spacer(modifier = Modifier.padding(start = 16.dp))
//                    }
                    }
            }
        }

        if (list.loadState.refresh !is LoadState.Loading && list.itemCount == 0)
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconApp(
                    resource = Res.drawable.ic_usd_circle,
                    modifier = Modifier
                        .padding(bottom = 11.dp)
                        .size(26.88.dp),
                    tint = gray70Color
                )
                TextApp(text = "История оплат отсутствует")
            }


        if (list.loadState.refresh is LoadState.Error) {
            (list.loadState.refresh as LoadState.Error).apply {
                if (error.message == "Unauthorized") {
                    LaunchedEffect(Unit) {
                        delay(1000)
                        intent(HistoryPaymentIntent.LogOut)
                    }
                } else {
//                    var message by remember { mutableStateOf<String?>(null) }
//                    LaunchedEffect(key1 = list.loadState.refresh) {
//                        if (list.loadState.refresh is LoadState.Error)
//                            message = error.message.toString()
//                    }
//                    if (!message.isNullOrEmpty())
//                        ToastError(text = message.toString()) { message = null }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HistoryPaymentPreview() {
    HistoryPaymentContent(
        HistoryPaymentUiState("fsfds"),
        flowOf(
            PagingData.from(
                listOf(
                    PaymentItem(id = 1, paymentService = PaymentService(id = 15)),
                    PaymentItem(id = 2)
                )
            )
        ).collectAsLazyPagingItems()
    ) {}
}