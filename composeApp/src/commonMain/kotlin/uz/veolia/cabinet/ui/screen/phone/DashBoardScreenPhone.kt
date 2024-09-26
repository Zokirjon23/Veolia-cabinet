package uz.veolia.cabinet.ui.screen.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.Scaffold
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.access_page_denied
import kotlinproject.composeapp.generated.resources.ic_consumers
import kotlinproject.composeapp.generated.resources.ic_payment_monitoring
import org.jetbrains.compose.resources.stringResource
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.presenter.phone.DashBoardPhonePresenter
import uz.veolia.cabinet.ui.companent.FabApp
import uz.veolia.cabinet.ui.companent.IconApp
import uz.veolia.cabinet.ui.companent.TextApp
import uz.veolia.cabinet.ui.intent.DashBoardIntent
import uz.veolia.cabinet.ui.theme.appDark
import uz.veolia.cabinet.ui.theme.backgroundColor
import uz.veolia.cabinet.ui.theme.primaryColor
import uz.veolia.cabinet.ui.uistate.DashBoardUiState

@Composable
fun DashBoardScreenPhone(presenter: DashBoardPhonePresenter) {
//    val systemUiController = rememberSystemUiController()
//    SideEffect {
//        systemUiController.isSystemBarsVisible = true
//        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
//    }
    val uiState = presenter.uiState.collectAsState().value
    DashBoardContentPhone(
        presenter.stack,
        uiState = uiState,
        presenter::onEventDispatch
    )
}

@Composable
private fun DashBoardContentPhone(
    stack: Value<ChildStack<*, DashBoardPhonePresenter.Child>>,
    uiState: DashBoardUiState,
    intent: (DashBoardIntent) -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            androidx.compose.material3.BottomAppBar(
                containerColor = Color.White,
                modifier = Modifier.shadow(12.dp),
            ) {
                if (RoleManager.hasConsumerAccess())
                    NavigationBarItem(
                        icon = {
                            IconApp(
                                resource = Res.drawable.ic_consumers,
                                contentDescription = "Потребители",
                                tint = if (uiState.bottomTab == 0) primaryColor else appDark
                            )
                        },
                        label = {
                            TextApp(
                                "Потребители",
                                color = if (uiState.bottomTab == 0) primaryColor else appDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(400)
                            )
                        },
                        selected = uiState.bottomTab == 0,
                        onClick = { intent(DashBoardIntent.BottomTabSelected(0)) },
                        enabled = true,
                        colors = NavigationBarItemDefaults
                            .colors(
                                selectedIconColor = primaryColor,
                                indicatorColor = Color.White
                            )
                    )
                if (RoleManager.hasMonitoringPay())
                    NavigationBarItem(
                        icon = {
                            IconApp(
                                resource = Res.drawable.ic_payment_monitoring,
                                contentDescription = "Мониторинг оплат",
                                tint = if (uiState.bottomTab == 1) primaryColor else appDark
                            )
                        },
                        label = {
                            TextApp(
                                "Мониторинг оплат",
                                color = if (uiState.bottomTab == 1) primaryColor else appDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(400)
                            )
                        },
                        selected = uiState.bottomTab == 1,
                        onClick = { intent(DashBoardIntent.BottomTabSelected(1)) },
                        enabled = true,
                        colors = NavigationBarItemDefaults
                            .colors(
                                selectedIconColor = primaryColor,
                                indicatorColor = Color.White
                            )
                    )
            }
        }, floatingActionButton = {
            if (stack.value.active.instance is DashBoardPhonePresenter.Child.ConsumerPhoneTabChild && RoleManager.canConsumerAdd()) {
                FabApp(onClick = { intent(DashBoardIntent.AddConsumer) })
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .background(backgroundColor)
        ) {
            Children(
                stack = stack,
                modifier = Modifier.fillMaxSize(),
                animation = stackAnimation(fade()),
            ) {
                when (val instance = it.instance) {
                    is DashBoardPhonePresenter.Child.ConsumerPhoneTabChild -> ConsumersTabPhone(
                        instance.presenter
                    )

                    is DashBoardPhonePresenter.Child.MonitoringPhoneTabChild -> MonitoringTabPhone(
                        instance.presenter
                    )

                    DashBoardPhonePresenter.Child.NoAccess -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            TextApp(
                                text = stringResource(Res.string.access_page_denied),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
