package com.luthfi.ecommerce.core.api.dummy

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

object DummyResponses {
    val loginResponseExpected = LoginResponse(
        code = 200,
        data =
        LoginResponse.Data(
            userImage = "",
            userName = "",
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDE4OTV9.AceVKZlMeFFvwNPAC5Opc6mSxhAXWz1CSf4E2FipZsJkPfaFt021Yi3TpG08ENUashUwJX-YLCuIolqnb7EulA",
            expiresAt = 600,
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoicmVmcmVzaFRva2VuIiwiZXhwIjoxNjg1MzQ0ODk1fQ.tB4EeMvkfJAV_kSwakcEujsJEqNtlvKaBbz6ga58lMw6R1NKNOSi6iy3Qn-dFtHGMkzwqpokY3uOdQYcVtahCA",
        ),
        message = "OK"
    )

    val registerResponseExpected = RegisterResponse(
        code = 200,
        data =
        RegisterResponse.Data(
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDE1MjB9.ldL_6Qoo-MfMmwHrhxXUv670Uz6j0CCF9t9I8uOmW_LuAUTzCWhjMcQelP8MjfnVDqKSZj2LaqHv3TY08AB7TQ",
            expiresAt = 600,
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDQ1MjB9.HeeNuQww-w2tb3pffNC43BCmMCcE3rOj-yL7-pTGOEcIcoFCv2n9IEWS0gqxNnDaNf3sXBm7JHCxFexB5FGRgQ"
        ),
        message = "OK"
    )

    val refreshResponseExpected = RefreshResponse(
        code = 200,
        data = RefreshResponse.Data(
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDIwMjN9.g4y-WkXHsk6gTxb72-L2Kk2Wv7dZ438zWZIfJ1Z9bER2Ob3ULnuo2ExBzq5S5l6eJ85PUYOeuiCUCeBRZ94RQQ",
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDUwMjN9.U3FQQCGsyBCWE5qUOkWjneI_igtUj9bDKvJI-25o-8a6NMekmvvdlzjJVvK2Yyed9IpAaGTMXNgeQsl9M04uDA",
            expiresAt = 600
        ),
        message = "OK"
    )

    val profileResponseExpected = ProfileResponse(
        code = 200,
        data = ProfileResponse.Data(
            userName = "Test",
            userImage = "1d32ba79-e879-4425-a011-2da4281f1c1b-test.png"
        ),
        message = "OK"
    )

    val productsResponseExpected = ProductsResponse(
        code = 200,
        data =
        ProductsResponse.Data(
            currentItemCount = 10,
            items = listOf(
                ProductsResponse.Data.Item(
                    productId = "601bb59a-4170-4b0a-bd96-f34538922c7c",
                    productName = "Lenovo Legion 3",
                    productPrice = 10000000,
                    image = "image1",
                    brand = "Lenovo",
                    store = "LenovoStore",
                    sale = 2,
                    productRating = 4.0
                )
            ),
            itemsPerPage = 10,
            pageIndex = 1,
            totalPages = 3
        ),
        message = "OK"
    )

    val searchResponseExpected = SearchResponse(
        code = 200,
        data = listOf(
            "Lenovo Legion 3",
            "Lenovo Legion 5",
            "Lenovo Legion 7",
            "Lenovo Ideapad 3",
            "Lenovo Ideapad 5",
            "Lenovo Ideapad 7"
        ),
        message = "OK"
    )

    val productDetailResponseExpected = ProductResponse(
        code = 200,
        data =
        ProductResponse.Data(
            brand = "Asus",
            description = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray [AMD Ryzen™ 7 6800H / NVIDIA® GeForce RTX™ 3060 / 8G*2 / 512GB / 17.3inch / WIN11 / OHS]\n" +
                "\n" +
                "CPU : AMD Ryzen™ 7 6800H Mobile Processor (8-core/16-thread, 20MB cache, up to 4.7 GHz max boost)\n" +
                "GPU : NVIDIA® GeForce RTX™ 3060 Laptop GPU\n" +
                "Graphics Memory : 6GB GDDR6\n" +
                "Discrete/Optimus : MUX Switch + Optimus\n" +
                "TGP ROG Boost : 1752MHz* at 140W (1702MHz Boost Clock+50MHz OC, 115W+25W Dynamic Boost)\n" +
                "Panel : 17.3-inch FHD (1920 x 1080) 16:9 360Hz IPS-level 300nits sRGB % 100.00%",
            image = listOf(
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg"
            ),
            productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
            productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
            productPrice = 24499000,
            productRating = 5.0,
            productVariant = listOf(
                ProductResponse.Data.ProductVariant(
                    variantName = "RAM 16GB",
                    variantPrice = 0
                ),
                ProductResponse.Data.ProductVariant(
                    variantName = "RAM 32GB",
                    variantPrice = 1000000
                )
            ),
            sale = 12,
            stock = 2,
            store = "AsusStore",
            totalRating = 7,
            totalReview = 5,
            totalSatisfaction = 100
        ),
        message = "OK"
    )

