package uz.veolia.cabinet.data.remote.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import uz.veolia.cabinet.data.local.LocalStorage
import uz.veolia.cabinet.data.remote.Api
import uz.veolia.cabinet.data.remote.response.PaymentItem
import uz.veolia.cabinet.data.remote.response.PaymentListResponse
import uz.veolia.cabinet.util.extension.isConnection

class PaymentHistoryPaging(
    private val api: Api,
    private val consumer: String,
    private val localStorage : LocalStorage
) : PagingSource<Int, PaymentItem>() {
    override fun getRefreshKey(state: PagingState<Int, PaymentItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaymentItem> {
        val page = params.key ?: 1
        return try {
            val response = api.getPaymentList(consumer,page,30)
            if (response.status == Unauthorized){
                localStorage.setToken(null)
            }
            val result = response.body<PaymentListResponse>()
            if (result.code == 0) {
                LoadResult.Page(
                    data = result.body?.list ?: listOf(),
                    prevKey = null,
                    nextKey = if ((result.body?.count ?: 0) <= (30 * page) || result.body?.list?.isEmpty() == true) null else page + 1
                )
            } else {
                LoadResult.Error(Throwable(result.msg ?: response.status.description))
            }
        } catch (e: Exception) {
            LoadResult.Error(if (e.isConnection()) Exception("Ошибка подключения") else e)
        }
    }
}