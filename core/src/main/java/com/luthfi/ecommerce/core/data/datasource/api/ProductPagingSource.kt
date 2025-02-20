package com.luthfi.ecommerce.core.data.datasource.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luthfi.ecommerce.core.data.datasource.api.body.ProductsBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductsResponse
import retrofit2.HttpException

class ProductPagingSource(
    private val apiService: ApiService,
    private val productsBody: ProductsBody?
) : PagingSource<Int, ProductsResponse.Data.Item>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ProductsResponse.Data.Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsResponse.Data.Item> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.product(
                search = productsBody?.search,
                brand = productsBody?.brand,
                lowest = productsBody?.lowest,
                highest = productsBody?.highest,
                sort = productsBody?.sort,
                10,
                position
            )

            if (responseData.isSuccessful) {
                LoadResult.Page(
                    data = responseData.body()?.data?.items!!,
                    prevKey = null,
                    nextKey = if (position == responseData.body()!!.data.totalPages) null else position + 1
                )
            } else {
                LoadResult.Error(
                    HttpException(responseData)
                )
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}
