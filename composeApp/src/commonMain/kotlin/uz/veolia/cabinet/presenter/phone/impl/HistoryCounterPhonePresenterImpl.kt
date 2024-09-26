package uz.veolia.cabinet.presenter.phone.impl

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.data.remote.response.CounterItem
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.HistoryCounterPhonePresenter
import uz.veolia.cabinet.ui.intent.HistoryCounterIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.HistoryCounterUiState
import uz.veolia.cabinet.util.extension.coroutineScope

class HistoryCounterPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<RootConfigPhone>,
    private val consumer: String,
) : HistoryCounterPhonePresenter, ComponentContext by componentContext, KoinComponent {
    private val coroutineScope = coroutineScope(Dispatchers.Main + kotlinx.coroutines.SupervisorJob())
    private val repository by inject<AppRepository>()
    override val meters = MutableStateFlow(
        PagingData.empty<CounterItem>(
            sourceLoadStates = LoadStates(
                refresh = LoadState.Loading,
                append = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = false)
            )
        )
    )
    override val uiState = MutableValue(HistoryCounterUiState())

    init {
        doOnDestroy { coroutineScope.cancel() }
        getHistory()
    }

    override fun onEventDispatcher(intent: HistoryCounterIntent) {
        when (intent) {
            is HistoryCounterIntent.SendCounter -> navigation.push(
                RootConfigPhone.SendCounter(
                    consumer,
                    intent.count ?: "",
                    intent.name ?: ""
                )
                { getHistory() }
            )

            HistoryCounterIntent.LogOut -> navigation.replaceAll(RootConfigPhone.Login)
            HistoryCounterIntent.Back -> navigation.pop()
        }
    }

    private fun getHistory() {
        coroutineScope.launch {
            repository.getMeterHistory(consumer)
                .distinctUntilChanged()
                .cachedIn(this)
                .collect { data ->
                    meters.update { data }
                }
        }
    }
}