    val reviewResponseExpected = ReviewResponse(
        code = 200,
        data = listOf(
            ReviewResponse.Data(
                userName = "John",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
                userRating = 4,
                userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            )
        ),
        message = "OK"
    )

    val fulfillmentResponseExpected = FulfillmentResponse(
        code = 200,
        data =
        FulfillmentResponse.Data(
            invoiceId = "ba47402c-d263-49d3-a1f8-759ae59fa4a1",
            status = true,
            date = "09 Jun 2023",
            time = "08:53",
            payment = "Bank BCA",
            total = 48998000
        ),
        message = "OK"
    )

    val ratingResponseExpected =
        RatingResponse(code = 200, message = "Fulfillment rating and review success")

    val transactionResponseExpected = TransactionResponse(
        code = 200,
        data = listOf(
            TransactionResponse.Data(
                invoiceId = "8cad85b1-a28f-42d8-9479-72ce4b7f3c7d",
                status = true,
                date = "09 Jun 2023",
                time = "09:05",
                payment = "Bank BCA",
                total = 48998000,
                items = listOf(
                    TransactionResponse.Data.Item(
                        productId = "bee98108-660c-4ac0-97d3-63cdc1492f53",
                        variantName = "RAM 16GB",
                        quantity = 2
                    )
                ),
                rating = 4,
                review = "LGTM",
                image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                name = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray"
            )
        ),
        message = "OK"
    )

    val paymentResponseExpected = PaymentResponse(
        code = 200,
        data = listOf(
            PaymentResponse.Data(
                title = "Transfer Virtual Account",
                item = listOf(
                    PaymentResponse.Data.Item(
                        label = "BCA Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
                        status = true
                    ),
                    PaymentResponse.Data.Item(
                        label = "BNI Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/id/thumb/5/55/BNI_logo.svg/1200px-BNI_logo.svg.png",
                        status = true
                    ),
                    PaymentResponse.Data.Item(
                        label = "BRI Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/BANK_BRI_logo.svg/1200px-BANK_BRI_logo.svg.png",
                        status = true
                    ),
                    PaymentResponse.Data.Item(
                        label = "Mandiri Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Bank_Mandiri_logo_2016.svg/2560px-Bank_Mandiri_logo_2016.svg.png",
                        status = true
                    )
                )
            ),
            PaymentResponse.Data(
                title = "Transfer Bank",
                item = listOf(
                    PaymentResponse.Data.Item(
                        label = "Bank BCA",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
                        status = true
                    ),
                    PaymentResponse.Data.Item(
                        label = "Bank BNI",
                        image = "https://upload.wikimedia.org/wikipedia/id/thumb/5/55/BNI_logo.svg/1200px-BNI_logo.svg.png",
                        status = true
                    ),
                    PaymentResponse.Data.Item(
                        label = "Bank BRI",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/BANK_BRI_logo.svg/1200px-BANK_BRI_logo.svg.png",
                        status = true
                    ),
                    PaymentResponse.Data.Item(
                        label = "Bank Mandiri",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Bank_Mandiri_logo_2016.svg/2560px-Bank_Mandiri_logo_2016.svg.png",
                        status = true
                    )
                )
            ),
            PaymentResponse.Data(
                title = "Pembayaran Instan",
                item = listOf(
                    PaymentResponse.Data.Item(
                        label = "GoPay",
                        image = "https://gopay.co.id/icon.png",
                        status = true
                    ),
                    PaymentResponse.Data.Item(
                        label = "OVO",
                        image = "https://theme.zdassets.com/theme_assets/1379487/2cb35fe96fa1191f49c2b769b50cf8b546fff65e.png",
                        status = true
                    )
                )
            )
        ),
        message = "OK"
    )
}
