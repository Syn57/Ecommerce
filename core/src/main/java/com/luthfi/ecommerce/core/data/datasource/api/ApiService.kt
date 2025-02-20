package com.luthfi.ecommerce.core.data.datasource.api

import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.body.RatingBody
import com.luthfi.ecommerce.core.data.datasource.api.body.RefreshBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.FulfillmentResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.LoginResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.PaymentResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductsResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProfileResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.RatingResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.RefreshResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.RegisterResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.ReviewResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.SearchResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.TransactionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<LoginResponse>

    @POST("refresh")
    suspend fun refreshToken(
        @Body refreshToken: RefreshBody
    ): Response<RefreshResponse>

    @POST("register")
    suspend fun register(
        @Body loginBody: LoginBody
    ): Response<RegisterResponse>

    @Multipart
    @POST("profile")
    suspend fun profile(
        @Part("userName") userName: RequestBody,
        @Part userImage: MultipartBody.Part? = null
    ): Response<ProfileResponse>

    @POST("products")
    suspend fun product(
        @Query("search") search: String? = null,
        @Query("brand") brand: String? = null,
        @Query("lowest") lowest: Int? = null,
        @Query("highest") highest: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): Response<ProductsResponse>

    @POST("search")
    suspend fun search(
        @Query("query") query: String? = null
    ): Response<SearchResponse>

    @GET("products/{id}")
    suspend fun productDetail(
        @Path("id") id: String? = null
    ): Response<ProductResponse>

    @GET("review/{id}")
    suspend fun productReview(
        @Path("id") id: String? = null
    ): Response<ReviewResponse>

    @GET("payment")
    suspend fun payment(): Response<PaymentResponse>

    @POST("fulfillment")
    suspend fun fulfillment(
        @Body fulfillmentBody: FulfillmentBody
    ): Response<FulfillmentResponse>

    @POST("rating")
    suspend fun rating(
        @Body ratingBody: RatingBody
    ): Response<RatingResponse>

    @GET("transaction")
    suspend fun transaction(): Response<TransactionResponse>
}
