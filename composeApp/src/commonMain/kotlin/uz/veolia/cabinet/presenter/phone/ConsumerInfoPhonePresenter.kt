package uz.veolia.cabinet.presenter.phone

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import uz.veolia.cabinet.ui.intent.AboutConsumerIntent
import uz.veolia.cabinet.ui.intent.AccrualIntent
import uz.veolia.cabinet.ui.intent.ConsumerInfoIntent
import uz.veolia.cabinet.ui.intent.PersonalDataIntent
import uz.veolia.cabinet.ui.uistate.AboutConsumerUiState
import uz.veolia.cabinet.ui.uistate.AccrualUiState
import uz.veolia.cabinet.ui.uistate.ConsumerInfoUiState
import uz.veolia.cabinet.ui.uistate.PersonalDataUiState

interface ConsumerInfoPhonePresenter {
    val uiState: Value<ConsumerInfoUiState>
    fun onEventDispatcher(intent: ConsumerInfoIntent)

    sealed interface Page {
        data class AboutConsumerPage(val presenter: CommonPresenter<AboutConsumerIntent, AboutConsumerUiState>) :
            Page

        data class HistoryCounterPage(val presenter: HistoryCounterPhonePresenter) : Page
        data class PersonalDataPage(val presenter: CommonPresenter<PersonalDataIntent, PersonalDataUiState>) :
            Page

        data class HistoryPaymentPage(val presenter: HistoryPaymentPhonePresenter) : Page
        data class AccrualPage(val presenter: CommonPresenter<AccrualIntent, AccrualUiState>) : Page
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object AboutConsumerPage : Config

        @Serializable
        data object HistoryCounterPage : Config

        @Serializable
        data object PersonalDataPage : Config

        @Serializable
        data object HistoryPaymentPage : Config

        @Serializable
        data object AccrualPage : Config
    }

    @OptIn(ExperimentalDecomposeApi::class)
    val pages: Value<ChildPages<*, Page>>
}