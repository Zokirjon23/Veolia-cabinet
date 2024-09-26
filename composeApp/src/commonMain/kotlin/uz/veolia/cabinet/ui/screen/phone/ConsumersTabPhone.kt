package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinproject.composeapp.generated.resources.ic_person
import kotlinproject.composeapp.generated.resources.ic_reload
import kotlinproject.composeapp.generated.resources.ic_search
import kotlinproject.composeapp.generated.resources.ic_setting
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.remote.response.ConsumerItem
import uz.veolia.cabinet.data.remote.response.ConsumerProfiles
import uz.veolia.cabinet.data.remote.response.ConsumerStates
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.presenter.phone.ConsumerTabPhonePresenter
import uz.veolia.cabinet.ui.companent.BoxWithSwipeRefresh
import uz.veolia.cabinet.ui.companent.ButtonIcon
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.ImageApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.companent.ToastError
import uz.veolia.cabinet.ui.intent.ConsumerTabIntent
import uz.veolia.cabinet.ui.screen.dialog.FilterPopupPhone
import uz.veolia.cabinet.ui.screen.dialog.LogOutDialogPhone
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.errorColor
import uz.veolia.cabinet.ui.theme.gray15Color
import uz.veolia.cabinet.ui.theme.gray70Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.primaryColor
import uz.veolia.cabinet.ui.theme.successColor
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.ui.uistate.ConsumerTabUiState
import uz.veolia.cabinet.util.extension.toDividedFormat

@Composable
fun ConsumersTabPhone(presenter: ConsumerTabPhonePresenter) {
    ConsumerTabPhoneContent(
        presenter.uiState.subscribeAsState().value,
        presenter.consumer.collectAsLazyPagingItems(),
        presenter::onEventDispatcher
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConsumerTabPhoneContent(
    uiState: ConsumerTabUiState,
    consumers: LazyPagingItems<ConsumerItem>,
    intent: (ConsumerTabIntent) -> Unit
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
//          var search by remember { mutableStateOf(uiState.search) }
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
                    onClick = { showProfileDialog = true })
                BasicTextField(
                    value = uiState.search,
                    onValueChange = { intent(ConsumerTabIntent.OnSearchChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 60.dp, end = 42.dp)
                        .onFocusEvent {
                            isFocus = it.hasFocus
                        }
                        .background(Color.Transparent),
                    decorationBox = @Composable { innerTextField ->
                        TextFieldDefaults.TextFieldDecorationBox(
                            value = uiState.search,
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = true,
                            trailingIcon = {
                                if (uiState.search.isNotEmpty()) {
                                    IconButton(onClick = { intent(ConsumerTabIntent.OnSearchChange("")) }) {
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
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = primaryColor,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
//                                unfocusedContainerColor = Color.Transparent,
//                                focusedContainerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                        )
                    })
                ButtonIcon(
                    idRes = Res.drawable.ic_setting,
                    Modifier.align(Alignment.CenterEnd),
                    onClick = { filterDialog = true },
                    count = (if (uiState.filterRegion != null) 1 else 0) + (if (uiState.filterStatus != null) 1 else 0) + (if (uiState.filterPersonType != null) 1 else 0)
                )
            }
            LaunchedEffect(uiState.search) {
                if (uiState.search.length == 1)
                    intent(ConsumerTabIntent.Search)
                else {
                    delay(1000)
                    intent(ConsumerTabIntent.Search)
                }
            }
            BoxWithSwipeRefresh(
                onSwipe = { intent(ConsumerTabIntent.ConsumerReload) },
                isRefreshing = false
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 10.dp, top = 11.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (consumers.loadState.refresh !is LoadState.Loading) {
                        items(
                            consumers.itemCount,
                            key = { consumers[it]?.consumer ?: "" }
                        ) { pos ->
                            val it = consumers[pos]
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable((RoleManager.canViewDetailsJuridical() || RoleManager.canViewDetailsPhysical())) {
                                        intent(
                                            ConsumerTabIntent.ItemClicked(
                                                it?.consumer ?: ""
                                            )
                                        )
                                    }
                                    .background(whiteColor)
                                    .padding(horizontal = 16.dp, vertical = 15.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 5.dp)
                                            .size(10.dp)
                                            .background(
                                                if (it?.status == true) successColor else errorColor,
                                                CircleShape
                                            )
                                    )
                                    TextApp(
                                        text = "Л/С: ${it?.consumer ?: ""}",
                                        color = appDark,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    TextApp(
                                        text = it?.consumerStates?.balance?.toDividedFormat() ?: "",
                                        maxLines = 1,
                                        fontFamily = lato_bold,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 13.sp
                                    )
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    TextApp(text = "Потребитель:", color = gray70Color)
                                    Spacer(
                                        modifier = Modifier
                                            .padding(bottom = 7.dp)
                                            .align(Alignment.Bottom)
                                            .weight(1f)
                                            .background(gray70Color)
                                    )
                                    TextApp(
                                        text = it?.consumerProfiles?.name ?: "не найден",
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
                                        text = "Адрес:",
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
                                        text = it?.consumerAddress?.address ?: "не найден",
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                    item {
                        if (consumers.loadState.append is LoadState.Loading) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                            ) {
                                CircularProgressIndicatorApp()
                            }
                        }

                        if (consumers.loadState.append is LoadState.Error || consumers.loadState.refresh is LoadState.Error && consumers.itemCount != 0) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton(onClick = { intent(ConsumerTabIntent.ConsumerReload) }) {
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

//        FabApp(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(bottom = 25.dp), onClick = { intent(ConsumerTabIntent.AddClicked) })

        if (showProfileDialog) {
            LogOutDialogPhone(name = uiState.name ?: "", onCancel = {
                showProfileDialog = false
            }) {
                showProfileDialog = false
                intent(ConsumerTabIntent.LogOut)
            }
        }

        if (consumers.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicatorApp()
        }
        if (consumers.loadState.refresh is LoadState.Error) {
            (consumers.loadState.refresh as LoadState.Error).apply {
                if (error.message == "Unauthorized") {
                    LaunchedEffect(Unit) {
                        delay(1000)
                        intent(ConsumerTabIntent.LogOut)
                    }
                } else if (error.message != null) {
                    intent(ConsumerTabIntent.Clear)
                    ToastError(text = (consumers.loadState.refresh as LoadState.Error).error.message!!) {
                        intent(ConsumerTabIntent.ToastHide)
                    }
                }
            }
        }

        if (filterDialog) {
            FilterPopupPhone(
                regions = uiState.region,
                selected = uiState.filterRegion,
                initStatus = uiState.filterStatus,
                initType = uiState.filterPersonType,
                mustSelect = uiState.isMustSelect,
                onCancel = { filterDialog = false }) { type, status, select ->
                filterDialog = false
                intent(ConsumerTabIntent.Filter(type, status, select))
            }
        }
    }
}

@Preview
@Composable
private fun ConsumerTabPhonePreview() {
    ConsumerTabPhoneContent(
        uiState = ConsumerTabUiState(), flowOf(
            PagingData.from(
                listOf(
                    ConsumerItem(
                        consumer = "88888888888",
                        consumerStates = ConsumerStates(balance = "1000000000"),
                        consumerProfiles = ConsumerProfiles(name = "sdfdsfdfsdfsdfsdffdsfds")
                    ),
                    ConsumerItem(
                        consumerStates = ConsumerStates(balance = "10000"),
                        consumerProfiles = ConsumerProfiles(name = "fdfsdfsdfsdffdsfds")
                    ),
                )
            )
        ).collectAsLazyPagingItems()
    ) {}
}