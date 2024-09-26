package uz.veolia.cabinet.ui.screen.dialog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveDatePicker
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.click
import kotlinproject.composeapp.generated.resources.ic_accrual
import kotlinproject.composeapp.generated.resources.ic_back
import kotlinproject.composeapp.generated.resources.ic_close
import kotlinproject.composeapp.generated.resources.ic_diagram
import kotlinproject.composeapp.generated.resources.ic_key
import kotlinproject.composeapp.generated.resources.ic_log_out
import kotlinproject.composeapp.generated.resources.ic_next
import kotlinproject.composeapp.generated.resources.ic_person
import kotlinproject.composeapp.generated.resources.ic_person_circle
import kotlinproject.composeapp.generated.resources.ic_usd_circle
import kotlinproject.composeapp.generated.resources.munis
import kotlinproject.composeapp.generated.resources.payme
import kotlinproject.composeapp.generated.resources.paynet
import kotlinproject.composeapp.generated.resources.upay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.model.Payment
import uz.veolia.cabinet.data.remote.response.AccItem
import uz.veolia.cabinet.data.remote.response.CounterDetails
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.ui.companent.ButtonApp
import uz.veolia.cabinet.ui.companent.CancelIcon
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.ImageApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.theme.VeoliaTheme
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.gray20Color
import uz.veolia.cabinet.ui.theme.gray15Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.primaryColor
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.util.extension.getCurrentDayInMillis
import uz.veolia.cabinet.util.extension.getCurrentYear
import uz.veolia.cabinet.util.extension.getFirstDayOfCurrentMonthInMillis
import uz.veolia.cabinet.util.extension.getMiddleOfDayInMillis
import uz.veolia.cabinet.util.extension.getYesterdayInMillis
import uz.veolia.cabinet.util.extension.isFirstDayOfCurrentMonth
import uz.veolia.cabinet.util.extension.isToday
import uz.veolia.cabinet.util.extension.isYesterday
import uz.veolia.cabinet.util.extension.toCurrencyFormat
import uz.veolia.cabinet.util.extension.toFormattedDateString
import uz.veolia.cabinet.util.extension.toMonth

private val services = listOf(
    Pair(Payment.CLICK.id, Res.drawable.click),
    Pair(Payment.PAYME.id, Res.drawable.payme),
    Pair(Payment.PAYNET.id, Res.drawable.paynet),
    Pair(Payment.UPAY.id, Res.drawable.upay),
    Pair(Payment.MUNIS.id, Res.drawable.munis)
)

val tabs = listOf(
    Pair("О потребителе", Res.drawable.ic_person_circle),
    Pair("Данные от ЛК", Res.drawable.ic_key),
    Pair("История оплат", Res.drawable.ic_usd_circle),
    Pair("История начислений", Res.drawable.ic_accrual),
    Pair("История показаний", Res.drawable.ic_diagram)
)

