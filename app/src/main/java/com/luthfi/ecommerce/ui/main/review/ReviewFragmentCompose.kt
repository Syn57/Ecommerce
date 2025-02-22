package com.luthfi.ecommerce.ui.main.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.gson.Gson
import com.gowtham.ratingbar.RatingBar
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ReviewResponse
import com.luthfi.ecommerce.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
class ReviewFragmentCompose : BaseFragment() {

    private val viewModel: ReviewViewModel by viewModel()
    private lateinit var productId: String
    private lateinit var composeView: ComposeView

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
        productId = ReviewFragmentComposeArgs.fromBundle(requireArguments()).productId

        viewModel.review(productId)
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
                ReviewScreen()
            }
        }
    }

    @Composable
    fun ReviewScreen() {
        val response by viewModel.reviewResponse.collectAsState()
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.buyer_reviews),
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
                                    contentDescription = stringResource(id = R.string.buyer_reviews)
                                )
                            }
                        }
                    )
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (response != null) {
                    when (response) {
                        is Resource.Success -> {
                            val data = response as Resource.Success<ReviewResponse>
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it)
                            ) {
                                items(data.data.data?.size ?: 0) { index ->
                                    ReviewItem(item = data.data.data?.get(index))
                                }
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
    fun ReviewItem(item: ReviewResponse.Data?) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                GlideImage(
                    model = item?.userImage,
                    contentDescription = "null",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp)
                        .clip(CircleShape),
                    loading = placeholder(R.drawable.thumbnail_load_product),
                    failure = placeholder(R.drawable.thumbnail_load_product)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .height(36.dp),
                ) {
                    val painterEmpty =
                        if (isSystemInDarkTheme()) R.drawable.ic_star_f else R.drawable.ic_star_nf
                    val painterFilled =
                        if (isSystemInDarkTheme()) R.drawable.ic_star_nf else R.drawable.ic_star_f
                    Text(
                        text = "${item?.userName}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(
                                listOf(
                                    Font(R.font.poppins_600)
                                )
                            )
                        )
                    )
                    RatingBar(
                        value = item?.userRating?.toFloat() ?: 0f,
                        painterEmpty = painterResource(id = painterEmpty),
                        painterFilled = painterResource(id = painterFilled),
                        isIndicator = true,
                        onValueChange = {
                        },
                        onRatingChanged = {
                        },
                        size = 17.dp,
                        spaceBetween = 0.dp,
                    )
                }
            }
            Text(
                text = "${item?.userReview}",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        listOf(
                            Font(R.font.poppins_400)
                        )
                    )
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Divider(color = Color.Gray, thickness = 1.dp)
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
                onClick = { viewModel.review(productId) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = getString(R.string.refresh))
            }
        }
    }

    @Composable
    fun LoadingScreen() {
        Column {
            CircularProgressIndicator()
        }
    }
}
