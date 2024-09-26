package uz.veolia.cabinet.presenter.phone.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.intent.PersonalDataIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.PersonalDataUiState

class PersonalDataPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<RootConfigPhone>
) :
    CommonPresenter<PersonalDataIntent, PersonalDataUiState>(componentContext) {
    override val uiState = MutableValue(PersonalDataUiState())

    override fun onEventDispatcher(intent: PersonalDataIntent) {
        when (intent) {
            PersonalDataIntent.Back -> navigation.pop()
        }
    }
}