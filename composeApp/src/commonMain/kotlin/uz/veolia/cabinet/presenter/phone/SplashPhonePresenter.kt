package uz.veolia.cabinet.presenter.phone

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.util.extension.coroutineScope

class SplashPhonePresenter(
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>
) : ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())
    private val repository by inject<AppRepository>()

    init {
        doOnDestroy { scope.coroutineContext }
        scope.launch {
            delay(2000)
            navigator.replaceAll(if (!repository.isExpired()) RootConfigPhone.Dashboard else RootConfigPhone.Login)
        }

    }
}