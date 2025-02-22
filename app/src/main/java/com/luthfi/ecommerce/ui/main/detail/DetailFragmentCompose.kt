package com.luthfi.ecommerce.ui.main.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.gson.Gson
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.toProduct
import com.luthfi.ecommerce.ui.base.BaseFragment
import com.luthfi.ecommerce.utils.format
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalGlideComposeApi::class,
    ExperimentalPagerApi::class
)
class DetailFragmentCompose : BaseFragment() {

    private val viewModel: DetailViewModel by viewModel()
    private val analytics: FirebaseAnalytics by inject()
    private lateinit var composeView: ComposeView
    private lateinit var snackbarHostState: SnackbarHostState
    private lateinit var scope: CoroutineScope
    private lateinit var productVariantPrice: MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val variantArgs = DetailFragmentComposeArgs.fromBundle(requireArguments()).variantName
        if (viewModel.variantFlagArgs == 0){
            viewModel.variantArg = variantArgs
            viewModel.variantFlagArgs = 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = DetailFragmentComposeArgs.fromBundle(requireArguments()).productId
        viewModel.saveId(productId)
        composeView.setContent {
            val colorScheme =
                if (isSystemInDarkTheme()) {
                    darkColorScheme()
                } else {
                    lightColorScheme().copy(
                        surface = Color.White,
                        surfaceTint = Color.White,
                        background = Color.White
                    )
                }

            MaterialTheme(
                colorScheme = colorScheme
            ) {
                productVariantPrice = remember { mutableStateOf(viewModel.variantArg) }
                DetailScreen()
            }
        }
    }

