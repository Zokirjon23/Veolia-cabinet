package uz.veolia.cabinet.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import uz.veolia.cabinet.data.local.LocalStorage
import uz.veolia.cabinet.data.remote.Api
import uz.veolia.cabinet.data.remote.request.MonitoringRequest
import uz.veolia.cabinet.data.remote.response.MonitoringItem
import uz.veolia.cabinet.data.remote.response.MonitoringResponse
import uz.veolia.cabinet.util.extension.isConnection
import uz.veolia.cabinet.util.extension.toFormattedDate

class MonitoringPaging(
    private val api: Api,
    private val keyword: String,
    private val state: String?,
    private val district: Int?,
    private val service: String?,
    private val startDate: Long,
    private val endDate: Long,
    private val localStorage: LocalStorage
) : PagingSource<Int, MonitoringItem>() {
    override fun getRefreshKey(state: PagingState<Int, MonitoringItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MonitoringItem> {
        val page = params.key ?: 1
        return try {
            val request = MonitoringRequest(
                keyword,
                state,
                page,
                30,
                district,
                service,
                startDate.toFormattedDate(),
                endDate.toFormattedDate()
            )
            val response = api.getMonitoring(request)
            if (response.status == Unauthorized) {
                localStorage.setToken(null)
            }
            val result = response.body<MonitoringResponse>()
            if (result.code == 0) {
                LoadResult.Page(
                    data = result.body?.list ?: listOf(),
                    prevKey = null,
                    nextKey = if ((result.body?.count
                            ?: 0) <= (30 * page) || result.body?.list?.isEmpty() == true
                    ) null else page + 1
                )
            } else {
                LoadResult.Page(
                    data = result.body?.list ?: listOf(),
                    prevKey = null,
                    nextKey = null
                )
                LoadResult.Error(Throwable(result.msg ?: response.status.description))
            }
        } catch (e: Exception) {
            LoadResult.Error(if (e.isConnection()) Exception("Ошибка подключения") else e)
        }
    }
}