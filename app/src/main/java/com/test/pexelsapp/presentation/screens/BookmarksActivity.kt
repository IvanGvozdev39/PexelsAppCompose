package com.test.pexelsapp.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import com.test.domain.models.images.Photo
import com.test.pexelsapp.R
import com.test.pexelsapp.app.App
import com.test.pexelsapp.presentation.viewmodelfactory.BookmarksViewModel
import com.test.pexelsapp.presentation.viewmodelfactory.BookmarksViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookmarksActivity : ComponentActivity() {

    @Inject
    lateinit var vmFactory: BookmarksViewModelFactory
    lateinit var viewModel: BookmarksViewModel
    private var layoutManagerState: Parcelable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[BookmarksViewModel::class.java]


        setContent {

            var imageData by remember {
                mutableStateOf(ArrayList<Photo>())
            }

            var noResultsVisible by remember { mutableStateOf(false) }


            viewModel.imageList.observe(this) { response ->
                if (response != null) {
                    imageData = response
                    noResultsVisible = response.size == 0
                }
            }



            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = if (!isSystemInDarkTheme()) R.color.white else R.color.dark_background))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .padding(vertical = 16.dp, horizontal = 20.dp)
                    ) {
                        IconButton(
                            onClick = {
                                onBackPressedDispatcher.onBackPressed()
                            },
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.CenterStart)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = colorResource(id = if (!isSystemInDarkTheme()) R.color.lighter_gray else R.color.dark_ligther_gray)),
                            content = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "back",
                                    colorFilter = ColorFilter.tint(color = colorResource(id = if (!isSystemInDarkTheme()) R.color.black else R.color.white))
                                )
                            }
                        )

                        Text(
                            text = stringResource(id = R.string.bookmarks),
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = 16.sp,
                            color = colorResource(id = if (!isSystemInDarkTheme()) R.color.black else R.color.white)
                        )
                    }


                    if (noResultsVisible) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                androidx.compose.material.Text(
                                    text = stringResource(id = R.string.nothing_in_bookmarks),
                                    color = colorResource(id = R.color.gray),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                ClickableText(
                                    text = AnnotatedString.Builder().apply {
                                        withStyle(
                                            style = SpanStyle(
                                                color = colorResource(id = R.color.red),
                                                textDecoration = TextDecoration.Underline,
                                                fontSize = 16.sp
                                            )
                                        ) {
                                            append(stringResource(id = R.string.explore))
                                        }
                                    }.toAnnotatedString(),
                                    onClick = {
                                        noResultsVisible = false
                                        onBackPressedDispatcher.onBackPressed()
                                    /*searchText = ""
                                        viewModel.getPopularImages()
                                        selectedCollectionIndex = -1*/
                                    }
                                )
                            }
                        }
                    }


                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                        modifier = Modifier.padding(bottom = 55.dp)
                    ) {
                        items(imageData) { photo ->
                            Card(
                                modifier = Modifier
                                    .padding(start = 6.dp, end = 6.dp, bottom = 12.dp),
                                shape = RoundedCornerShape(20.dp),
                                onClick = {
                                    val intent = Intent(this@BookmarksActivity, DetailsActivity::class.java)
                                    intent.putExtra("photo", photo) // Assuming photo is a Photo object
                                    startActivity(intent)
                                }
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize() // Fill entire card area
                                ) {
                                    ImageItem(photo = photo, onItemClick = {})

                                    Box( // Box for text with bottom positioning
                                        modifier = Modifier
                                            .fillMaxWidth() // Occupy full card width
                                            .align(Alignment.BottomCenter) // Align to bottom center of Card's Box
                                            .background(colorResource(id = R.color.gray_semi_transparent))
                                            .padding(8.dp)
                                    ) {
                                        Row( // Use Row for horizontal alignment
                                            modifier = Modifier.fillMaxWidth(), // Occupy full width within Box
                                            horizontalArrangement = Arrangement.Center // Center content within Row
                                        ) {
                                            Text(
                                                text = photo.photographer,
                                                color = colorResource(id = R.color.white),
                                                fontSize = 16.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth() // Text can now occupy full Row width
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(colorResource(id = if (!isSystemInDarkTheme()) R.color.white else R.color.dark_background))
                        .align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Tab(
                        selected = true,
                        onClick = {
                                  onBackPressedDispatcher.onBackPressed()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(30.dp)
                                    .background(colorResource(id = R.color.gray))
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_home_gray),
                                contentDescription = "Home icon",
                                tint = colorResource(id = R.color.gray)
                            )
                        }
                    }

                    Tab(
                        selected = false,
                        onClick = { /* Handle tab click */ },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(30.dp)
                                    .background(colorResource(id = R.color.red))
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_bookmark_filled),
                                contentDescription = "Bookmark icon",
                                tint = colorResource(id = R.color.red)
                            )
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { viewModel.getAllImagesFromBookmarks() }
    }


    @Composable
    fun ImageItem(photo: Photo, onItemClick: (Photo) -> Unit) {
        val painter = rememberImagePainter(photo.src.original)
        val imageSize = remember { mutableStateOf(Size.Zero) }

        BoxWithConstraints {
            val maxWidth = constraints.maxWidth.toFloat()
            val imageHeight = (maxWidth / photo.width.toFloat()) * photo.height.toFloat()
            imageSize.value = Size(maxWidth, imageHeight)
            Box(
                modifier = Modifier
                    .aspectRatio(photo.width.toFloat() / photo.height.toFloat())
                    .background(
                        Color(android.graphics.Color.parseColor(photo.avg_color)),
                        RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // If you want to show a placeholder while the image is loading
                /* if (painter.state is ImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center)
                    )
                } */
            }
        }
    }
}