    private fun shareIntent(product: ProductResponse.Data) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "${product.productName}\n${
                    format(
                        product.productPrice
                    )
                }\nLink: www.tokopaerbe.com/${product.productId} "
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun DetailScreen() {
        val response by viewModel.productResponse2.collectAsState()
        scope = rememberCoroutineScope()
        snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.detail),
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontFamily = FontFamily(
                                        listOf(
                                            Font(R.font.poppins_400)
                                        )
                                    )
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { findNavController().navigateUp() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = stringResource(id = R.string.review)
                                )
                            }
                        }
                    )
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            },
            bottomBar = {
                if (response is Resource.Success) {
                    Column {
                        Divider(color = Color.Gray, thickness = 1.dp)
                        BottomAppBar(
                            content = {
                                ConstraintLayout(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    val (buy, cart) = createRefs()
                                    OutlinedButton(
                                        onClick = {
                                            val product = viewModel.product
                                            product.count = 1
                                            viewModel.checkoutItems.clear()
                                            viewModel.checkoutItems.add(product)
                                            val args =
                                                DetailFragmentComposeDirections.actionDetailFragmentToCheckoutFragment(
                                                    viewModel.checkoutItems.toTypedArray()
                                                )
                                            analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
                                                val bs = Bundle()
                                                bs.putString(
                                                    FirebaseAnalytics.Param.ITEM_NAME,
                                                    viewModel.product.productName
                                                )
                                                bs.putString(
                                                    FirebaseAnalytics.Param.ITEM_BRAND,
                                                    viewModel.product.brand
                                                )
                                                param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
                                                param(
                                                    FirebaseAnalytics.Param.CURRENCY,
                                                    INDONESIA_CURRENCY
                                                )
                                                param(
                                                    FirebaseAnalytics.Param.VALUE,
                                                    (viewModel.product.productPrice + viewModel.product.variantPrice).toDouble()
                                                )
                                            }
                                            findNavController().navigate(args)
                                        },
                                        modifier = Modifier.constrainAs(buy) {
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                            start.linkTo(parent.start, 16.dp)
                                            end.linkTo(cart.start, 16.dp)
                                            width = Dimension.fillToConstraints
                                        }
                                    ) {
                                        Text(
                                            text = getString(R.string.buy),
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(
                                                    listOf(
                                                        Font(R.font.poppins_500)
                                                    )
                                                )
                                            )
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            scope.launch(Dispatchers.IO) {
                                                val message =
                                                    viewModel.addChart(viewModel.product, true)
                                                if (message == CART_ADDED) {
                                                    snackbarHostState.showSnackbar(
                                                        message = getString(
                                                            R.string.success_added_to_chart
                                                        ),
                                                        duration = SnackbarDuration.Short
                                                    )
                                                } else {
                                                    snackbarHostState.showSnackbar(
                                                        message = getString(
                                                            R.string.out_of_stock
                                                        ),
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                                analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
                                                    val bs = Bundle()
                                                    bs.putString(
                                                        FirebaseAnalytics.Param.ITEM_NAME,
                                                        viewModel.product.productName
                                                    )
                                                    bs.putString(
                                                        FirebaseAnalytics.Param.ITEM_BRAND,
                                                        viewModel.product.brand
                                                    )
                                                    param(
                                                        FirebaseAnalytics.Param.ITEMS,
                                                        arrayOf(bs)
                                                    )
                                                    param(
                                                        FirebaseAnalytics.Param.CURRENCY,
                                                        DetailFragment.INDONESIA_CURRENCY
                                                    )
                                                    param(
                                                        FirebaseAnalytics.Param.VALUE,
                                                        (viewModel.product.productPrice + viewModel.product.variantPrice).toDouble()
                                                    )
                                                }
                                            }
                                        },
                                        modifier = Modifier.constrainAs(cart) {
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                            end.linkTo(parent.end, 16.dp)
                                            start.linkTo(buy.end)
                                            width = Dimension.fillToConstraints
                                        }
                                    ) {
                                        Text(
                                            text = getString(R.string.add_chart),
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(
                                                    listOf(
                                                        Font(R.font.poppins_500)
                                                    )
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            ) {
                if (response != null) {
                    when (response) {
                        is Resource.Success -> {
                            val data = response as Resource.Success<ProductResponse>
                            viewModel.product = data.data.toProduct()
                            if (productVariantPrice.value == "RAM 16GB") {
                                viewModel.variant.postValue(0)
                                viewModel.product.variantName = "RAM 16GB"
                                viewModel.product.variantPrice = 0
                            } else {
                                viewModel.variant.postValue(1)
                                viewModel.product.variantName = "RAM 32GB"
                                viewModel.product.variantPrice = 1_000_000
                            }
                            analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
                                param(
                                    FirebaseAnalytics.Param.CURRENCY,
                                    DetailFragment.INDONESIA_CURRENCY
                                )
                                param(
                                    FirebaseAnalytics.Param.VALUE,
                                    data.data.data.productPrice.toDouble()
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                ImageScreen(data.data)
                                PriceScreen(data.data)
                                ChipScreen(data.data)
                                DescriptionScreen(data.data)
                                ReviewScreen(data.data)
                            }
                        }

                        is Resource.Loading -> {
                            LoadingScreen()
                        }

                        is Resource.Error -> {
                            val data = response as Resource.Error
                            ErrorScreen(data.error)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    @Composable
    fun ReviewScreen(data: ProductResponse) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (title, btn, star, ratingNum, per5, satisfaction, detail, spacer) = createRefs()
            Text(
                text = getString(R.string.buyer_reviews),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_600)
                        )
                    )
                ),
                modifier = Modifier
                    .constrainAs(title) { top.linkTo(parent.top, margin = 12.dp) }
                    .padding(start = 16.dp)
            )
            TextButton(
                onClick = {
                    val navToDetail =
                        DetailFragmentComposeDirections.actionDetailFragmentToReviewFragment(data.data.productId)
                    findNavController().navigate(navToDetail)
                },
                modifier = Modifier.constrainAs(btn) { end.linkTo(parent.end, margin = 16.dp) }
            ) {
                Text(
                    text = getString(R.string.see_all),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(
                            listOf(
                                Font(R.font.poppins_500)
                            )
                        )
                    )
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(star) {
                        top.linkTo(satisfaction.top)
                        start.linkTo(parent.start, 16.dp)
                        bottom.linkTo(detail.bottom)
                    }
            )
            Text(
                text = data.data.productRating.toString(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_600)
                        )
                    )
                ),
                modifier = Modifier.constrainAs(ratingNum) {
                    start.linkTo(star.end, margin = 4.dp)
                    top.linkTo(star.top, 2.dp)
                    bottom.linkTo(star.bottom)
                }
            )
            Text(
                text = "/5.0",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_400)
                        )
                    )
                ),
                modifier = Modifier.constrainAs(per5) {
                    start.linkTo(ratingNum.end)
                    bottom.linkTo(ratingNum.bottom, margin = 6.dp)
                }
            )
            Text(
                text = getString(R.string.buyers_are_satisfied, data.data.totalSatisfaction),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_600)
                        )
                    )
                ),
                modifier = Modifier.constrainAs(satisfaction) {
                    start.linkTo(per5.end, margin = 32.dp)
                    top.linkTo(title.bottom, margin = 8.dp)
                }
            )
            Text(
                text = getString(
                    R.string.rate_review_num,
                    data.data.totalRating,
                    data.data.totalReview
                ),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_400)
                        )
                    )
                ),
                modifier = Modifier.constrainAs(detail) {
                    start.linkTo(satisfaction.start)
                    top.linkTo(satisfaction.bottom)
                }
            )
            Spacer(
                modifier = Modifier
                    .height(18.dp)
                    .constrainAs(spacer) { top.linkTo(detail.bottom) }
            )
        }
    }

    @Composable
    fun DescriptionScreen(data: ProductResponse) {
        ConstraintLayout {
            val (title, desc, dvdr) = createRefs()
            Text(
                text = getString(R.string.product_description),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_600)
                        )
                    )
                ),
                modifier = Modifier
                    .constrainAs(title) { top.linkTo(parent.top, margin = 12.dp) }
                    .padding(start = 16.dp)
            )
            Text(
                text = data.data.description,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_400)
                        )
                    )
                ),
                modifier = Modifier
                    .constrainAs(desc) { top.linkTo(title.bottom, margin = 8.dp) }
                    .padding(start = 16.dp)
            )

            Divider(
                color = Color(0xFFCAC4D0),
                modifier = Modifier.constrainAs(dvdr) {
                    top.linkTo(desc.bottom, margin = 8.dp)
                }
            )
        }
    }

    @Composable
    fun ImageScreen(
        data: ProductResponse
    ) {
        ConstraintLayout {
            val pagerState by remember { mutableStateOf(PagerState(0)) }
            val slideImage = remember { mutableStateOf("") }
            val (image, dots) = createRefs()
            HorizontalPager(
                state = pagerState,
                count = data.data.image.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                    }
            ) { page ->
                slideImage.value = data.data.image[page]
                GlideImage(
                    model = slideImage.value,
                    loading = placeholder(R.drawable.thumbnail_load_product),
                    failure = placeholder(R.drawable.thumbnail_load_product),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(309.dp)
                        .width(IntrinsicSize.Max)
                )
            }
            if (data.data.image.size > 1) {
                DotsIndicator(
                    totalDots = data.data.image.size,
                    selectedIndex = pagerState.currentPage,
                    selectedColor = Color(0xFF6750A4),
                    unSelectedColor = Color(0xFFCAC4D0),
                    modifier = Modifier.constrainAs(dots) {
                        bottom.linkTo(image.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        }
    }

    @Composable
    fun DotsIndicator(
        totalDots: Int,
        selectedIndex: Int,
        selectedColor: Color,
        unSelectedColor: Color,
        modifier: Modifier
    ) {
        LazyRow(
            modifier = modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(totalDots) { index ->
                if (index == selectedIndex) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(selectedColor)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(unSelectedColor)
                    )
                }

                if (index != totalDots - 1) {
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }
    }

    @Composable
    fun PriceScreen(
        data: ProductResponse
    ) {
        Column {
            ConstraintLayout {
                val (priceRow, titleRow, chipRow, dvdr) = createRefs()
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(priceRow) {}
                ) {
                    var variant by remember {
                        mutableIntStateOf(0)
                    }
                    viewModel.variant.observe(viewLifecycleOwner) {
                        variant = it
                    }
                    ConstraintLayout {
                        val (price, share, fav) = createRefs()
                        Text(
                            text = format(data.data.productVariant[variant].variantPrice + data.data.productPrice),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(
                                    listOf(
                                        Font(R.font.poppins_600)
                                    )
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(price) {
                                    top.linkTo(parent.top, margin = 14.dp)
                                }
                        )

//                        AndroidView(
//                            factory = { context ->
//                                LikeButton(context).apply {
//                                    viewModel.getIsFav(data.data.productId).observe(viewLifecycleOwner) {
//                                        viewModel.isFav = it
//                                        when (it) {
//                                            true -> this.isLiked = true
//                                            false -> this.isLiked = false
//                                        }
//                                    }
//                                    this.isEnabled = true
//                                    setLikeDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite_red, context.theme))
//                                    setUnlikeDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite_border, context.theme))
//                                    setIconSizeDp(24);
//                                    setOnLikeListener(object: OnLikeListener{
//                                        override fun liked(likeButton: LikeButton?) {
//                                            viewModel.isFav = !viewModel.isFav
//                                            viewModel.insertFav(viewModel.product)
//                                            scope.launch {
//                                                snackbarHostState.showSnackbar(
//                                                    message = getString(R.string.success_added_to_wishlist),
//                                                    duration = SnackbarDuration.Short
//                                                )
//                                            }
//                                            analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
//                                                val bs = Bundle()
//                                                bs.putString(
//                                                    FirebaseAnalytics.Param.ITEM_NAME,
//                                                    viewModel.product.productName
//                                                )
//                                                bs.putString(
//                                                    FirebaseAnalytics.Param.ITEM_BRAND,
//                                                    viewModel.product.brand
//                                                )
//                                                param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
//                                                param(FirebaseAnalytics.Param.CURRENCY, INDONESIA_CURRENCY)
//                                                param(
//                                                    FirebaseAnalytics.Param.VALUE,
//                                                    (viewModel.product.productPrice + viewModel.product.variantPrice).toDouble()
//                                                )
//                                            }
//                                        }
//
//                                        override fun unLiked(likeButton: LikeButton?) {
//                                            viewModel.isFav = !viewModel.isFav
//                                            viewModel.deleteFav(viewModel.product.productId)
//                                            scope.launch {
//                                                snackbarHostState.showSnackbar(
//                                                    message = getString(R.string.removed_from_wishlist),
//                                                    duration = SnackbarDuration.Short
//                                                )
//                                            }
//                                        }
//                                    })
//                                }
//                            },
//                            modifier = Modifier
//                                .size(24.dp)
//                                .constrainAs(fav) {
//                                    end.linkTo(parent.end)
//                                    top.linkTo(parent.top, margin = 14.dp)
//                                },
//
//                        )

//                        IconButton(
//                            modifier = Modifier
//                                .size(24.dp)
//                                .constrainAs(fav) {
//                                    end.linkTo(parent.end)
//                                    top.linkTo(parent.top, margin = 14.dp)
//                                },
//                            onClick = {
//                                viewModel.isFav = !viewModel.isFav
//                                if (viewModel.isFav) {
//                                    viewModel.insertFav(viewModel.product)
//                                    scope.launch {
//                                        snackbarHostState.showSnackbar(
//                                            message = getString(R.string.success_added_to_wishlist),
//                                            duration = SnackbarDuration.Short
//                                        )
//                                    }
//                                    analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
//                                        val bs = Bundle()
//                                        bs.putString(
//                                            FirebaseAnalytics.Param.ITEM_NAME,
//                                            viewModel.product.productName
//                                        )
//                                        bs.putString(
//                                            FirebaseAnalytics.Param.ITEM_BRAND,
//                                            viewModel.product.brand
//                                        )
//                                        param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
//                                        param(FirebaseAnalytics.Param.CURRENCY, INDONESIA_CURRENCY)
//                                        param(
//                                            FirebaseAnalytics.Param.VALUE,
//                                            (viewModel.product.productPrice + viewModel.product.variantPrice).toDouble()
//                                        )
//                                    }
//                                } else {
//                                    viewModel.deleteFav(viewModel.product.productId)
//                                    scope.launch {
//                                        snackbarHostState.showSnackbar(
//                                            message = getString(R.string.removed_from_wishlist),
//                                            duration = SnackbarDuration.Short
//                                        )
//                                    }
//                                }
//                            }
//                        ) {
//                            var favBg by remember {
//                                mutableIntStateOf(R.drawable.ic_favorite_border)
//                            }
//                            viewModel.getIsFav(data.data.productId).observe(viewLifecycleOwner) {
//                                viewModel.isFav = it
//                                favBg = when (it) {
//                                    true -> R.drawable.ic_favorite_red
//                                    false -> R.drawable.ic_favorite_border
//                                }
//                            }
//                            Icon(
//                                painter = painterResource(id = favBg),
//                                contentDescription = "",
//                                tint = Color.Unspecified
//                            )
//                        }

                        IconButton(
                            modifier = Modifier
                                .size(24.dp)
                                .constrainAs(share) {
                                    end.linkTo(fav.start, margin = 16.dp)
                                    top.linkTo(parent.top, margin = 14.dp)
                                },
                            onClick = { shareIntent(data.data) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = ""
                            )
                        }
                    }
                }
                Text(
                    text = data.data.productName,
                    maxLines = 2,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            listOf(
                                Font(R.font.poppins_400)
                            )
                        )
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(titleRow) { top.linkTo(priceRow.bottom, margin = 8.dp) }
                )
                Row(
                    modifier = Modifier.constrainAs(chipRow) {
                        top.linkTo(
                            titleRow.bottom,
                            margin = 8.dp
                        )
                    }
                ) {
                    ConstraintLayout {
                        val (sold, chip) = createRefs()
                        Text(
                            text = getString(R.string.sold, data.data.sale),
                            modifier = Modifier
                                .padding(start = 16.dp, end = 8.dp)
                                .constrainAs(sold) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(
                                    listOf(
                                        Font(R.font.poppins_400)
                                    )
                                )
                            )
                        )
                        ConstraintLayout(
                            modifier = Modifier
                                .paint(
                                    painter = painterResource(id = R.drawable.bgratepng),
                                    contentScale = ContentScale.Fit
                                )
                                .padding(end = 8.dp, start = 4.dp, top = 2.dp, bottom = 2.dp)
                                .constrainAs(chip) {
                                    start.linkTo(sold.end, margin = 4.dp)
                                },
                        ) {
                            val (image, text) = createRefs()
                            Image(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(15.dp)
                                    .constrainAs(image) {
                                        start.linkTo(parent.start)
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        end.linkTo(text.start)
                                    }
                            )
                            Text(
                                text = "${data.data.productRating} (${data.data.totalReview})"
//                                    +getString(
//                                    R.string.rate_review,
//                                    data.data.productRating,
//                                    data.data.totalReview
//                                )
                                ,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(
                                        listOf(
                                            Font(R.font.poppins_400)
                                        )
                                    )
                                ),
                                modifier = Modifier.constrainAs(text) {
                                    start.linkTo(image.end, margin = 4.dp)
                                    top.linkTo(parent.top, margin = 2.dp)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                }
                            )
                        }
                    }
                }
                Divider(
                    color = Color(0xFFCAC4D0),
                    modifier = Modifier.constrainAs(dvdr) {
                        top.linkTo(chipRow.bottom, margin = 12.dp)
                    }
                )
            }
        }
    }

    @Composable
    fun ChipScreen(data: ProductResponse) {
        Column {
            ConstraintLayout {

                val (choose, chips, dvdr) = createRefs()
                Text(
                    text = getString(R.string.choose_variants),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp)
                        .constrainAs(choose) {},
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            listOf(
                                Font(R.font.poppins_500)
                            )
                        )
                    ),
                )
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                        .constrainAs(chips) {
                            top.linkTo(choose.bottom)
                        }
                ) {
                    data.data.productVariant.forEach {
                        var colorSelected by remember {
                            mutableStateOf(Color.Black)
                        }
                        var colorUnselected by remember {
                            mutableStateOf(Color.Black)
                        }
                        if (isSystemInDarkTheme()) {
                            colorSelected = Color(0xFF4A4458)
                            colorUnselected = Color(0xFF1D1B20)
                        } else {
                            colorSelected = Color(0xFFE8DEF8)
                            colorUnselected = Color.White
                        }
                        SuggestionChip(
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                productVariantPrice.value = it.variantName
                                viewModel.product.variantName = it.variantName
                                viewModel.product.variantPrice = it.variantPrice
                                viewModel.variant.postValue(if (it.variantPrice == 0) 0 else 1)
                                viewModel.variantArg = it.variantName
                            },
                            label = {
                                Text(
                                    text = it.variantName,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(
                                            listOf(
                                                Font(R.font.poppins_500)
                                            )
                                        )
                                    )
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (it.variantName == productVariantPrice.value) colorSelected else colorUnselected
                            ),
//                            border = SuggestionChipDefaults.suggestionChipBorder(
//                                borderColor = if (it.variantName == productVariantPrice.value) {
//                                    Color.Transparent
//                                } else {
//                                    Color(
//                                        0xFF49454F
//                                    )
//                                }
//                            )
                        )
                    }
                }

                Divider(
                    color = Color(0xFFCAC4D0),
                    modifier = Modifier.constrainAs(dvdr) {
                        top.linkTo(chips.bottom, margin = 12.dp)
                    }
                )
            }
        }
    }

    @Composable
    fun ErrorScreen(message: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val messageErr = try {
                Gson().fromJson(message, ErrorBody::class.java).message
            } catch (e: Exception) {
                message
            }
            val code = try {
                Gson().fromJson(message, ErrorBody::class.java).code.toString()
            } catch (e: Exception) {
                "500"
            }
            Image(
                painter = painterResource(id = R.drawable.store_error),
                modifier = Modifier.size(128.dp),
                contentDescription = ""
            )
            Text(
                textAlign = TextAlign.Center,
                text = code,
                modifier = Modifier.padding(top = 8.dp),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_500)
                        )
                    )
                )
            )
            Text(
                text = messageErr,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_400)
                        )
                    )
                )
            )
            Button(
                onClick = { viewModel.productDetail() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = getString(R.string.refresh))
            }
        }
    }

    @Composable
    fun LoadingScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }

    companion object {
        const val RAM_16_GB = "RAM 16GB"
        const val INDONESIA_CURRENCY = "IDR"
        const val CART_ADDED = "cartAdded"
    }
}
