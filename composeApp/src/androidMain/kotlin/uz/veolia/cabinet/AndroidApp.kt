package uz.veolia.cabinet

import android.app.Application
import uz.veolia.cabinet.data.local.ApplicationComponent

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextUtils.setContext(this)
        ApplicationComponent.init()
    }
}