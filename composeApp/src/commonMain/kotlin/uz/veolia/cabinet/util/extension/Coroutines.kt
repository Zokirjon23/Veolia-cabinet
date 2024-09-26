package uz.veolia.cabinet.util.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.collectData(
    coroutineScope: CoroutineScope,
    crossinline block: suspend T.() -> Unit
) {
    coroutineScope.launch {
        onEach {
            block(it)
        }.collect()
    }
}