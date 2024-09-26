package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_back
import kotlinproject.composeapp.generated.resources.ic_diagram
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.remote.response.CounterItem
import uz.veolia.cabinet.presenter.phone.HistoryCounterPhonePresenter
import uz.veolia.cabinet.ui.companent.DashedDivider
import uz.veolia.cabinet.ui.companent.FabApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.intent.HistoryCounterIntent
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.gray50Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.util.extension.toDate

@Composable
fun HistoryCounterPhoneScreen(
    presenter: HistoryCounterPhonePresenter,
    name: String?,
) {
    HistoryCounterContent(
        presenter.meters.collectAsLazyPagingItems(),
        name,
        presenter::onEventDispatcher
    )
}

@Composable
private fun HistoryCounterContent(
    meters: LazyPagingItems<CounterItem>,
    name: String?,
    intent: (HistoryCounterIntent) -> Unit
) {
    val clickedItem = remember { mutableStateOf<CounterItem?>(null) }
    Box(
        Modifier
            .fillMaxSize()
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
                    onClick = { intent(HistoryCounterIntent.Back) },
                    Modifier.padding(horizontal = 4.dp)
                ) {
                    IconApp(resource = Res.drawable.ic_back, tint = appDark)
                }
                Column {
                    TextApp(
                        text = "История показаний",
                        fontSize = 22.sp,
                        fontFamily = lato_bold,
                        lineHeight = 26.4.sp
                    )
                    TextApp(
                        text = name ?: "",
                        color = gray50Color,
                        fontSize = 13.sp,
                        lineHeight = 15.6.sp,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(bottom = 10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(meters.itemCount) {
                    val data = meters[it]
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(
                                whiteColor,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable(data?.meterDetails?.isNotEmpty() == true) {
                                clickedItem.value = data
                            }
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        TextApp(text = "№ ${(it + 1)}", color = appDark, fontSize = 15.sp)
                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Дата показание",
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
                                text = data?.updatedAt?.toDate() ?: "не найден",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Показание",
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
                                text = "${data?.meter} м3",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Разница с предыдущим",
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
                                text = if (meters.itemCount - 1 == it) "0" else ((data?.meter?.toIntOrNull()
                                    ?: 0) - (meters[it + 1]?.meter?.toIntOrNull() ?: 0)).toString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Источник",
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
                                text = if (data?.system == "CABINET") "Кабинет" else data?.system
                                    ?: "не найден",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        if (clickedItem.value != null)
//            CountersImageDialogPhone(
//                onCancel = { clickedItem.value = null },
//                list = clickedItem.value!!.meterDetails!!
//            )

        FabApp(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            intent(
                HistoryCounterIntent.SendCounter(
                    if (meters.itemSnapshotList.isNotEmpty()) meters[0]?.meter else null, name
                )
            )
        }

        if (meters.loadState.refresh !is LoadState.Loading && meters.itemCount == 0)
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconApp(
                    resource = Res.drawable.ic_diagram,
                    modifier = Modifier
                        .padding(bottom = 11.dp)
                        .size(26.88.dp),
                    tint = gray70Color
                )
                TextApp(text = "История показаний отсутствует")
            }

        if (meters.loadState.refresh is LoadState.Error) {
            (meters.loadState.refresh as LoadState.Error).apply {
                if (error.message == "Unauthorized") {
                    LaunchedEffect(Unit) {
                        delay(1000)
                        intent(HistoryCounterIntent.LogOut)
                    }
                } else {
//                    var message by remember { mutableStateOf<String?>(null) }
//                    LaunchedEffect(key1 = meters.loadState.refresh) {
//                        if (meters.loadState.refresh is LoadState.Error)
//                            message = error.message.toString()
//                    }
//                    if (!message.isNullOrEmpty())
//                        ToastError(text = message.toString()) { message = null }
//                    ToastError(text = error.message ?: ""){  }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HistoryCounterPreview() {
    HistoryCounterContent(
        flowOf(PagingData.from(listOf(CounterItem()))).collectAsLazyPagingItems(),
        ""
    ) {}
}