package com.luthfi.ecommerce.core.repository

import com.luthfi.ecommerce.core.data.datasource.api.ApiService
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
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.fulfillmentResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.loginResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.paymentResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.productDetailResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.productsResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.profileResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.ratingResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.refreshResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.registerResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.reviewResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.searchResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.transactionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakeApiService : ApiService {
    override suspend fun login(loginBody: LoginBody): Response<LoginResponse> {
        return Response.success(loginResponse)
    }

    override suspend fun refreshToken(refreshToken: RefreshBody): Response<RefreshResponse> {
        return Response.success(refreshResponse)
    }

    override suspend fun register(loginBody: LoginBody): Response<RegisterResponse> {
        return Response.success(registerResponse)
    }

    override suspend fun profile(
        userName: RequestBody,
        userImage: MultipartBody.Part?
    ): Response<ProfileResponse> {
        return Response.success(profileResponse)
    }

    override suspend fun product(
        search: String?,
        brand: String?,
        lowest: Int?,
        highest: Int?,
        sort: String?,
        limit: Int?,
        page: Int?
    ): Response<ProductsResponse> {
        return Response.success(productsResponse)
    }

    override suspend fun search(query: String?): Response<SearchResponse> {
        return Response.success(searchResponse)
    }

    override suspend fun productDetail(id: String?): Response<ProductResponse> {
        return Response.success(productDetailResponse)
    }

    override suspend fun productReview(id: String?): Response<ReviewResponse> {
        return Response.success(reviewResponse)
    }

    override suspend fun payment(): Response<PaymentResponse> {
        return Response.success(paymentResponse)
    }

    override suspend fun fulfillment(fulfillmentBody: FulfillmentBody): Response<FulfillmentResponse> {
        return Response.success(fulfillmentResponse)
    }

    override suspend fun rating(ratingBody: RatingBody): Response<RatingResponse> {
        return Response.success(ratingResponse)
    }

    override suspend fun transaction(): Response<TransactionResponse> {
        return Response.success(transactionResponse)
    }
}
