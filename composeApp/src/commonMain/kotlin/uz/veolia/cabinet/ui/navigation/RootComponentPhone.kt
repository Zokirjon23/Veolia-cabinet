package uz.veolia.cabinet.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import uz.veolia.cabinet.data.remote.response.ConsumerDetailsBody
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.presenter.phone.DashBoardPhonePresenter
import uz.veolia.cabinet.presenter.phone.HistoryCounterPhonePresenter
import uz.veolia.cabinet.presenter.phone.HistoryPaymentPhonePresenter
import uz.veolia.cabinet.presenter.phone.SplashPhonePresenter
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

interface RootComponentPhone {
    val stack: Value<ChildStack<*, Child>>
    sealed interface Child {
        data class SplashChild(val splashPresenter: SplashPhonePresenter) : Child
        class LoginChild(val presenter: CommonPresenter<LoginIntent, LoginUiState>) : Child
        class DashboardChild(val presenter: DashBoardPhonePresenter) : Child
        class SendCounterChild(val presenter: CommonPresenter<SendCounterIntent, SendCounterUiState>) :
            Child

        class ConsumerChild(val presenter: CommonPresenter<ConsumerInfoIntent, ConsumerInfoUiState>) :
            Child

        class CreateConsumerChild(val presenter: CommonPresenter<AddConsumerIntent, AddConsumerUiState>) :
            Child

        class CreateAccrualChild(val presenter: CommonPresenter<CreateAccrualIntent, CreateAccrualUiState>) :
            Child

        data class AboutConsumer(
            val presenter: CommonPresenter<AboutConsumerIntent, AboutConsumerUiState>,
            val data: ConsumerDetailsBody?
        ) :
            Child

        data class HistoryCounter(val presenter: HistoryCounterPhonePresenter, val name: String) :
            Child

        data class PersonalData(
            val presenter: CommonPresenter<PersonalDataIntent, PersonalDataUiState>,
            val login: String,
            val password: String,
            val passport: String,
            val pinfl: String,
            val inn: String,
            val cadastre: String,
            val name: String
        ) :
            Child

        data class HistoryPayment(val presenter: HistoryPaymentPhonePresenter) :
            Child

        data class Accrual(val presenter: CommonPresenter<AccrualIntent, AccrualUiState>) :
            Child
    }
}

class RootComponentPhoneImpl(componentContext: ComponentContext) : RootComponentPhone,
    ComponentContext by componentContext {
    private val navigator = StackNavigation<RootConfigPhone>()
    override val stack: Value<ChildStack<*, RootComponentPhone.Child>> =
        childStack(
            source = navigator,
            serializer = RootConfigPhone.serializer(),
            initialConfiguration = RootConfigPhone.Splash,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: RootConfigPhone,
        componentContext: ComponentContext
    ): RootComponentPhone.Child =
        when (config) {
            is RootConfigPhone.Splash -> RootComponentPhone.Child.SplashChild(
                splashPresenterPhone(
                    componentContext,
                    navigator
                )
            )

            is RootConfigPhone.ConsumerPhone -> RootComponentPhone.Child.ConsumerChild(
                consumerPresenterPhone(
                    config.id,
                    componentContext,
                    navigator
                )
            )

            is RootConfigPhone.CreateConsumer -> RootComponentPhone.Child.CreateConsumerChild(
                createConsumerPresenterPhone(componentContext, navigator)
            )

            is RootConfigPhone.Dashboard -> RootComponentPhone.Child.DashboardChild(
                dashBoardPresenterPhone(
                    componentContext,
                    navigator
                )
            )

            is RootConfigPhone.Login -> RootComponentPhone.Child.LoginChild(
                loginPresenterPhone(
                    componentContext,
                    navigator
                )
            )

            is RootConfigPhone.SendCounter -> RootComponentPhone.Child.SendCounterChild(
                sendCounterPresenterPhone(
                    config.consumerId,
                    config.name,
                    config.last,
                    config.callback,
                    componentContext,
                    navigator
                )
            )

            is RootConfigPhone.CreateAccrual -> RootComponentPhone.Child.CreateAccrualChild(
                createAccrualPresenterPhone(
                    config.consumer,
                    config.balance,
                    config.name,
                    componentContext,
                    navigator,
                    config.callBack
                )
            )

            is RootConfigPhone.AboutConsumer -> RootComponentPhone.Child.AboutConsumer(
                aboutConsumerPresenterPhone(
                    config.consumer,
                    componentContext,
                    navigator,
                    onUpdate = config.update,
                ), config.data
            )

            is RootConfigPhone.Accrual -> {
                RootComponentPhone.Child.Accrual(
                    accrualPagePresenterPhone(
                        componentContext,
                        navigator,
                        consumer = config.consumer,
                        onBalanceChange = config.balanceCallBack,
                        balance = config.balance,
                        name = config.name,
                    )
                )
            }

            is RootConfigPhone.HistoryCounter -> RootComponentPhone.Child.HistoryCounter(
                historyCounterPresenterPhone(componentContext, navigator, config.consumerId),
                config.name
            )

            is RootConfigPhone.HistoryPayment -> RootComponentPhone.Child.HistoryPayment(
                historyPaymentPresenterPhone(
                    componentContext,
                    navigator,
                    config.consumerId,
                    config.name
                )
            )

            is RootConfigPhone.PersonalData -> RootComponentPhone.Child.PersonalData(
                personalDataPresenterPhone(componentContext, navigator),
                config.login,
                config.password,
                config.passport,
                config.pinfl,
                config.inn,
                config.cadastre,
                config.name
            )
        }

    fun onBackPressed(){
        navigator.pop()
    }
}

@Serializable
sealed interface RootConfigPhone {
    @Serializable
    data object Splash : RootConfigPhone

    @Serializable
    data object Login : RootConfigPhone

    @Serializable
    data object Dashboard : RootConfigPhone

    @Serializable
    data class SendCounter(
        val consumerId: String,
        val last: String?,
        val name: String?,
        @Transient val callback: () -> Unit = { }
    ) : RootConfigPhone

    @Serializable
    data class ConsumerPhone(val id: String) : RootConfigPhone

    @Serializable
    data object CreateConsumer : RootConfigPhone

    @Serializable
    data class CreateAccrual(
        val consumer: String,
        val balance: String,
        val name: String,
        @Transient val callBack: (String) -> Unit = { },
    ) : RootConfigPhone

    @Serializable
    class AboutConsumer(
        val consumer: String,
        val data: ConsumerDetailsBody?,
        @Transient val update: () -> Unit = {}
    ) : RootConfigPhone

    @Serializable
    class HistoryCounter(val consumerId: String, val name: String) : RootConfigPhone

    @Serializable
    class PersonalData(
        val login: String,
        val password: String,
        val passport: String,
        val pinfl: String,
        val inn: String,
        val cadastre: String,
        val name: String,
    ) : RootConfigPhone

    @Serializable
    class HistoryPayment(val consumerId: String, val name: String) : RootConfigPhone

    @Serializable
    class Accrual(
        val consumer: String,
        val name: String,
        val balance: String,
        @Transient
        val balanceCallBack: (String) -> Unit = { }
    ) : RootConfigPhone
}