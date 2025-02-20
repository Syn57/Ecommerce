package com.luthfi.ecommerce.core.room.dummy

import com.luthfi.ecommerce.core.data.datasource.room.entity.ChartEntity
import com.luthfi.ecommerce.core.data.datasource.room.entity.FavoriteEntity
import com.luthfi.ecommerce.core.data.datasource.room.entity.NotificationEntity

object DummyEntities {
    val cart = ChartEntity(
        productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
        brand = "Asus",
        description = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray [AMD Ryzen™ 7 6800H / NVIDIA® GeForce RTX™ 3060 / 8G*2 / 512GB / 17.3inch / WIN11 / OHS]\\n\\nCPU : AMD Ryzen™ 7 6800H Mobile Processor (8-core/16-thread, 20MB cache, up to 4.7 GHz max boost)\\nGPU : NVIDIA® GeForce RTX™ 3060 Laptop GPU\\nGraphics Memory : 6GB GDDR6\\nDiscrete/Optimus : MUX Switch + Optimus\\nTGP ROG Boost : 1752MHz* at 140W (1702MHz Boost Clock+50MHz OC, 115W+25W Dynamic Boost)\\nPanel : 17.3-inch FHD (1920 x 1080) 16:9 360Hz IPS-level 300nits sRGB % 100.00%",
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
        productPrice = 24499000,
        productRating = 5.0,
        variantName = "RAM 16GB",
        variantPrice = 0,
        sale = 12,
        stock = 2,
        store = "AsusStore",
        totalRating = 7,
        totalReview = 5,
        totalSatisfaction = 100,
        isChecked = false,
        count = 1
    )

    val favorite = FavoriteEntity(
        productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
        brand = "Asus",
        description = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray [AMD Ryzen™ 7 6800H / NVIDIA® GeForce RTX™ 3060 / 8G*2 / 512GB / 17.3inch / WIN11 / OHS]\\n\\nCPU : AMD Ryzen™ 7 6800H Mobile Processor (8-core/16-thread, 20MB cache, up to 4.7 GHz max boost)\\nGPU : NVIDIA® GeForce RTX™ 3060 Laptop GPU\\nGraphics Memory : 6GB GDDR6\\nDiscrete/Optimus : MUX Switch + Optimus\\nTGP ROG Boost : 1752MHz* at 140W (1702MHz Boost Clock+50MHz OC, 115W+25W Dynamic Boost)\\nPanel : 17.3-inch FHD (1920 x 1080) 16:9 360Hz IPS-level 300nits sRGB % 100.00%",
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
        productPrice = 24499000,
        productRating = 5.0,
        variantName = "RAM 16GB",
        variantPrice = 0,
        sale = 12,
        stock = 2,
        store = "AsusStore",
        totalRating = 7,
        totalReview = 5,
        totalSatisfaction = 100
    )

    val notification = NotificationEntity(
        id = 1,
        title = "My Award for Luthfi",
        body = "Nikmati kemeriahan ulang tahun Telkomsel pada hari Jumat, 21 Juli 2023 pukul 19.00 – 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
        image = "https://www.telkomsel.com/sites/default/files/banner/hero-10/2023-07/telkomsel-awards-2023--HERO3.png",
        type = "Promo",
        date = "20 November 2023",
        time = "18:34"
    )
}
