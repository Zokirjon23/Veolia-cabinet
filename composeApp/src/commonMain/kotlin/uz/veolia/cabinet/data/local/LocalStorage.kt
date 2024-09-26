package uz.veolia.cabinet.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.plus
import uz.veolia.cabinet.dataStorePreferences


interface LocalStorage {
    suspend fun getToken(): String?
    suspend fun setToken(string: String?): Preferences
}

internal class LocalStorageImpl(
    private val dataStore: DataStore<Preferences>,
) : LocalStorage {

    private val tokenKey = stringPreferencesKey("token")

    override suspend fun getToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[tokenKey]
    }

    override suspend fun setToken(string: String?) = dataStore.edit { preferences ->
        preferences[tokenKey] = string ?: ""
    }
}

interface CoroutinesComponent {
    val mainImmediateDispatcher: CoroutineDispatcher
    val applicationScope: CoroutineScope
}

internal class CoroutinesComponentImpl private constructor() : CoroutinesComponent {

    companion object {
        fun create(): CoroutinesComponent = CoroutinesComponentImpl()
    }

    override val mainImmediateDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
    override val applicationScope: CoroutineScope
        get() = CoroutineScope(
            SupervisorJob() + mainImmediateDispatcher,
        )
}


interface CoreComponent : CoroutinesComponent {
    val localStorage: LocalStorage
}

internal class CoreComponentImpl internal constructor() : CoreComponent,
    CoroutinesComponent by CoroutinesComponentImpl.create() {

    private val dataStore: DataStore<Preferences> = dataStorePreferences(
        corruptionHandler = null,
        coroutineScope = applicationScope + Dispatchers.IO,
        migrations = emptyList()
    )

    override val localStorage: LocalStorage = LocalStorageImpl(dataStore)
}


object ApplicationComponent {
    private var _coreComponent: CoreComponent? = null
    val coreComponent
        get() = _coreComponent
            ?: throw IllegalStateException("Make sure to call ApplicationComponent.init()")

    fun init() {
        _coreComponent = CoreComponentImpl()
    }
}

//val coreComponent get() = ApplicationComponent.coreComponent

//import android.content.Context

//class LocalStorage(context: Context) {
//    private val preference = context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)
//
//    var token: String?
//        get() = preference.getString("token", null)
//        set(value) {
//            preference.edit().putString("token", value).apply()
//        }
//}