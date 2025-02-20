package com.luthfi.ecommerce.core.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.luthfi.ecommerce.core.data.RepositoryImpl
import com.luthfi.ecommerce.core.data.datasource.api.ApiService
import com.luthfi.ecommerce.core.data.datasource.api.ProductPagingSource
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.body.RatingBody
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import com.luthfi.ecommerce.core.data.datasource.room.dao.ChartDao
import com.luthfi.ecommerce.core.data.datasource.room.dao.FavoriteDao
import com.luthfi.ecommerce.core.data.datasource.room.dao.NotificationDao
import com.luthfi.ecommerce.core.data.datasource.room.entity.toNotification
import com.luthfi.ecommerce.core.data.datasource.room.entity.toProducts
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.accessToken
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.cart
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.errorBody
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.favorite
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.fulfillmentResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.locale
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.loginResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.notification
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.paymentResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.product
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.productsResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.profileResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.promo
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.ratingResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.refreshToken
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.registerResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.reviewResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.searchResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.transactionResponse
import com.luthfi.ecommerce.core.repository.dummy.DummyRepository.username
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RepositoryImplTest {

    private lateinit var repository: Repository
    private val preference: UserPreferences = mock()
    private val apiService: ApiService = mock()
    private val cartDao: ChartDao = mock()
    private val favoriteDao: FavoriteDao = mock()
    private val notificationDao: NotificationDao = mock()

    @Before
    fun initiation() {
        repository = RepositoryImpl(
            preference,
            apiService,
            cartDao,
            favoriteDao,
            notificationDao
        )
    }

    // Preference
    @Test
    fun `test get refresh token`() = runTest {
        whenever(preference.getRefreshToken()).thenReturn(flowOf(refreshToken))
        val expectedData = refreshToken
        val actualData = repository.getRefreshToken().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set refresh token`() = runTest {
        whenever(preference.setRefreshToken("")).thenReturn(Unit)
        val actualData = repository.setRefreshToken(refreshToken)
        assertEquals(Unit, actualData)
    }

    @Test
    fun `test get locale`() = runTest {
        whenever(preference.getLocale()).thenReturn(flowOf(locale))
        val expectedData = locale
        val actualData = repository.getLocale().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set locale`() = runTest {
        whenever(preference.setLocale(locale)).thenReturn(Unit)
        val actualData = repository.setLocale(locale)
        assertEquals(Unit, actualData)
    }

    @Test
    fun `test get access token`() = runTest {
        whenever(preference.getAccessToken()).thenReturn(flowOf(accessToken))
        val expectedData = accessToken
        val actualData = repository.getAccessToken().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set access token`() = runTest {
        whenever(preference.setAccessToken(accessToken)).thenReturn(Unit)
        val actualData = repository.setAccessToken(accessToken)
        assertEquals(Unit, actualData)
    }

    @Test
    fun `test get is onboard`() = runTest {
        whenever(preference.getIsOnboard()).thenReturn(flowOf(false))
        val expectedData = false
        val actualData = repository.getIsOnboard().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set is onboard`() = runTest {
        whenever(preference.setIsOnBoard(true)).thenReturn(Unit)
        val actualData = repository.setIsOnBoard(true)
        assertEquals(Unit, actualData)
    }

    @Test
    fun `test get is light`() = runTest {
        whenever(preference.getIsLight()).thenReturn(flowOf(true))
        val expectedData = true
        val actualData = repository.getIsLight().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set is light`() = runTest {
        whenever(preference.setIsLight(true)).thenReturn(Unit)
        val actualData = repository.setIsLight(true)
        assertEquals(Unit, actualData)
    }

    @Test
    fun `test get username`() = runTest {
        whenever(preference.getUsername()).thenReturn(flowOf("id"))
        val expectedData = "id"
        val actualData = repository.getUsername().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set username`() = runTest {
        whenever(preference.setUsername(username)).thenReturn(Unit)
        val actualData = repository.setUsername(username)
        assertEquals(Unit, actualData)
    }

    @Test
    fun `test get is login`() = runTest {
        whenever(preference.getIsLogin()).thenReturn(flowOf(false))
        val expectedData = false
        val actualData = repository.getIsLogin().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set is login`() = runTest {
        whenever(preference.setIsLogin(true)).thenReturn(Unit)
        val actualData = repository.setIsLogin(true)
        assertEquals(Unit, actualData)
    }

    // Room
    @Test
    fun `test get all cart`() = runTest {
        whenever(cartDao.getAllChart()).thenReturn(flowOf(listOf(cart)))
        val expectedData = listOf(cart).toProducts()
        val actualData = repository.getAllChart().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test add cart increase count success`() = runTest {
        whenever(cartDao.getStockChart(cart.productId)).thenReturn(cart)
        val actualData = repository.addChart(product, true)
        val expectedData = "cartAdded"
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test add cart increase count full`() = runTest {
        whenever(cartDao.getStockChart(cart.productId)).thenReturn(cart.copy(count = 1, stock = 1))
        val actualData = repository.addChart(product, true)
        val expectedData = "cartFull"
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test add cart decrease count success`() = runTest {
        whenever(cartDao.getStockChart(cart.productId)).thenReturn(cart.copy(count = 5, stock = 10))
        val actualData = repository.addChart(product, false)
        val expectedData = "cartDecreased"
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test add cart decrease count minimum`() = runTest {
        whenever(cartDao.getStockChart(cart.productId)).thenReturn(cart.copy(count = 1, stock = 1))
        val actualData = repository.addChart(product, false)
        val expectedData = "cartMinimum"
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test add cart new item`() = runTest {
        whenever(cartDao.getStockChart(product.productId)).thenReturn(null)
        val actualData = repository.addChart(product, true)
        val expectedData = "cartAdded"
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test delete item cart`() = runTest {
        repository.deleteItemChart(product.productId)
        verify(cartDao).deleteItemChart(product.productId)
    }

    @Test
    fun `test delete checked cart`() = runTest {
        repository.deleteCheckedChart()
        verify(cartDao).deleteCheckedChart()
    }

    @Test
    fun `test update check all cart`() = runTest {
        repository.updateCheckAllChart(true)
        verify(cartDao).updateCheckAllChart(true)
    }

    @Test
    fun `test update is checked cart`() = runTest {
        repository.updateIsCheckedChart(product.productId, true)
        verify(cartDao).updateIsCheckedChart(product.productId, true)
    }

    @Test
    fun `test get all fav`() = runTest {
        whenever(favoriteDao.getAllFav()).thenReturn(flowOf(listOf(favorite)))
        val expectedData = listOf(favorite).toProducts()
        val actualData = repository.getAllFav().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test insert fav`() = runTest {
        repository.insertFav(product)
        verify(favoriteDao).insertFav(favorite)
    }

    @Test
    fun `test delete item fav`() = runTest {
        repository.deleteItemFav(product.productId)
        verify(favoriteDao).deleteItemFav(product.productId)
    }

    @Test
    fun `test get is fav`() = runTest {
        repository.getIsFav(product.productId)
        verify(favoriteDao).getIsFav(product.productId)
    }

    @Test
    fun `test get all notification`() = runTest {
        whenever(notificationDao.getAllNotification()).thenReturn(flowOf(listOf(notification)))
        val expectedData = listOf(notification).toNotification()
        val actualData = repository.getAllNotification().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test insert notification`() = runTest {
        repository.insertNotification(promo)
        verify(notificationDao).insertNotification(notification.copy(id = 0))
    }

    @Test
    fun `test update check notification`() = runTest {
        repository.updateCheckNotification(notification.id, true)
        verify(notificationDao).updateIsCheckedNotification(notification.id, true)
    }

    // Api
    @Test
    fun `test login success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.login(LoginBody())).thenReturn(Response.success(loginResponse))
        val actualData = repository.login(LoginBody()).first()
        val expectedData = Resource.Success(loginResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.login(LoginBody())).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.login(LoginBody()).first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.login(LoginBody())).thenThrow(timeout)
        val actualData = repository.login(LoginBody()).first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test register success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.register(LoginBody())).thenReturn(Response.success(registerResponse))
        val actualData = repository.register(LoginBody()).first()
        val expectedData = Resource.Success(registerResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test register failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.register(LoginBody())).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.register(LoginBody()).first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test register fail timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.register(LoginBody())).thenThrow(timeout)
        val actualData = repository.register(LoginBody()).first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test profile success`() = runTest(UnconfinedTestDispatcher()) {
        val reqBody = username.toRequestBody("text/plain".toMediaType())
        whenever(apiService.profile(reqBody)).thenReturn(Response.success(profileResponse))
        val actualData = repository.profile(reqBody, null).first()
        val expectedData = Resource.Success(profileResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test profile failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val reqBody = username.toRequestBody("text/plain".toMediaType())
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.profile(reqBody)).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.profile(reqBody, null).first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test profile fail timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        val reqBody = username.toRequestBody("text/plain".toMediaType())
        whenever(apiService.profile(reqBody)).thenThrow(timeout)
        val actualData = repository.profile(reqBody, null).first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test product paging data success`() = runTest {
        val fakeApiService = FakeApiService()
        val pagingSource = ProductPagingSource(fakeApiService, null)
        val config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 1,
            initialLoadSize = 10
        )
        val pager = TestPager(config, pagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertEquals(result.data, productsResponse.data.items)
    }

    @Test
    fun `test product paging consecutive load`() = runTest {
        val fakeApiService = FakeApiService()
        val pagingSource = ProductPagingSource(fakeApiService, null)
        val config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 1,
            initialLoadSize = 10
        )
        val pager = TestPager(config, pagingSource)
        val page = with(pager) {
            refresh()
            append()
            append()
        } as PagingSource.LoadResult.Page
        assertEquals(page.data, productsResponse.data.items)
    }

    @Test
    fun `test search success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.search("")).thenReturn(Response.success(searchResponse))
        val actualData = repository.search("").first()
        val expectedData = Resource.Success(searchResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test search failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.search("")).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.search("").first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test search failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.search("")).thenThrow(timeout)
        val actualData = repository.search("").first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test product detail success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.search("")).thenReturn(Response.success(searchResponse))
        val actualData = repository.search("").first()
        val expectedData = Resource.Success(searchResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test product detail failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.search("")).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.search("").first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test product detail failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.search("")).thenThrow(timeout)
        val actualData = repository.search("").first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test review success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.productReview("")).thenReturn(Response.success(reviewResponse))
        val actualData = repository.productReview("").first()
        val expectedData = Resource.Success(reviewResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test review failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.productReview("")).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.productReview("").first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test refresh token failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.productReview("")).thenThrow(timeout)
        val actualData = repository.productReview("").first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test payment success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.payment()).thenReturn(Response.success(paymentResponse))
        val actualData = repository.payment().first()
        val expectedData = Resource.Success(paymentResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test payment failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.payment()).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.payment().first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test payment failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.payment()).thenThrow(timeout)
        val actualData = repository.payment().first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test fulfillment success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.fulfillment(FulfillmentBody(null, null))).thenReturn(
            Response.success(
                fulfillmentResponse
            )
        )
        val actualData = repository.fulfillment(FulfillmentBody(null, null)).first()
        val expectedData = Resource.Success(fulfillmentResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test fulfillment failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.fulfillment(FulfillmentBody(null, null))).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.fulfillment(FulfillmentBody(null, null)).first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test fulfillment failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.fulfillment(FulfillmentBody(null, null))).thenThrow(timeout)
        val actualData = repository.fulfillment(FulfillmentBody(null, null)).first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test rating success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.rating(RatingBody(null, null, null))).thenReturn(
            Response.success(
                ratingResponse
            )
        )
        val actualData = repository.rating(RatingBody(null, null, null)).first()
        val expectedData = Resource.Success(ratingResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test rating failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.rating(RatingBody(null, null, null))).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.rating(RatingBody(null, null, null)).first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test rating failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.rating(RatingBody(null, null, null))).thenThrow(timeout)
        val actualData = repository.rating(RatingBody(null, null, null)).first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test transaction success`() = runTest(UnconfinedTestDispatcher()) {
        whenever(apiService.transaction()).thenReturn(Response.success(transactionResponse))
        val actualData = repository.transaction().first()
        val expectedData = Resource.Success(transactionResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test transaction failed from server`() = runTest(UnconfinedTestDispatcher()) {
        val mediaType = MEDIA_TYPE.toMediaTypeOrNull()
        val responseBody: ResponseBody = errorBody.toResponseBody(mediaType)
        whenever(apiService.transaction()).thenReturn(
            Response.error(
                INTERNAL_SERVER_ERROR_CODE,
                responseBody
            )
        )
        val actualData = repository.transaction().first()
        val expectedData = Resource.Error(errorBody)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test transaction failed timeout`() = runTest(UnconfinedTestDispatcher()) {
        val timeout = RuntimeException(RUNTIME_EXCEPTION_TIMEOUT)
        whenever(apiService.transaction()).thenThrow(timeout)
        val actualData = repository.transaction().first()
        val expectedData = Resource.Error(RESOURCE_ERROR_TIMEOUT)
        assertEquals(expectedData, actualData)
    }

    companion object {
        const val INTERNAL_SERVER_ERROR_CODE = 500
        const val MEDIA_TYPE = "application/json"
        const val RUNTIME_EXCEPTION_TIMEOUT = "timeout"
        const val RESOURCE_ERROR_TIMEOUT = "Timeout"
    }
}
