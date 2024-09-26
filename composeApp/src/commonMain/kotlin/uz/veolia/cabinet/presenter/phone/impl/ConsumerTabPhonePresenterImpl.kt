package uz.veolia.cabinet.presenter.phone.impl

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.data.remote.response.ConsumerItem
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.ConsumerTabPhonePresenter
import uz.veolia.cabinet.ui.intent.ConsumerTabIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.ConsumerTabUiState
import uz.veolia.cabinet.util.extension.coroutineScope

class ConsumerTabPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>
) : ConsumerTabPhonePresenter, ComponentContext by componentContext, KoinComponent {
    override val consumer = MutableStateFlow(PagingData.from(listOf<ConsumerItem>()))
    private val repository by inject<AppRepository>()
    private val tokenPayload = runBlocking { repository.tokenPayload() }
    private val regions =
        if (tokenPayload?.resourceAccess?.cabinetClient?.roles?.find { r -> r == "REGION" } != null) {
            tokenPayload.region ?: listOf()
        } else {
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13)
        }
    override val uiState = MutableValue(
        ConsumerTabUiState(
            region = regions,
            name = tokenPayload?.name,
            filterRegion = if (regions.size == 12) null else {
                if (regions.isNotEmpty()) regions[0] else null
            },
            isMustSelect = regions.size != 12,
            filterPersonType = if (RoleManager.canJuridical() && RoleManager.canPhysical()) null else if (RoleManager.canJuridical()) "3" else if (RoleManager.canPhysical()) "1" else null
        )
    )
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())
    private var search = ""

    init {
        if (RoleManager.hasConsumerAccess())
            getConsumers()
    }

    override fun onEventDispatcher(intent: ConsumerTabIntent) {
        when (intent) {
            ConsumerTabIntent.AddClicked -> navigator.push(RootConfigPhone.CreateConsumer)
            ConsumerTabIntent.Clear -> consumer.update { PagingData.from(listOf()) }
            ConsumerTabIntent.ConsumerReload -> {
                getConsumers(
                    uiState.value.search,
                    uiState.value.filterPersonType,
                    uiState.value.filterStatus,
                    uiState.value.filterRegion
                )
            }

            is ConsumerTabIntent.Filter -> {
                uiState.update {
                    it.copy(
                        filterPersonType = intent.type,
                        filterStatus = intent.status,
                        filterRegion = intent.region
                    )
                }
                getConsumers(uiState.value.search, intent.type, intent.status, intent.region)
            }

            is ConsumerTabIntent.ItemClicked -> navigator.push(RootConfigPhone.ConsumerPhone(intent.consumer))
            ConsumerTabIntent.LogOut -> {
                repository.logOut()
                navigator.replaceAll(RootConfigPhone.Login)
            }

            is ConsumerTabIntent.OnSearchChange -> uiState.update { it.copy(search = intent.search) }
            ConsumerTabIntent.Search -> {
                if (uiState.value.search != search) {
                    getConsumers(
                        uiState.value.search,
                        uiState.value.filterPersonType,
                        uiState.value.filterStatus,
                        uiState.value.filterRegion
                    )
                }
            }

            ConsumerTabIntent.ToastHide -> uiState.update { it.copy(warningText = null) }
        }
    }

    private fun getConsumers(
        search: String = "",
        type: String? = if (RoleManager.canJuridical() && RoleManager.canPhysical()) null else if (RoleManager.canJuridical()) "3" else if (RoleManager.canPhysical()) "1" else null,
        status: Boolean? = null,
        region: Int? = if (regions.size == 12) null else {
            if (regions.isNotEmpty()) regions[0] else null
        }
    ) {
        scope.launch {
            repository.getConsumers(
                keyword = search,
                type = type,
                status = status,
                region = region
            ).distinctUntilChanged()
                .cachedIn(this)
                .collect { data ->
                    consumer.update { data }
                    this@ConsumerTabPhonePresenterImpl.search = search
                }
        }
    }
}