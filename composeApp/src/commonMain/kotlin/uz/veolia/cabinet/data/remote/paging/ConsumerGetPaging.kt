package uz.veolia.cabinet.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.coroutines.runBlocking
import uz.veolia.cabinet.data.local.LocalStorage
import uz.veolia.cabinet.data.remote.Api
import uz.veolia.cabinet.data.remote.request.ConsumerGetRequest
import uz.veolia.cabinet.data.remote.response.ConsumerGetResponse
import uz.veolia.cabinet.data.remote.response.ConsumerItem
import uz.veolia.cabinet.util.extension.isConnection

class ConsumerGetPaging(
    private val api: Api,
    private val keyword: String,
    private val status: Boolean?,
    private val type: String?,
    private val region: Int?,
    private val localStorage : LocalStorage
) : PagingSource<Int, ConsumerItem>() {
    override fun getRefreshKey(state: PagingState<Int, ConsumerItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ConsumerItem> {
        val page = params.key ?: 1
        return try {
            val request = ConsumerGetRequest(region, keyword, page, 30, status, type)
            val response = api.getConsumer(request)
            if (response.status == Unauthorized){
                runBlocking { localStorage.setToken(null) }
            }
            val result = response.body<ConsumerGetResponse>()
            if (result.code == 0) {
                LoadResult.Page(
                    data = result.body?.list ?: listOf(),
                    prevKey = null,
                    nextKey = if ((result.body?.count
                            ?: 0) <= (30 * page) || result.body?.list?.isEmpty() == true
                    ) null else page + 1
                )
            } else {
                LoadResult.Error(Throwable(result.msg ?: response.status.description))
            }
        } catch (e: Exception) {
            LoadResult.Error(if (e.isConnection()) Exception("Ошибка подключения") else e)
        }
    }
}