val tabsAccrualLess = listOf(
    Pair("О потребителе", Res.drawable.ic_person_circle),
    Pair("Данные от ЛК", Res.drawable.ic_key),
    Pair("История оплат", Res.drawable.ic_usd_circle),
    Pair("История показаний", Res.drawable.ic_diagram)
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterPopup(
    regions: List<Int>,
    selected: Int?,
    initStatus: Boolean?,
    initType: String?,
    onCancel: () -> Unit,
    mustSelect: Boolean = false,
    result: (String?, Boolean?, Int?) -> Unit
) {
    var type by remember { mutableStateOf(initType) }
    var status by remember { mutableStateOf(initStatus) }
    var select by remember { mutableStateOf(selected) }
    BasicAlertDialog(onDismissRequest = onCancel) {
        Column {
            CancelIcon(
                onClick = onCancel,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 10.dp),
                size = 40.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            gray15Color,
                            RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
                        )
                        .padding(vertical = 15.5.dp, horizontal = 16.dp)

                ) {
                    TextApp(text = "Фильтр", fontSize = 16.sp, fontFamily = lato_bold)
//                    TextApp(
//                        text = "Сбросить",
//                        color = if ((type != initType || status != initStatus || select != selected) || (selected != null || initStatus != null || initType != null)) primaryColor else primaryColor.copy(
//                            0.5f
//                        ),
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .clickable(
//                                (type != initType || status != initStatus || select != selected) || (selected != null || initStatus != null || initType != null)
//                            ) {
//                                select = null
//                                type = null
//                                status = null
//                            })
                }
                TextApp(
                    text = "Тип потребителя",
                    fontFamily = lato_bold,
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    if (RoleManager.canPhysical() && RoleManager.canJuridical()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .height(40.dp)
                                .width(74.dp)
                                .background(
                                    if (type == null) primaryColor else whiteColor,
                                )
                                .clickable {
                                    type = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            TextApp(
                                text = "Все",
                                color = if (type == null) whiteColor else appDark
                            )
                        }
                    }
                    FilterItem(
                        text = "Физ. лица",
                        isSelected = type == "1",
                        clickAble = RoleManager.canJuridical() && RoleManager.canPhysical(),
                        onSelected = { type = if (type == "1") null else "1" })
                    FilterItem(
                        text = "Юр. лица",
                        isSelected = type == "3",
                        clickAble = RoleManager.canJuridical() && RoleManager.canPhysical(),
                        onSelected = { type = if (type == "3") null else "3" })
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(1.dp)
                        .background(gray15Color)
                )
                TextApp(
                    text = "Статус",
                    fontFamily = lato_bold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(40.dp)
                            .width(74.dp)
                            .background(
                                if (status == null) primaryColor else whiteColor,
                            )
                            .clickable {
                                status = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        TextApp(
                            text = "Все",
                            color = if (status == null) whiteColor else appDark
                        )
                    }

                    FilterItem(
                        text = "Активный",
                        status == true,
                        onSelected = { status = if (status == true) null else true })
                    FilterItem(
                        text = "Неактивный",
                        status == false,
                        onSelected = { status = if (status == false) null else false })
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(1.dp)
                        .background(gray15Color)
                )
                if (regions.isNotEmpty()) {
                    TextApp(
                        text = "Регион",
                        fontFamily = lato_bold,
                        modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        if (!mustSelect) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(40.dp)
                                    .width(74.dp)
                                    .background(
                                        if (select == null) primaryColor else whiteColor,
                                    )
                                    .clickable {
                                        select = null
                                    }
                                    .padding(horizontal = 16.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextApp(
                                    text = "Все",
                                    color = if (select == null) whiteColor else appDark
                                )
                            }
                        }
                        regions.forEach {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(40.dp)
                                    .background(
                                        if (select == it) primaryColor else whiteColor,
                                    )
                                    .clickable(if (mustSelect) select != it else true) {
                                        select = if (it == select) null else it
                                    }
                                    .padding(horizontal = 16.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextApp(
                                    text = "RU ${if (it < 10) "0" else ""}${it}",
                                    color = if (select == it) whiteColor else appDark
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(1.dp)
                            .background(gray15Color)
                    )
                }

                ButtonApp(
                    text = "Применить",
                    enabled = type != initType || status != initStatus || select != selected,
                    onClick = { result(type, status, select) },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPopupPhone(
    regions: List<Int>,
    selected: Int?,
    initStatus: Boolean?,
    initType: String?,
    onCancel: () -> Unit,
    mustSelect: Boolean = false,
    result: (String?, Boolean?, Int?) -> Unit
) {
    var type by remember { mutableStateOf(initType) }
    var status by remember { mutableStateOf(initStatus) }
    var select by remember { mutableStateOf(selected) }
    ModalBottomSheet(
        windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Bottom),
        containerColor = backgroundColor,
        onDismissRequest = onCancel,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .background(
                    backgroundColor,
                )
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(gray15Color)
                    .padding(vertical = 15.5.dp, horizontal = 16.dp)

            ) {
                TextApp(text = "Фильтр", fontSize = 16.sp, fontFamily = lato_bold)
//                TextApp(
//                    text = "Сбросить",
//                    color = if ((type != initType || status != initStatus || select != selected) || (selected != null || initStatus != null || initType != null)) primaryColor else primaryColor.copy(
//                        0.5f
//                    ),
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .clickable(
//                            (type != initType || status != initStatus || select != selected) || (selected != null || initStatus != null || initType != null)
//                        ) {
//                            select = null
//                            type = null
//                            status = null
//                        })
            }
            TextApp(
                text = "Тип потребителя",
                fontFamily = lato_bold,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (RoleManager.canPhysical() && RoleManager.canJuridical()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(40.dp)
                            .width(76.dp)
                            .background(
                                if (type == null) primaryColor else whiteColor,
                            )
                            .clickable {
                                type = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        TextApp(
                            text = "Все",
                            color = if (type == null) whiteColor else appDark
                        )
                    }
                }
                FilterItem(
                    text = "Физ. лица",
                    isSelected = type == "1",
                    clickAble = RoleManager.canJuridical() && RoleManager.canPhysical(),
                    onSelected = { type = if (type == "1") null else "1" })
                FilterItem(
                    text = "Юр. лица",
                    isSelected = type == "3",
                    clickAble = RoleManager.canJuridical() && RoleManager.canPhysical(),
                    onSelected = { type = if (type == "3") null else "3" })
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .background(gray15Color)
            )
            TextApp(
                text = "Статус",
                fontFamily = lato_bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)

            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(40.dp)
                        .width(76.dp)
                        .background(
                            if (status == null) primaryColor else whiteColor,
                        )
                        .clickable {
                            status = null
                        },
                    contentAlignment = Alignment.Center
                ) {
                    TextApp(
                        text = "Все",
                        color = if (status == null) whiteColor else appDark
                    )
                }
                FilterItem(
                    text = "Активный",
                    status == true,
                    onSelected = { status = if (status == true) null else true })
                FilterItem(
                    text = "Неактивный",
                    status == false,
                    onSelected = { status = if (status == false) null else false })
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .background(gray15Color)
            )
            if (regions.isNotEmpty()) {
                TextApp(
                    text = "Регион",
                    fontFamily = lato_bold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    try {
                        if (!mustSelect) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .height(40.dp)
                                        .background(
                                            if (select == null) primaryColor else whiteColor,
                                        )
                                        .clickable {
                                            select = null
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextApp(
                                        text = "Все",
                                        color = if (select == null) whiteColor else appDark
                                    )
                                }
                            }
                        }
                        items(regions) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(40.dp)
                                    .background(
                                        if (select == it) primaryColor else whiteColor,
                                    )
                                    .clickable(if (mustSelect) select != it else true) {
                                        select = if (it == select) null else it
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                TextApp(
                                    text = "RU ${if (it < 10) "0" else ""}${it}",
                                    color = if (select == it) whiteColor else appDark
                                )
                            }
                        }
                    } catch (e: Exception) {
//                        Log.e("DDDD", e.message.toString())
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(1.dp)
                        .background(gray15Color)
                )
            }

            ButtonApp(
                text = "Применить",
                enabled = type != initType || status != initStatus || select != selected,
                onClick = { result(type, status, select) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PhoneFilter() {
    FilterPopupPhone(
        regions = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
        selected = null,
        initStatus = null,
        initType = null,
        onCancel = { /*TODO*/ }) { _, _, _ ->
    }
}

@Preview
@Composable
private fun MonitoringTabPreview() {
    MonitoringFilterPopup(regions = listOf(1, 2, 3, 4, 5, 6),
        selected = 1,
        service = null,
        onCancel = { /*TODO*/ }) { _, _, _, _ ->
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MonitoringFilterPopup(
    regions: List<Int>,
    selected: Int?,
    service: String?,
    startDate: Long? = getCurrentDayInMillis(),
    endDate: Long? = getCurrentDayInMillis(),
    isMustSelect: Boolean = false,
    onCancel: () -> Unit,
    result: (Int?, String?, Long, Long) -> Unit
) {
    var select by remember { mutableStateOf(selected) }
    var selectService by remember { mutableStateOf(service) }
    var startDateLong by remember { mutableStateOf(startDate) }
    var endDateLong by remember { mutableStateOf(endDate) }
    var showTimePicker by remember { mutableStateOf(false) }

    BasicAlertDialog(onDismissRequest = onCancel) {
        Column {
            CancelIcon(
                onClick = onCancel,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 10.dp),
                size = 40.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            gray15Color,
                            RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
                        )
                        .padding(vertical = 15.5.dp, horizontal = 16.dp)

                ) {
                    TextApp(text = "Фильтр", fontSize = 16.sp, fontFamily = lato_bold)
//                    TextApp(
//                        text = "Сбросить",
//                        color = if (select != selected || selected != null) primaryColor else primaryColor.copy(
//                            0.5f
//                        ),
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .clickable(select != selected || selected != null) { select = null })
                }


                TextApp(
                    text = "Платежная система",
                    fontFamily = lato_bold,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 10.dp)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(40.dp)
                            .width(74.dp)
                            .background(
                                if (selectService == null) primaryColor else whiteColor,
                            )
                            .clickable {
                                selectService = null
                            }
                            .padding(horizontal = 16.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextApp(
                            text = "Все",
                            color = if (selectService == null) whiteColor else appDark
                        )
                    }
                    services.forEach {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .height(40.dp)
                                .width(74.dp)
                                .background(
                                    if (selectService == it.first) Color(0xFFF8E4E5) else whiteColor,
                                )
                                .clickable {
                                    selectService =
                                        if (it.first == selectService) null else it.first
                                }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ImageApp(id = it.second, modifier = Modifier.height(35.dp))
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(1.dp)
                        .background(gray15Color)
                )
                TextApp(
                    text = "Период",
                    fontFamily = lato_bold,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 10.dp)
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
//                text = when (it) {
//                0 -> "Сегодня"
//                1 -> "Вчера"
//                2 -> "Месяц"
//                3 -> "Период"
//                else -> ""
//            },
                    val isToday =
                        startDateLong == endDateLong && startDateLong?.isToday() == true
                    val isYesterday =
                        startDateLong == endDateLong && startDateLong?.isYesterday() == true
                    val isMonth =
                        startDateLong?.isFirstDayOfCurrentMonth() == true && endDateLong?.isToday() == true
                    PeriodItem(
                        text = "Сегодня",
                        enable = isToday
                    ) {
                        startDateLong = getCurrentDayInMillis()
                        endDateLong = getCurrentDayInMillis()
                    }
                    PeriodItem(
                        text = "Вчера",
                        enable = isYesterday
                    ) {
                        startDateLong = getYesterdayInMillis()
                        endDateLong = getYesterdayInMillis()
                    }
                    PeriodItem(
                        text = "Месяц",
                        enable = isMonth
                    ) {
                        startDateLong = getFirstDayOfCurrentMonthInMillis()
                        endDateLong = getCurrentDayInMillis()
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(40.dp)
                            .background(
                                if (!isMonth && !isToday && !isYesterday) primaryColor else whiteColor,
                            )
                            .clickable {
                                showTimePicker = true
                            }
                            .padding(vertical = 5.dp)
                            .padding(
                                start = 16.dp,
                                end = if (isMonth || isToday || isYesterday) 16.dp else 2.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextApp(
                            text = if (isMonth || isToday || isYesterday) "Другой период" else "${startDateLong?.toFormattedDateString()} - ${endDateLong?.toFormattedDateString()}",
                            color = if (isMonth || isToday || isYesterday) appDark else whiteColor
                        )
                        if (!isMonth && !isToday && !isYesterday)
                            IconButton(onClick = {
                                startDateLong = getCurrentDayInMillis()
                                endDateLong = getCurrentDayInMillis()
                            }) {
                                IconApp(
                                    resource = Res.drawable.ic_close,
                                    tint = whiteColor
                                )
                            }
                    }
                }

                if (regions.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(1.dp)
                            .background(gray15Color)
                    )
                    TextApp(
                        text = "Регион",
                        fontFamily = lato_bold,
                        modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        if (!isMustSelect) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(40.dp)
                                    .width(74.dp)
                                    .background(
                                        if (select == null) primaryColor else whiteColor,
                                    )
                                    .clickable {
                                        select = null
                                    }
                                    .padding(horizontal = 16.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextApp(
                                    text = "Все",
                                    color = if (select == null) whiteColor else appDark
                                )
                            }
                        }
                        regions.forEach {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(40.dp)
                                    .width(74.dp)
                                    .background(
                                        if (select == it) primaryColor else whiteColor,
                                    )
                                    .clickable(if (isMustSelect) select != it else true) {
                                        select = if (it == select) null else it
                                    }
                                    .padding(horizontal = 16.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextApp(
                                    text = "RU ${if (it < 10) "0" else ""}${it}",
                                    color = if (select == it) whiteColor else appDark
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(1.dp)
                            .background(gray15Color)
                    )
                }


                ButtonApp(
                    text = "Применить",
                    enabled = select != selected || service != selectService || (startDate != startDateLong || endDateLong != endDate),
                    onClick = {
                        result(
                            select,
                            selectService,
                            startDateLong ?: 0,
                            endDateLong ?: 0
                        )
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
        if (showTimePicker) {
            CalendarScreenContent(
                startDate = startDateLong,
                endDate = endDateLong,
                onDismiss = { showTimePicker = false }) { start, end ->
                startDateLong = start
                endDateLong = end
            }
        }
    }
}

@Preview
@Composable
private fun RegionPhone() {
    MonitoringPhoneBottomSheet(
        regions = listOf(1, 2, 3, 4, 5, 6),
        selected = 1,
        service = null,
        onCancel = { /*TODO*/ }) { _, _, _, _ ->

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MonitoringPhoneBottomSheet(
    regions: List<Int>,
    selected: Int?,
    service: String?,
    startDate: Long? = getCurrentDayInMillis(),
    endDate: Long? = getCurrentDayInMillis(),
    isMustSelect: Boolean = false,
    onCancel: () -> Unit,
    result: (Int?, String?, Long, Long) -> Unit
) {
    var selectRegion by remember { mutableStateOf(selected) }
    var selectService by remember { mutableStateOf(service) }
    var startDateLong by remember { mutableStateOf(startDate) }
    var endDateLong by remember { mutableStateOf(endDate) }
    ModalBottomSheet(
        windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Bottom),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = backgroundColor,
        onDismissRequest = onCancel
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        gray15Color,
                    )
                    .padding(vertical = 15.5.dp, horizontal = 16.dp)
            ) {
                TextApp(text = "Фильтр", fontSize = 16.sp, fontFamily = lato_bold)
//                TextApp(
//                    text = "Сбросить",
//                    color = if (select != selected || selected != null) primaryColor else primaryColor.copy(
//                        0.5f
//                    ),
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .clickable(select != selected || selected != null) { select = null })
            }

            TextApp(
                text = "Платежная система",
                fontFamily = lato_bold,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 10.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                try {
                    item {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .height(40.dp)
                                .background(
                                    if (selectService == null) primaryColor else whiteColor,
                                )
                                .clickable {
                                    selectService = null
                                }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextApp(
                                text = "Все",
                                color = if (selectService == null) whiteColor else appDark
                            )
                        }
                    }
                    items(services) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .height(40.dp)
                                .background(
                                    if (selectService == it.first) Color(0xFFF8E4E5) else whiteColor,
                                )
                                .clickable {
                                    selectService =
                                        if (it.first == selectService) null else it.first
                                }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ImageApp(id = it.second, modifier = Modifier.height(35.dp))
                        }
                    }
                } catch (e: Exception) {
//                    Log.e("DDDD", e.message.toString())
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(1.dp)
                    .background(gray15Color)
            )
            var showTimePicker by remember { mutableStateOf(false) }
            TextApp(
                text = "Период",
                fontFamily = lato_bold,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 10.dp)
            )
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
//                text = when (it) {
//                0 -> "Сегодня"
//                1 -> "Вчера"
//                2 -> "Месяц"
//                3 -> "Период"
//                else -> ""
//            },
                val isToday = startDateLong == endDateLong && startDateLong?.isToday() == true
                val isYesterday =
                    startDateLong == endDateLong && startDateLong?.isYesterday() == true
                val isMonth =
                    startDateLong?.isFirstDayOfCurrentMonth() == true && endDateLong?.isToday() == true
                PeriodItem(
                    text = "Сегодня",
                    enable = isToday
                ) {
                    startDateLong = getCurrentDayInMillis()
                    endDateLong = getCurrentDayInMillis()
                }
                PeriodItem(
                    text = "Вчера",
                    enable = isYesterday
                ) {
                    startDateLong = getYesterdayInMillis()
                    endDateLong = getYesterdayInMillis()
                }
                PeriodItem(
                    text = "Месяц",
                    enable = isMonth
                ) {
                    startDateLong = getFirstDayOfCurrentMonthInMillis()
                    endDateLong = getCurrentDayInMillis()
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(40.dp)
                        .background(
                            if (!isMonth && !isToday && !isYesterday) primaryColor else whiteColor,
                        )
                        .clickable {
                            showTimePicker = true
                        }
                        .padding(vertical = 5.dp)
                        .padding(
                            start = 16.dp,
                            end = if (isMonth || isToday || isYesterday) 16.dp else 2.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextApp(
                        text = if (isMonth || isToday || isYesterday) "Другой период" else "${startDateLong?.toFormattedDateString()} - ${endDateLong?.toFormattedDateString()}",
                        color = if (isMonth || isToday || isYesterday) appDark else whiteColor
                    )
                    if (!isMonth && !isToday && !isYesterday)
                        IconButton(onClick = {
                            startDateLong = getCurrentDayInMillis()
                            endDateLong = getCurrentDayInMillis()
                        }) {
                            IconApp(
                                resource = Res.drawable.ic_close,
                                tint = whiteColor
                            )
                        }
                }
            }
            if (showTimePicker)
                RangeDatePicker(
                    startDateLong,
                    endDateLong,
                    onDismiss = { showTimePicker = false }) { start, end ->
                    startDateLong = start
                    endDateLong = end
                }

            if (regions.isNotEmpty()) {
                TextApp(
                    text = "Регион",
                    fontFamily = lato_bold,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 10.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    try {
                        if (!isMustSelect) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .height(40.dp)
                                        .background(
                                            if (selectRegion == null) primaryColor else whiteColor,
                                        )
                                        .clickable {
                                            selectRegion = null
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextApp(
                                        text = "Все",
                                        color = if (selectRegion == null) whiteColor else appDark
                                    )
                                }
                            }
                        }
                        items(regions) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(40.dp)
                                    .background(
                                        if (selectRegion == it) primaryColor else whiteColor,
                                    )
                                    .clickable(if (isMustSelect) selectRegion != it else true) {
                                        selectRegion = if (it == selectRegion) null else it
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                TextApp(
                                    text = "RU ${if (it < 10) "0" else ""}${it}",
                                    color = if (selectRegion == it) whiteColor else appDark
                                )
                            }
                        }
                    } catch (e: Exception) {
//                        Log.e("DDDD", e.message.toString())
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(1.dp)
                    .background(gray15Color)
            )

            ButtonApp(
                text = "Применить",
                enabled = selectRegion != selected || service != selectService || (startDate != startDateLong || endDateLong != endDate),
                onClick = {
                    result(
                        selectRegion,
                        selectService,
                        startDateLong ?: 0,
                        endDateLong ?: 0
                    )
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun FilterItem(
    text: String,
    isSelected: Boolean,
    clickAble: Boolean = true,
    onSelected: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .height(40.dp)
            .background(if (isSelected) primaryColor else whiteColor)
            .clickable(clickAble) { onSelected(!isSelected) }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextApp(
            text = text,
            color = if (isSelected) whiteColor else appDark
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogOutDialog(name: String, onCancel: () -> Unit, onLogOut: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = onCancel
    ) {
        Column {
            CancelIcon(
                onClick = onCancel,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 10.dp),
                size = 40.dp
            )
            Column(
                modifier = Modifier
                    .background(
                        backgroundColor,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(gray15Color, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconApp(resource = Res.drawable.ic_person)
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        TextApp(text = name)
                        TextApp(text = "Сотрудник", fontSize = 12.sp, color = appDark.copy(0.7f))
                    }
                }
                Button(
                    onClick = onLogOut,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(50.dp)
                        .width(327.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(gray15Color)
                ) {
                    IconApp(Res.drawable.ic_log_out, tint = primaryColor)
                    TextApp(
                        text = "Выйти",
                        Modifier.padding(start = 11.dp),
                        color = primaryColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogOutPhone() {
    LogOutDialogPhone(name = "rewr", onCancel = { /*TODO*/ }) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogOutDialogPhone(name: String, onCancel: () -> Unit, onLogOut: () -> Unit) {
    ModalBottomSheet(
        containerColor = backgroundColor,
        onDismissRequest = onCancel,
        windowInsets = BottomSheetDefaults.windowInsets.only(
            WindowInsetsSides.Bottom
        )
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(gray15Color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconApp(Res.drawable.ic_person)
                }
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    TextApp(text = name)
                    TextApp(text = "Сотрудник", fontSize = 12.sp, color = appDark.copy(0.7f))
                }
            }
            Button(
                onClick = onLogOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(primaryColor)
            ) {
                IconApp(Res.drawable.ic_log_out, tint = whiteColor)
                TextApp(
                    text = "Выйти",
                    Modifier.padding(start = 11.dp),
                    color = whiteColor
                )
            }

            Button(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                TextApp(
                    text = "Отмена",
                    Modifier.padding(start = 11.dp),
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}

@Preview
@Composable
private fun LogOut() {
    FilterPopupPhone(
        regions = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
        selected = 1,
        initStatus = null,
        initType = null,
        onCancel = { /*TODO*/ }) { _, _, _ ->

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountersImageDialog(onCancel: () -> Unit, list: List<CounterDetails?>) {
    Dialog(onDismissRequest = onCancel) {
//        val state = rememberPagerState { list.size }
//        val coroutineScope = rememberCoroutineScope()
//        Column {
//            CancelIcon(
//                onClick = onCancel,
//                modifier = Modifier
//                    .align(Alignment.End)
//                    .padding(bottom = 10.dp), size = 40.dp
//            )
//            Box(
//                modifier = Modifier
//                    .size(549.dp)
//                    .clip(RoundedCornerShape(10.dp))
//            ) {
//                HorizontalPager(
//                    state = state, modifier = Modifier.fillMaxSize()
//                ) {
//                    LoadImageAsync(
//                        modifier = Modifier.fillMaxSize(),
//                        url = list[state.currentPage]?.fileLink?.let { if (it.startsWith("http")) it else "https://$it" }
//                            ?: ""
//                    )
//                }
//                Row(
//                    Modifier
//                        .align(Alignment.BottomCenter)
//                        .fillMaxWidth()
//                        .background(Color(0xFF332E38).copy(0.75f))
//                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                ) {
//                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                        TextApp(
//                            text = "Счетчик ${state.currentPage + 1}",
//                            fontWeight = FontWeight(600),
//                            color = whiteColor
//                        )
//                        TextApp(
//                            text = "${list[state.currentPage]?.meter?.ifEmpty { "0" } ?: 0} м3",
//                            color = whiteColor
//                        )
//                    }
//
//                    Box(
//                        Modifier
//                            .padding(horizontal = 20.dp)
//                            .defaultMinSize(minHeight = 36.dp)
//                            .width(1.dp)
//                            .background(whiteColor.copy(0.2f))
//                    )
//                    TextApp(
//                        text = list[state.currentPage]?.fileName ?: "",
//                        color = whiteColor
//                    )
//                }
//                if (state.currentPage != 0)
//                    Box(
//                        Modifier
//                            .align(Alignment.CenterStart)
//                            .padding(start = 16.dp)
//                            .size(40.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFF47404F).copy(0.75f))
//                            .clickable {
//                                coroutineScope.launch {
//                                    state.animateScrollToPage(state.currentPage - 1)
//                                }
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        IconApp(id = R.drawable.ic_arrow_left, tint = whiteColor)
//                    }
//                if (state.currentPage != state.pageCount - 1)
//                    Box(
//                        Modifier
//                            .align(Alignment.CenterEnd)
//                            .padding(end = 16.dp)
//                            .size(40.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFF47404F).copy(0.75f))
//                            .clickable {
//                                coroutineScope.launch {
//                                    state.animateScrollToPage(state.currentPage + 1)
//                                }
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        IconApp(id = R.drawable.ic_arrow_right, tint = whiteColor)
//                    }
//            }
//        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountersImageDialogPhone(onCancel: () -> Unit, list: List<CounterDetails?>) {
    Dialog(onDismissRequest = onCancel) {
//        val state = rememberPagerState { list.size }
//        val coroutineScope = rememberCoroutineScope()
//        Box(
//            modifier = Modifier
//                .height(300.dp)
//                .clip(RoundedCornerShape(10.dp))
//        ) {
//            HorizontalPager(
//                state = state, modifier = Modifier.fillMaxSize()
//            ) {
//                LoadImageAsync(
//                    modifier = Modifier.fillMaxSize(),
//                    url = list[state.currentPage]?.fileLink?.let { if (it.startsWith("http")) it else "https://$it" }
//                        ?: ""
//                )
//            }
//            Row(
//                Modifier
//                    .align(Alignment.BottomCenter)
//                    .fillMaxWidth()
//                    .background(Color(0xFF332E38).copy(0.75f))
//                    .padding(horizontal = 16.dp, vertical = 12.dp)
//            ) {
//                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                    TextApp(
//                        text = "Счетчик ${state.currentPage + 1}",
//                        fontWeight = FontWeight(600),
//                        color = whiteColor
//                    )
//                    TextApp(
//                        text = "${list[state.currentPage]?.meter?.ifEmpty { "0" } ?: 0} м3",
//                        color = whiteColor
//                    )
//                }
//
//                Box(
//                    Modifier
//                        .padding(horizontal = 20.dp)
//                        .defaultMinSize(minHeight = 36.dp)
//                        .width(1.dp)
//                        .background(whiteColor.copy(0.2f))
//                )
//                TextApp(
//                    text = list[state.currentPage]?.fileName ?: "",
//                    color = whiteColor
//                )
//            }
//            if (state.currentPage != 0)
//                Box(
//                    Modifier
//                        .align(Alignment.CenterStart)
//                        .padding(start = 16.dp)
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFF47404F).copy(0.75f))
//                        .clickable {
//                            coroutineScope.launch {
//                                state.animateScrollToPage(state.currentPage - 1)
//                            }
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//                    IconApp(id = R.drawable.ic_arrow_left, tint = whiteColor)
//                }
//            if (state.currentPage != state.pageCount - 1)
//                Box(
//                    Modifier
//                        .align(Alignment.CenterEnd)
//                        .padding(end = 16.dp)
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFF47404F).copy(0.75f))
//                        .clickable {
//                            coroutineScope.launch {
//                                state.animateScrollToPage(state.currentPage + 1)
//                            }
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//                    IconApp(id = R.drawable.ic_arrow_right, tint = whiteColor)
//                }
//        }
    }
}

//@Composable
//fun DeleteDialog(onCancel: () -> Unit, onDelete: () -> Unit) {
//    Dialog(onDismissRequest = onCancel) {
//        Column {
//            CancelIcon(
//                onClick = onCancel,
//                modifier = Modifier
//                    .align(Alignment.End)
//                    .padding(bottom = 10.dp),
//                size = 40.dp
//            )
//
//            Column(
//                modifier = Modifier
//                    .background(
//                        backgroundColor,
//                        RoundedCornerShape(20.dp)
//                    )
//                    .padding(16.dp)
//            ) {
//                TextApp(
//                    text = "Вы уверены, что хотите удалить пользователя?",
//                    Modifier.padding(bottom = 16.dp)
//                )
//                Row(horizontalArrangement = Arrangement.spacedBy(23.dp)) {
//                    ButtonApp(
//                        text = "Отмена", onCancel, colors = ButtonDefaults.buttonColors(
//                            gray15Color
//                        ), modifier = Modifier.width(152.dp), textColor = appDark
//                    )
//                    ButtonApp(
//                        text = "Удалить",
//                        onClick = onDelete,
//                        modifier = Modifier.width(152.dp)
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YearDialog(onCancel: () -> Unit, year: Int, onSelected: (Int) -> Unit) {
    Dialog(onDismissRequest = onCancel) {
        Column {
            CancelIcon(
                onClick = onCancel,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 10.dp),
                size = 40.dp
            )

            Column(
                modifier = Modifier
                    .background(
                        backgroundColor,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                TextApp(
                    text = "Год",
                    Modifier.padding(bottom = 10.dp, start = 16.dp),
                    fontFamily = lato_bold
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    for (i in getCurrentYear() downTo 2003) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (year == i) primaryColor else whiteColor)
                                .clickable { onSelected(i) }
                                .padding(horizontal = 29.5.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextApp(
                                text = i.toString(),
                                color = if (year == i) whiteColor else appDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDialogPhone(onCancel: () -> Unit, year: Int, onSelected: (Int) -> Unit) {
    ModalBottomSheet(
        containerColor = backgroundColor,
        onDismissRequest = onCancel,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        val list = mutableListOf<Int>()
        for (i in getCurrentYear() downTo 2003) {
            list.add(i)
        }
        Column(
            modifier = Modifier
                .background(
                    backgroundColor,
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp)
                .padding(top = 5.dp, bottom = 20.dp)
        ) {
            TextApp(
                text = "Год",
                Modifier.padding(bottom = 10.dp),
                fontFamily = lato_bold
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(list) { i ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (year == i) primaryColor else whiteColor)
                            .clickable { onSelected(i) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextApp(
                            text = i.toString(),
                            color = if (year == i) whiteColor else appDark
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccrualBottomSheet(onCancel: () -> Unit, list: List<AccItem>) {
    ModalBottomSheet(
        windowInsets = BottomSheetDefaults.windowInsets.only(
            WindowInsetsSides.Bottom
        ),
        containerColor = backgroundColor,
        onDismissRequest = onCancel,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        TextApp(
            text = if (list.isNotEmpty()) list[0].period.toMonth() else
                "",
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 15.sp,
            fontFamily = lato_bold
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(0.42f)
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    TextApp(
                        text = "№",
                        modifier = Modifier.weight(0.2582f),
                        fontFamily = lato_bold,
                    )
                    TextApp(
                        text = "Тип",
                        modifier = Modifier.weight(0.735f),
                        fontFamily = lato_bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                TextApp(
                    text = "Дебит",
                    modifier = Modifier.weight(0.29f),
                    fontFamily = lato_bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                TextApp(
                    text = "Кредит",
                    modifier = Modifier.weight(0.29f),
                    fontFamily = lato_bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                Modifier.verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                list.forEachIndexed { index, accItem ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(whiteColor, RoundedCornerShape(7.5.dp))
                            .padding(vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(0.42f)
                                .padding(start = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            TextApp(
                                text = "${index + 1}",
                                modifier = Modifier.weight(0.2582f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            TextApp(
                                text = when (accItem.type) {
                                    "01" -> "Основной долг"
                                    "02" -> "Судебный гос. пошлина и почта расходы"
                                    "03" -> "Пеня"
                                    else -> ""
                                },
                                modifier = Modifier.weight(0.735f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        TextApp(
                            text = accItem.debet?.let {
                                if (it.length > 4) {
                                    it.substring(0, it.length - 3)
                                        .toCurrencyFormat() + it.substring(
                                        it.length - 3,
                                        it.length
                                    )
                                } else {
                                    it
                                }
                            } ?: "",
                            modifier = Modifier.weight(0.29f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        TextApp(
                            text = accItem.credit?.let {
                                if (it.length > 4) {
                                    it.substring(0, it.length - 3)
                                        .toCurrencyFormat() + it.substring(
                                        it.length - 3,
                                        it.length
                                    )
                                } else {
                                    it
                                }
                            } ?: "",
                            modifier = Modifier.weight(0.29f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Accrual() {
    AccrualBottomSheet(
        {},
        list = listOf(AccItem("wer", "dwer", "12331", "ewrwer", "werwer", "01"), AccItem())
    )
}

@Preview
@Composable
private fun YearPhone() {
    YearDialogPhone(onCancel = { /*TODO*/ }, year = 2024) {

    }
}

@Preview
@Composable
fun DeleteDialogPreview() {
    Box(
        Modifier
            .fillMaxSize()
            .background(whiteColor)
    ) {
        YearDialog(onCancel = { /*TODO*/ }, year = 2024) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeDatePicker(
    startDate: Long?,
    endDate: Long?,
    onDismiss: () -> Unit,
    onDone: (Long, Long) -> Unit
) {
    val today =
        remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate?.let { getMiddleOfDayInMillis(it) },
        initialSelectedEndDateMillis = endDate?.let { getMiddleOfDayInMillis(it) },
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                return selectedDate <= today
            }
        }
    )
    ModalBottomSheet(
        windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Bottom),
        onDismissRequest = onDismiss,
        dragHandle = null,
        shape = RoundedCornerShape(0.dp),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Box(modifier = Modifier.systemBarsPadding()) {
            RangeDatePickerDialog(
                state = state,
                modifier = Modifier.padding(bottom = 85.dp),
                onBack = onDismiss
            )
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                HorizontalDivider(color = gray15Color)
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(26.dp)
                ) {
                    ButtonApp(
                        textColor = appDark,
                        text = "Очистить",
                        onClick = { state.setSelection(null, null) },
                        enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null,
                        modifier = Modifier.weight(0.5f),
                        colors = ButtonDefaults.buttonColors(gray15Color)
                    )
                    ButtonApp(
                        enabled = if (state.selectedStartDateMillis == null && state.selectedEndDateMillis == null) true else if (state.selectedStartDateMillis != null && state.selectedEndDateMillis != null) true else false,
                        text = "Применить",
                        onClick = {
                            onDone(
                                state.selectedStartDateMillis ?: getCurrentDayInMillis(),
                                state.selectedEndDateMillis ?: getCurrentDayInMillis()
                            )
                            onDismiss()
                        },
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CalendarViewPreview() {
    CalendarScreenContent(
        null, null,
        onDismiss = { /*TODO*/ }) { _, _ ->

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenContent(
    startDate: Long?,
    endDate: Long?,
    onDismiss: () -> Unit,
    onDone: (Long, Long) -> Unit
) {
    val today =
        remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate?.let { getMiddleOfDayInMillis(it) },
        initialSelectedEndDateMillis = endDate?.let { getMiddleOfDayInMillis(it) },
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                return selectedDate <= today
            }
        })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        RangeDatePickerDialog(
            state = state,
            modifier = Modifier.padding(bottom = 85.dp),
            onBack = onDismiss
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            HorizontalDivider(color = gray15Color)
            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(26.dp)
            ) {
                ButtonApp(
                    textColor = appDark,
                    text = "Очистить",
                    onClick = { state.setSelection(null, null) },
                    enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null,
                    modifier = Modifier.weight(0.5f),
                    colors = ButtonDefaults.buttonColors(gray15Color)
                )
                ButtonApp(
                    enabled = if (state.selectedStartDateMillis == null && state.selectedEndDateMillis == null) true else if (state.selectedStartDateMillis != null && state.selectedEndDateMillis != null) true else false,
                    text = "Применить",
                    onClick = {
                        onDone(
                            state.selectedStartDateMillis ?: getCurrentDayInMillis(),
                            state.selectedEndDateMillis ?: getCurrentDayInMillis()
                        )
                        onDismiss()
                    },
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
    }
}

fun getFormattedDate(timeInMillis: Long): String {
    val russianMonths = listOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    val instant = Instant.fromEpochMilliseconds(timeInMillis)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val day = localDate.dayOfMonth
    val month = russianMonths[localDate.monthNumber - 1] // Month numbers are 1-based
    return "$day $month"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeDatePickerDialog(
    modifier: Modifier = Modifier,
    state: DateRangePickerState,
    onBack: () -> Unit = {}
) {
    Surface(color = backgroundColor) {
        DateRangePicker(
            state,
            modifier = modifier,
            headline = {
                Column(
                    modifier = Modifier
                        .background(whiteColor)
                        .padding(bottom = 12.dp)
                ) {

                    Row(
                        Modifier
                            .padding(top = 6.dp)
                            .fillMaxWidth()
                            .height(64.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            Modifier.padding(horizontal = 4.dp)
                        ) {
                            IconApp(Res.drawable.ic_back, tint = appDark)
                        }
                        TextApp(
                            text = "Выберите период",
                            fontSize = 22.sp,
                            fontFamily = lato_bold,
                            lineHeight = 26.4.sp
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .weight(0.5f)
                                .height(40.dp)
                                .border(1.dp, gray20Color, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .background(backgroundColor),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            val chosen = state.selectedStartDateMillis != null
                            (if (chosen) state.selectedStartDateMillis?.let {
                                getFormattedDate(it)
                            } else "от")?.let {
                                TextApp(
                                    text = it,
                                    Modifier.padding(start = 14.dp),
                                    color = if (chosen) appDark else appDark.copy(0.5f)
                                )
                            }
                        }
                        IconApp(Res.drawable.ic_next)
                        Box(
                            Modifier
                                .weight(0.5f)
                                .height(40.dp)
                                .border(1.dp, gray20Color, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .background(backgroundColor),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            val chosen = state.selectedEndDateMillis != null
                            (if (state.selectedEndDateMillis != null) state.selectedEndDateMillis?.let {
                                getFormattedDate(it)
                            } else "до")?.let {
                                TextApp(
                                    text = it,
                                    Modifier.padding(start = 14.dp),
                                    color = if (chosen) appDark else appDark.copy(0.5f)
                                )
                            }
                        }
                    }
                }
            },
            title = null,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = backgroundColor,
                titleContentColor = appDark,
                headlineContentColor = appDark,
                weekdayContentColor = appDark,
                todayDateBorderColor = primaryColor,
                todayContentColor = primaryColor,
                dayInSelectionRangeContainerColor = Color(0xFFFEE9EA),
                dayInSelectionRangeContentColor = appDark,
                selectedDayContainerColor = primaryColor
            )
        )
    }
}


@Preview
@Composable
fun GreetingPreview16() {
    VeoliaTheme {
        RangeDatePicker(null, null, {}) { _, _ -> }
    }
}

@Preview
@Composable
private fun FilterPopupPreview() {
    Box(Modifier.fillMaxSize()) {
        CountersImageDialogPhone({}, listOf(CounterDetails(null, "fsd", "fsdsdf", null, "", null)))
    }
}

@Composable
fun PeriodItem(text: String, enable: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .height(40.dp)
            .background(
                if (enable) primaryColor else whiteColor,
            )
            .clickable {
                onClick()
            }
            .padding(vertical = 5.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextApp(
//            text = when (it) {
//                0 -> "Сегодня"
//                1 -> "Вчера"
//                2 -> "Месяц"
//                3 -> "Период"
//                else -> ""
//            },
            text = text,
            color = if (enable) whiteColor else appDark
        )
    }
}