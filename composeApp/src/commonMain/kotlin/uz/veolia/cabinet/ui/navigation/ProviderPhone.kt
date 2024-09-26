package uz.veolia.cabinet.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import uz.veolia.cabinet.presenter.phone.AboutConsumerPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.presenter.phone.ConsumerTabPhonePresenter
import uz.veolia.cabinet.presenter.phone.DashBoardPhonePresenter
import uz.veolia.cabinet.presenter.phone.HistoryCounterPhonePresenter
import uz.veolia.cabinet.presenter.phone.HistoryPaymentPhonePresenter
import uz.veolia.cabinet.presenter.phone.MonitoringTabPhonePresenter
import uz.veolia.cabinet.presenter.phone.SplashPhonePresenter
import uz.veolia.cabinet.presenter.phone.impl.AccrualPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.AddConsumerPhonePresenter
import uz.veolia.cabinet.presenter.phone.impl.ConsumerTabPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.CreateAccrualPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.DashBoardPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.HistoryCounterPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.HistoryPaymentPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.LoginPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.MonitoringTabPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.PersonalDataPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.SendCounterPhonePresenterImpl
import uz.veolia.cabinet.presenter.phone.impl.ConsumerInfoPhonePresenterImpl
import uz.veolia.cabinet.ui.intent.AboutConsumerIntent
import uz.veolia.cabinet.ui.intent.AccrualIntent
import uz.veolia.cabinet.ui.intent.AddConsumerIntent
import uz.veolia.cabinet.ui.intent.ConsumerInfoIntent
import uz.veolia.cabinet.ui.intent.CreateAccrualIntent
import uz.veolia.cabinet.ui.intent.LoginIntent
import uz.veolia.cabinet.ui.intent.PersonalDataIntent
import uz.veolia.cabinet.ui.intent.SendCounterIntent
import uz.veolia.cabinet.ui.uistate.AboutConsumerUiState
import uz.veolia.cabinet.ui.uistate.AccrualUiState
import uz.veolia.cabinet.ui.uistate.AddConsumerUiState
import uz.veolia.cabinet.ui.uistate.ConsumerInfoUiState
import uz.veolia.cabinet.ui.uistate.CreateAccrualUiState
import uz.veolia.cabinet.ui.uistate.LoginUiState
import uz.veolia.cabinet.ui.uistate.PersonalDataUiState
import uz.veolia.cabinet.ui.uistate.SendCounterUiState


fun splashPresenterPhone(componentContext: ComponentContext, navigation: StackNavigation<RootConfigPhone>) =
    SplashPhonePresenter(componentContext, navigation)

fun dashBoardPresenterPhone(
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>
): DashBoardPhonePresenter = DashBoardPhonePresenterImpl(componentContext, navigation)

fun consumerPresenterPhone(
    consumer: String,
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>
): CommonPresenter<ConsumerInfoIntent,ConsumerInfoUiState> {
    return ConsumerInfoPhonePresenterImpl(consumer, componentContext, navigation)
}

fun createConsumerPresenterPhone(
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>
): CommonPresenter<AddConsumerIntent, AddConsumerUiState> {
    return AddConsumerPhonePresenter(componentContext, navigation)
}


fun createAccrualPresenterPhone(
    consumer: String,
    balance: String,
    name: String,
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>,
    callBack: (String) -> Unit
): CommonPresenter<CreateAccrualIntent, CreateAccrualUiState> {
    return CreateAccrualPhonePresenterImpl(
        consumer,
        balance,
        name,
        componentContext,
        navigation,
        callBack
    )
}

fun sendCounterPresenterPhone(
    consumer: String,
    name: String?,
    last: String?,
    callBack: () -> Unit,
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>
): CommonPresenter<SendCounterIntent, SendCounterUiState> {
    return SendCounterPhonePresenterImpl(consumer, name, last, componentContext, navigation, callBack)
}

fun loginPresenterPhone(
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>
): CommonPresenter<LoginIntent, LoginUiState> {
    return LoginPhonePresenterImpl(componentContext, navigation)
}

fun aboutConsumerPresenterPhone(
    consumer: String,
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>,
    onUpdate: () -> Unit,
): CommonPresenter<AboutConsumerIntent, AboutConsumerUiState> {
    return AboutConsumerPhonePresenterImpl(
        consumer,
        componentContext,
        navigation,
        onUpdate
    )
}

fun personalDataPresenterPhone(componentContext: ComponentContext,navigation: StackNavigation<RootConfigPhone>): CommonPresenter<PersonalDataIntent, PersonalDataUiState> {
    return PersonalDataPhonePresenterImpl(componentContext,navigation)
}

fun historyPaymentPresenterPhone(
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>,
    consumer: String,
    name: String,
): HistoryPaymentPhonePresenter {
    return HistoryPaymentPhonePresenterImpl(componentContext, navigation, consumer,name)
}

fun historyCounterPresenterPhone(
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>,
    consumer: String,
): HistoryCounterPhonePresenter {
    return HistoryCounterPhonePresenterImpl(componentContext, navigation, consumer)
}

fun accrualPagePresenterPhone(
    componentContext: ComponentContext,
    navigator: StackNavigation<RootConfigPhone>,
    consumer: String,
    balance: String,
    name: String,
    onBalanceChange: (String) -> Unit,
): CommonPresenter<AccrualIntent, AccrualUiState> {
    return AccrualPhonePresenterImpl(
        componentContext,
        navigator,
        consumer,
        balance,
        name,
        onBalanceChange
    )
}


fun monitoringTabPresenterPhone(
    componentContext: ComponentContext,
    navigation: StackNavigation<RootConfigPhone>
): MonitoringTabPhonePresenter {
    return MonitoringTabPhonePresenterImpl(componentContext,navigation)
}

fun consumerTabPresenterPhone(
    componentContext: ComponentContext,
    navigator: StackNavigation<RootConfigPhone>,
): ConsumerTabPhonePresenter {
    return ConsumerTabPhonePresenterImpl(componentContext,navigator)
}
