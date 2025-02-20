package com.luthfi.ecommerce.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.luthfi.ecommerce.core.data.datasource.api.ApiService
import com.luthfi.ecommerce.core.data.datasource.api.ProductPagingSource
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.body.ProductsBody
import com.luthfi.ecommerce.core.data.datasource.api.body.RatingBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.FulfillmentResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.LoginResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.PaymentResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProfileResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.RatingResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.RegisterResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ReviewResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.SearchResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.TransactionResponse
import com.luthfi.ecommerce.core.data.datasource.firebase.data.PromoFcm
import com.luthfi.ecommerce.core.data.datasource.firebase.data.toEntity
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import com.luthfi.ecommerce.core.data.datasource.room.dao.ChartDao
import com.luthfi.ecommerce.core.data.datasource.room.dao.FavoriteDao
import com.luthfi.ecommerce.core.data.datasource.room.dao.NotificationDao
import com.luthfi.ecommerce.core.data.datasource.room.entity.toNotification
import com.luthfi.ecommerce.core.data.datasource.room.entity.toProducts
import com.luthfi.ecommerce.core.domain.model.Notification
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.core.domain.model.toChartEntity
import com.luthfi.ecommerce.core.domain.model.toFavEntity
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RepositoryImpl(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService,
    private val chartDao: ChartDao,
    private val favoriteDao: FavoriteDao,
    private val notificationDao: NotificationDao
) :
    Repository {

    // Preference
    override fun getRefreshToken(): Flow<String> = userPreferences.getRefreshToken()
    override suspend fun setRefreshToken(value: String) {
        userPreferences.setRefreshToken(value)
    }

    override fun getLocale(): Flow<String> = userPreferences.getLocale()
    override suspend fun setLocale(value: String) {
        userPreferences.setLocale(value)
    }

    override fun getAccessToken(): Flow<String> = userPreferences.getAccessToken()
    override suspend fun setAccessToken(value: String) {
        userPreferences.setAccessToken(value)
    }

    override fun getIsOnboard(): Flow<Boolean> {
        return userPreferences.getIsOnboard()
    }

    override suspend fun setIsOnBoard(value: Boolean) {
        userPreferences.setIsOnBoard(value)
    }

    override fun getIsLight(): Flow<Boolean> = userPreferences.getIsLight()
    override suspend fun setIsLight(value: Boolean) {
        userPreferences.setIsLight(value)
    }

    override fun getUsername(): Flow<String> = userPreferences.getUsername()
    override suspend fun setUsername(value: String) {
        userPreferences.setUsername(value)
    }

    override fun getIsLogin(): Flow<Boolean> = userPreferences.getIsLogin()
    override suspend fun setIsLogin(value: Boolean) {
        userPreferences.setIsLogin(value)
    }

    override suspend fun logout() {
        userPreferences.logout()
    }

    // Room database
    override fun getAllChart(): Flow<List<Product>> = chartDao.getAllChart().map { it.toProducts() }
    override suspend fun addChart(product: Product, action: Boolean): String {
        val chart = chartDao.getStockChart(product.productId)
        if (chart != null) {
            return when (action) {
                true -> {
                    if (chart.count == chart.stock) {
                        CART_FULL
                    } else {
                        chart.count += 1
                        chartDao.updateCountChart(product.productId, chart.count)
                        CART_ADDED
                    }
                }

                false -> {
                    if (chart.count == 1) {
                        CART_MINIMUM
                    } else {
                        chart.count -= 1
                        chartDao.updateCountChart(product.productId, chart.count)
                        CART_DECREASED
                    }
                }
            }
        } else {
            chartDao.insertChart(product.toChartEntity(1, false))
            return CART_ADDED
        }
    }

    override suspend fun deleteItemChart(id: String) = chartDao.deleteItemChart(id)
    override suspend fun deleteCheckedChart() = chartDao.deleteCheckedChart()
    override suspend fun updateCheckAllChart(value: Boolean) = chartDao.updateCheckAllChart(value)
    override suspend fun updateIsCheckedChart(id: String, newIsChecked: Boolean) =
        chartDao.updateIsCheckedChart(id, newIsChecked)

    override fun getAllFav(): Flow<List<Product>> = favoriteDao.getAllFav().map { it.toProducts() }
    override suspend fun insertFav(product: Product) = favoriteDao.insertFav(product.toFavEntity())
    override suspend fun deleteItemFav(id: String) = favoriteDao.deleteItemFav(id)
    override fun getIsFav(id: String) = favoriteDao.getIsFav(id)

    override suspend fun updateCheckNotification(id: Int, value: Boolean) {
        notificationDao.updateIsCheckedNotification(id, value)
    }

    override fun getAllNotification(): Flow<List<Notification>> =
        notificationDao.getAllNotification().map { it.toNotification() }

    override suspend fun insertNotification(notification: PromoFcm) =
        notificationDao.insertNotification(notification.toEntity())

    // Api
    override fun login(body: LoginBody): Flow<Resource<LoginResponse>> {
        return flow {
            try {
                val response = apiService.login(loginBody = body)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun register(body: LoginBody): Flow<Resource<RegisterResponse>> {
        return flow {
            try {
                val response = apiService.register(loginBody = body)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!)) // Kodingan 1 (asli d pake buat ngambil data dari API
//                    emit( //Kodingan 2 dia setiap dipanggil bakal return data dummy d bawah
//                        Resource.Success(
//                            RegisterResponse(
//                                code = 200,
//                                data = RegisterResponse.Data(
//                                    accessToken = "mytoken",
//                                    expiresAt = null,
//                                    refreshToken = "myrefreshtoken"
//                                ),
//                                message = "Success"
//                            )
//                        )
//                    )
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun profile(
        userName: RequestBody,
        userImage: MultipartBody.Part?
    ): Flow<Resource<ProfileResponse>> {
        return flow {
            try {
                val response = apiService.profile(userName, userImage)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun product(productsBody: ProductsBody?) = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 1,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            ProductPagingSource(apiService, productsBody)
        }
    ).flow

    override fun search(query: String?): Flow<Resource<SearchResponse>> {
        return flow {
            try {
                val response = apiService.search(query)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun productDetail(id: String?): Flow<Resource<ProductResponse>> {
        return flow {
            try {
                val response = apiService.productDetail(id)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun productReview(id: String?): Flow<Resource<ReviewResponse>> {
        return flow {
            try {
                val response = apiService.productReview(id)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun payment(): Flow<Resource<PaymentResponse>> {
        return flow {
            try {
                val response = apiService.payment()
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun fulfillment(fulfillmentBody: FulfillmentBody): Flow<Resource<FulfillmentResponse>> {
        return flow {
            try {
                val response = apiService.fulfillment(fulfillmentBody)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun rating(body: RatingBody): Flow<Resource<RatingResponse>> {
        return flow {
            try {
                val response = apiService.rating(body)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun transaction(): Flow<Resource<TransactionResponse>> {
        return flow {
            try {
                val response = apiService.transaction()
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    if (response.errorBody() != null) {
                        emit(
                            Resource.Error(
                                response.errorBody()!!.string()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        const val CART_FULL = "cartFull"
        const val CART_ADDED = "cartAdded"
        const val CART_DECREASED = "cartDecreased"
        const val CART_MINIMUM = "cartMinimum"
    }
}
