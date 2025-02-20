package com.luthfi.ecommerce.core.api

import com.luthfi.ecommerce.core.api.dummy.DummyResponses.fulfillmentResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.loginResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.paymentResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.productDetailResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.productsResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.profileResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.ratingResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.refreshResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.registerResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.reviewResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.searchResponseExpected
import com.luthfi.ecommerce.core.api.dummy.DummyResponses.transactionResponseExpected
import com.luthfi.ecommerce.core.data.datasource.api.ApiService
import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.body.RatingBody
import com.luthfi.ecommerce.core.data.datasource.api.body.RefreshBody
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun initiation() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun termination() {
        mockWebServer.shutdown()
    }

    private fun setJsonResponse(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path ?: "")
        return file.readText()
    }

    @Test
    fun `success login test`() = runTest {
        val loginBody = LoginBody()
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_login.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.login(loginBody).body()
        assertEquals(loginResponseExpected, actual)
    }

    @Test
    fun `success register test`() = runTest {
        val loginBody = LoginBody()
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_register.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.register(loginBody).body()
        assertEquals(registerResponseExpected, actual)
    }

    @Test
    fun `success refresh token test`() = runTest {
        val refreshBody = RefreshBody()
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_refresh.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.refreshToken(refreshBody).body()
        assertEquals(refreshResponseExpected, actual)
    }

    @Test
    fun `success profile test`() = runTest {
        val requestBody = "".toRequestBody("text/plain".toMediaType())
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_profile.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.profile(requestBody).body()
        assertEquals(profileResponseExpected, actual)
    }

    @Test
    fun `success product test`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_products.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.product(
            search = null,
            brand = null,
            lowest = null,
            highest = null,
            sort = null,
            limit = null,
            page = null
        ).body()
        assertEquals(productsResponseExpected, actual)
    }

    @Test
    fun `success search test`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_search.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.search(
            query = ""
        ).body()
        assertEquals(searchResponseExpected, actual)
    }

    @Test
    fun `success product detail test`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_product_detail.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.productDetail("").body()
        assertEquals(productDetailResponseExpected, actual)
    }

    @Test
    fun `success review test`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_review.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.productReview("").body()
        assertEquals(reviewResponseExpected, actual)
    }

    @Test
    fun `test payment success`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_payment.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.payment().body()
        assertEquals(paymentResponseExpected, actual)
    }

    @Test
    fun `success fulfillment test`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_fulfillment.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.fulfillment(
            fulfillmentBody = FulfillmentBody(
                payment = null,
                items = listOf()
            )
        ).body()
        assertEquals(fulfillmentResponseExpected, actual)
    }

    @Test
    fun `success rating test`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_rating.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.rating(
            ratingBody = RatingBody(
                invoiceId = null,
                rating = null,
                review = null
            )
        ).body()
        assertEquals(ratingResponseExpected, actual)
    }

    @Test
    fun `success transaction test`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(setJsonResponse("response_transaction.json"))
        mockWebServer.enqueue(mockResponse)
        val actual = apiService.transaction().body()
        assertEquals(transactionResponseExpected, actual)
    }
}
