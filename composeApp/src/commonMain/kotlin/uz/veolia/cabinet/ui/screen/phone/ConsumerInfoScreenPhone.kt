package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import uz.veolia.cabinet.ui.companent.CircularProgressIndicatorApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.error_connection_server
import kotlinproject.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.veolia.cabinet.data.remote.response.ConsumerDetailsBody
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.companent.ToastError
import uz.veolia.cabinet.ui.intent.ConsumerInfoIntent
import uz.veolia.cabinet.ui.screen.dialog.tabs
import uz.veolia.cabinet.ui.screen.dialog.tabsAccrualLess
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.errorColor
import uz.veolia.cabinet.ui.theme.gray50Color
import uz.veolia.cabinet.ui.theme.lato_bold
import uz.veolia.cabinet.ui.theme.successColor
import uz.veolia.cabinet.ui.theme.whiteColor
import uz.veolia.cabinet.ui.uistate.ConsumerInfoUiState
import uz.veolia.cabinet.util.extension.toDividedFormat

@Composable
fun ConsumerInfoScreenPhone(presenter: CommonPresenter<ConsumerInfoIntent, ConsumerInfoUiState>) {
//    val systemUiController = rememberSystemUiController()
//    SideEffect {
//        systemUiController.isSystemBarsVisible = true
//        systemUiController.setSystemBarsColor(Color.White, darkIcons = true)
//    }
    ConsumerInfoContentPhone(
        presenter.uiState.subscribeAsState().value,
        presenter::onEventDispatcher,
    )
}

@Composable
private fun ConsumerInfoContentPhone(
    uiState: ConsumerInfoUiState,
    intent: (ConsumerInfoIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(bottom = 16.dp)
        ) {
            Column(
                Modifier
                    .padding(bottom = 20.dp)
                    .background(Color.White)
            ) {
                Row(
                    Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { intent(ConsumerInfoIntent.Back) },
                        Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconApp(resource = Res.drawable.ic_back, tint = appDark)
                    }
                    Column {
                        TextApp(
                            text = "Потребитель",
                            fontSize = 22.sp,
                            fontFamily = lato_bold,
                            lineHeight = 26.4.sp,
                        )
                        TextApp(
                            text = uiState.consumerDetailsBody?.consumerProfiles?.name ?: "",
                            color = gray50Color,
                            fontSize = 13.sp,
                            lineHeight = 15.6.sp,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
                val balance =
                    (uiState.consumerDetailsBody?.consumerStates?.balance?.toFloatOrNull()
                        ?: 0f)
                Row(
                    modifier = Modifier.padding(bottom = 4.dp, start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(10.dp)
                            .background(
                                color = when (uiState.consumerDetailsBody?.status) {
                                    true -> successColor
                                    else -> errorColor
                                },
                                CircleShape
                            )
                    )
                    TextApp(
                        text = "Л/С: ${uiState.consumer ?: ""}",
                        color = appDark,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextApp(
                        text = uiState.consumerDetailsBody?.consumerStates?.balance.toDividedFormat(),
                        lineHeight = 15.6.sp
                    )
                    TextApp(
                        text = " на ${uiState.consumerDetailsBody?.consumerStates?.balanceDate}",
                        color = gray50Color,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
            if(!uiState.loading){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(if (RoleManager.canAccrualView() && uiState.consumerDetailsBody?.type == "1") tabs else tabsAccrualLess) { index, it ->
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(whiteColor)
                                .clickable {
                                    intent(ConsumerInfoIntent.OnItemClicked(it.first))
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                                .height(100.dp), verticalArrangement = Arrangement.Center
                        ) {
                            IconApp(resource = it.second, Modifier.padding(bottom = 20.dp))
                            TextApp(text = it.first)
                        }
                    }
                }
            }
        }


        if (uiState.loading)
            CircularProgressIndicatorApp()

        if (uiState.serverError) {
            ToastError(text = stringResource(Res.string.error_connection_server)) {
                intent(ConsumerInfoIntent.ToastHide)
            }
        }

        uiState.message?.let {
            ToastError(text = it) {
                intent(ConsumerInfoIntent.ToastHide)
            }
        }
    }
}

@Preview
@Composable
private fun ConsumerInfoScreenPhonePreview() {
    ConsumerInfoContentPhone(
        ConsumerInfoUiState(
            consumerDetailsBody = ConsumerDetailsBody(
                status = true,
                type = ""
            )
        )
    ) {}
}