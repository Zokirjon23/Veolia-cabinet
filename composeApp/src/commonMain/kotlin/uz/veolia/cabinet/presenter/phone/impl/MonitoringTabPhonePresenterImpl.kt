package uz.veolia.cabinet.presenter.phone.impl

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
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
import uz.veolia.cabinet.data.remote.response.MonitoringItem
import uz.veolia.cabinet.data.model.RoleManager
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.MonitoringTabPhonePresenter
import uz.veolia.cabinet.ui.intent.MonitoringTabIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.MonitoringTabUiState
import uz.veolia.cabinet.util.extension.coroutineScope

class MonitoringTabPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>
) : MonitoringTabPhonePresenter, ComponentContext by componentContext, KoinComponent {
    private val repository by inject<AppRepository>()
    private val tokenPayload = runBlocking { repository.tokenPayload() }
    private val regions =
        if (tokenPayload?.resourceAccess?.cabinetClient?.roles?.find { r -> r == "REGION" } != null) {
            tokenPayload.region ?: listOf()
        } else {
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13)
        }
    override val uiState = MutableValue(
        MonitoringTabUiState(
            region = regions,
            name = tokenPayload?.name,
            filterRegion = if (regions.size == 12) null else {
                if (regions.isNotEmpty()) regions[0] else null
            },
            isMustSelect = regions.size != 12
        )
    )
    private var search = ""
    override val monitoring = MutableStateFlow(PagingData.from(listOf<MonitoringItem>()))
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        if (RoleManager.hasMonitoringPay()) {
            getMonitoring()
        }
    }

    override fun onEventDispatcher(intent: MonitoringTabIntent) {
        when (intent) {
            MonitoringTabIntent.Clear -> monitoring.update { PagingData.from(listOf()) }
            is MonitoringTabIntent.Filter -> {
                uiState.update {
                    it.copy(
                        serviceId = intent.service,
                        filterRegion = intent.district,
                        startDate = intent.startDate,
                        endDate = intent.endDate
                    )
                }
                getMonitoring(
                    search = uiState.value.search,
                    district = intent.district,
                    service = uiState.value.serviceId,
                    startDate = uiState.value.startDate,
                    endDate = uiState.value.endDate
                )
            }

            MonitoringTabIntent.LogOut -> {
                repository.logOut()
                navigator.replaceAll(RootConfigPhone.Login)
            }

            MonitoringTabIntent.MonitoringReload -> {
                getMonitoring(
                    search = uiState.value.search,
                    district = uiState.value.filterRegion,
                    service = uiState.value.serviceId,
                    startDate = uiState.value.startDate,
                    endDate = uiState.value.endDate
                )
            }

            is MonitoringTabIntent.OnSearchChange -> uiState.update { it.copy(search = intent.search) }
            MonitoringTabIntent.Search -> {
                if (uiState.value.search != search) {
                    getMonitoring(
                        search = uiState.value.search,
                        district = uiState.value.filterRegion,
                        service = uiState.value.serviceId,
                        startDate = uiState.value.startDate,
                        endDate = uiState.value.endDate
                    )
                }
            }

            MonitoringTabIntent.ToastHide -> uiState.update { it.copy(warningText = null) }
        }
    }

    private fun getMonitoring(
        search: String = "",
        state: String? = null,
        district: Int? = if (regions.size == 12) null else {
            if (regions.isNotEmpty()) regions[0] else null
        },
        service: String? = uiState.value.serviceId,
        startDate : Long = uiState.value.startDate,
        endDate : Long = uiState.value.endDate
    ) {
        scope.launch {
            repository.getMonitoring(
                keyword = search,
                state = state,
                district = district,
                service = service,
                startDate = startDate,
                endDate = endDate,
            ).distinctUntilChanged()
                .cachedIn(this)
                .collect { data ->
                    monitoring.update { data }
                    this@MonitoringTabPhonePresenterImpl.search = search
                }
        }
    }
}