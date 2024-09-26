package uz.veolia.cabinet.ui.screen.phone.accrual

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_accrual
import kotlinproject.composeapp.generated.resources.ic_arrow_left
import kotlinproject.composeapp.generated.resources.ic_arrow_right
import kotlinproject.composeapp.generated.resources.ic_back
import kotlinproject.composeapp.generated.resources.ic_calendar
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.remote.response.AccItem
import uz.veolia.cabinet.data.remote.response.GetAccrualItem
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import uz.veolia.cabinet.ui.companent.DashedDivider
import uz.veolia.cabinet.ui.companent.FabApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.intent.AccrualIntent
import uz.veolia.cabinet.ui.screen.dialog.AccrualBottomSheet
import uz.veolia.cabinet.ui.screen.dialog.YearDialogPhone
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.gray50Color
import uz.veolia.cabinet.ui.theme.gray15Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.ui.uistate.AccrualUiState
import uz.veolia.cabinet.util.extension.getCurrentYear
import uz.veolia.cabinet.util.extension.toDividedFormat
import uz.veolia.cabinet.util.extension.toMonth

@Composable
fun AccrualPhoneScreen(
    presenter: CommonPresenter<AccrualIntent, AccrualUiState>,
) {
    AccrualPhoneContent(
        presenter.uiState.subscribeAsState().value,
        presenter::onEventDispatcher,
    )
}

@Composable
private fun AccrualPhoneContent(
    uiState: AccrualUiState,
    intent: (AccrualIntent) -> Unit,
) {
    Box(
        Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        var datePicker by remember { mutableStateOf(false) }
        var accItem by remember { mutableStateOf<List<AccItem>?>(null) }
        Column {
            Row(
                Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { intent(AccrualIntent.Back) },
                    Modifier.padding(horizontal = 4.dp)
                ) {
                    IconApp(resource = Res.drawable.ic_back, tint = appDark)
                }
                Column {
                    TextApp(
                        text = "История начислений",
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

            Row {
                Row(
                    Modifier
                        .padding(bottom = 10.dp, start = 16.dp)
                        .clip(RoundedCornerShape(4.17.dp))
                        .clickable { datePicker = true }
                        .background(whiteColor)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconApp(resource = Res.drawable.ic_calendar, tint = Color(0xFF151515))
                    TextApp(text = uiState.year)
                }
                Box(
                    Modifier
                        .padding(start = 20.dp, end = 10.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (uiState.year == "2003") gray15Color.copy(0.5f) else gray15Color)
                        .clickable(uiState.year != "2003") {
                            intent(AccrualIntent.AccrualDateChange(uiState.year.toInt() - 1))
                        }, contentAlignment = Alignment.Center
                ) {
                    IconApp(
                        resource = Res.drawable.ic_arrow_left,
                        tint = Color(0xFF151515).copy(if (uiState.year == "2003") 0.5f else 1f)
                    )
                }
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (uiState.year != getCurrentYear().toString()) gray15Color else gray15Color.copy(
                                0.5f
                            )
                        )
                        .clickable(
                            uiState.year != getCurrentYear().toString()
                        ) {
                            intent(AccrualIntent.AccrualDateChange(uiState.year.toInt() + 1))
                        }, contentAlignment = Alignment.Center
                ) {
                    IconApp(
                        resource = Res.drawable.ic_arrow_right, tint = Color(0xFF151515).copy(
                            if (uiState.year != getCurrentYear().toString()
                            ) 1f else 0.5f
                        )
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                itemsIndexed(uiState.list) { i, it ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(whiteColor)
                            .clickable { accItem = uiState.list[i].accList }
                            .padding(horizontal = 16.dp)
                            .padding(
                                bottom = 20.dp,
                                top = if (it.state == "CURRENT") 5.dp else 20.dp
                            ),
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            TextApp(text = "Период", color = gray70Color)
                            DashedDivider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .padding(bottom = 7.dp)
                                    .align(Alignment.Bottom)
                                    .weight(1f)
                                    .background(gray70Color)
                            )
                            Column {
                                if (it.state == "CURRENT")
                                    TextApp(
                                        text = "Текущий",
                                        color = Color(0xFFFFC400),
                                        fontSize = 12.sp
                                    )
                                TextApp(
//                                    text = it.period ?: "",
                                    text = it.period.toMonth(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Дебит",
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
                                text = it.debet?.let { it.toDividedFormat() } ?: "не найден",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Кредит",
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
                                text = it.credit?.let { it.toDividedFormat() } ?: "не найден",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Оплата",
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
                                text = it.payment?.let { it.toDividedFormat() } ?: "не найден",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
                        ) {
                            TextApp(
                                text = "Баланс",
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
                                text = it.balance?.let { it.toDividedFormat() } ?: "не найден",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        if (accItem != null) {
            AccrualBottomSheet(onCancel = { accItem = null }, list = accItem!!)
        }

        if (uiState.list.isEmpty() && !uiState.loading)
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconApp(
                    resource = Res.drawable.ic_accrual,
                    modifier = Modifier
                        .padding(bottom = 11.dp)
                        .size(26.88.dp),
                    tint = gray70Color
                )
                TextApp(text = "История начислений отсутствует")
            }

        if (uiState.loading) {
            CircularProgressIndicatorApp()
        }
        if (RoleManager.canAccrualAdd())
            FabApp(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                intent(AccrualIntent.OpenCreateAccrualScreen)
            }

        if (datePicker) {
            YearDialogPhone(onCancel = { datePicker = false }, year = uiState.year.toInt()) {
                intent(AccrualIntent.AccrualDateChange(it))
                datePicker = false
            }
        }
    }
}

@Preview
@Composable
private fun AccrualPreview() {
    AccrualPhoneContent(
        AccrualUiState(
            year = "2024",
            list = listOf(
                GetAccrualItem(
                    listOf(
                        AccItem("dsa", "ert", "ert", "ert", "ter", "01"),
                        AccItem("dsa", "ert", "ert", "ert", "ter", "01"),
                        AccItem("dsa", "ert", "ert", "ert", "ter", "01")
                    ),
                    "23423",
                    "4324234",
                    "5345345",
                    "435",
                    "543-534",
                    "CURRENT"
                ),
                GetAccrualItem(
                    listOf(
                        AccItem("dsa", "ert", "ert", "ert", "ter", "01"),
                        AccItem("dsa", "ert", "ert", "ert", "ter", "01"),
                        AccItem("dsa", "ert", "ert", "ert", "ter", "01")
                    ),
                    "23423",
                    "4324234",
                    "5345345",
                    "435",
                    "543-534",
                    null
                )
            ), false
        )
    ) {}
}