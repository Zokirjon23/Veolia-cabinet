package uz.veolia.cabinet.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.coroutines.runBlocking
import uz.veolia.cabinet.data.local.LocalStorage
import uz.veolia.cabinet.data.remote.Api
import uz.veolia.cabinet.data.remote.response.CounterItem
import uz.veolia.cabinet.data.remote.response.GetCounterResponse
import uz.veolia.cabinet.util.extension.isConnection

class GetMeterHistoryPaging(
    private val api: Api,
    private val consumer: String,
    private val localStorage : LocalStorage
) : PagingSource<Int, CounterItem>() {
    override fun getRefreshKey(state: PagingState<Int, CounterItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CounterItem> {
        val page = params.key ?: 1
        return try {
            val response = api.getCounterHistory(consumer,page,30)
            if (response.status == Unauthorized){
                runBlocking { localStorage.setToken(null) }
            }
            val result = response.body<GetCounterResponse>()
            if (result.code == 0) {
                LoadResult.Page(
                    data = result.counterListBody?.list ?: listOf(),
                    prevKey = null,
                    nextKey = if ((result.counterListBody?.count ?: 0) <= (30 * page) || result.counterListBody?.list?.isEmpty() == true) null else page + 1
                )
            } else {
                LoadResult.Error(Throwable(result.msg ?: response.status.description))
            }
        } catch (e: Exception) {
            LoadResult.Error(if (e.isConnection()) Exception("Ошибка подключения") else e)
        }
    }
}