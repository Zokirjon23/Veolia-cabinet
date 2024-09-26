package uz.veolia.cabinet.presenter.phone.impl

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.data.remote.response.PaymentItem
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.HistoryPaymentPhonePresenter
import uz.veolia.cabinet.ui.intent.HistoryPaymentIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.HistoryPaymentUiState
import uz.veolia.cabinet.util.extension.coroutineScope

class HistoryPaymentPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<RootConfigPhone>,
    private val consumer: String,
    private val name: String,
) : HistoryPaymentPhonePresenter, ComponentContext by componentContext, KoinComponent {

    private val repository by inject<AppRepository>()
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())
    override val payment = MutableStateFlow(
        PagingData.empty<PaymentItem>(
            sourceLoadStates = LoadStates(
                refresh = LoadState.Loading,
                append = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = false)
            )
        )
    )
    override val uiState = MutableValue(HistoryPaymentUiState(name))

    init {
        doOnDestroy { scope.cancel() }
        getPaymentList()
    }

    override fun onEventDispatcher(intent: HistoryPaymentIntent) {
        when (intent) {
            HistoryPaymentIntent.LogOut -> navigation.replaceAll(RootConfigPhone.Login)
            HistoryPaymentIntent.Back -> navigation.pop()
        }
    }

    private fun getPaymentList() {
        scope.launch {
            repository.getPaymentList(consumer)
                .distinctUntilChanged()
                .cachedIn(this)
                .collect { data ->
                    payment.update { data }
                }
        }
    }
}