package com.luthfi.ecommerce.core.domain.repository

import androidx.paging.PagingData
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.body.ProductsBody
import com.luthfi.ecommerce.core.data.datasource.api.body.RatingBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.FulfillmentResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.LoginResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.PaymentResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductsResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProfileResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.RatingResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.RegisterResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ReviewResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.SearchResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.TransactionResponse
import com.luthfi.ecommerce.core.data.datasource.firebase.data.PromoFcm
import com.luthfi.ecommerce.core.domain.model.Notification
import com.luthfi.ecommerce.core.domain.model.Product
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface Repository {

    fun getRefreshToken(): Flow<String>
    suspend fun setRefreshToken(value: String)
    fun getLocale(): Flow<String>
    suspend fun setLocale(value: String)
    fun getAccessToken(): Flow<String>
    suspend fun setAccessToken(value: String)
    fun getIsOnboard(): Flow<Boolean>
    suspend fun setIsOnBoard(value: Boolean)
    fun getIsLight(): Flow<Boolean>
    suspend fun setIsLight(value: Boolean)
    fun getUsername(): Flow<String>
    suspend fun setUsername(value: String)
    fun getIsLogin(): Flow<Boolean>
    suspend fun setIsLogin(value: Boolean)
    suspend fun logout()
    fun getAllFav(): Flow<List<Product>>
    fun getAllChart(): Flow<List<Product>>
    suspend fun insertFav(product: Product)
    suspend fun addChart(product: Product, action: Boolean): String
    suspend fun deleteItemChart(id: String)
    suspend fun deleteItemFav(id: String)
    suspend fun deleteCheckedChart()
    suspend fun updateCheckAllChart(value: Boolean)
    suspend fun updateIsCheckedChart(id: String, newIsChecked: Boolean)

    fun getIsFav(id: String): Flow<Boolean>
    fun login(body: LoginBody): Flow<Resource<LoginResponse>>
    fun register(body: LoginBody): Flow<Resource<RegisterResponse>>
    fun profile(
        userName: RequestBody,
        userImage: MultipartBody.Part?
    ): Flow<Resource<ProfileResponse>>

    fun product(productsBody: ProductsBody?): Flow<PagingData<ProductsResponse.Data.Item>>
    fun search(query: String?): Flow<Resource<SearchResponse>>
    fun productDetail(id: String?): Flow<Resource<ProductResponse>>
    fun productReview(id: String?): Flow<Resource<ReviewResponse>>

    fun payment(): Flow<Resource<PaymentResponse>>
    fun fulfillment(fulfillmentBody: FulfillmentBody): Flow<Resource<FulfillmentResponse>>
    suspend fun rating(body: RatingBody): Flow<Resource<RatingResponse>>
    suspend fun transaction(): Flow<Resource<TransactionResponse>>
    suspend fun updateCheckNotification(id: Int, value: Boolean)
    fun getAllNotification(): Flow<List<Notification>>
    suspend fun insertNotification(notification: PromoFcm)
}
