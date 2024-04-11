package com.test.pexelsapp.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import com.test.domain.models.images.Photo
import com.test.pexelsapp.R
import com.test.pexelsapp.app.App
import com.test.pexelsapp.presentation.viewmodelfactory.HomeViewModel
import com.test.pexelsapp.presentation.viewmodelfactory.HomeViewModelFactory
import javax.inject.Inject


class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var vmFactory: HomeViewModelFactory
    private var layoutManagerState: Parcelable? = null
    private var connectionRecoveredRecreate = false
    lateinit var viewModel: HomeViewModel
    private var lastSearchQuery = ""

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[HomeViewModel::class.java]

        super.onCreate(savedInstanceState)

        setContent {
            var homeTabSelected by remember { mutableStateOf(true) }
            var noResultsVisible by remember { mutableStateOf(false) }

            var featuredCollectionsData by remember {
                mutableStateOf(listOf<com.test.domain.models.images.Collection>()) // Change YourCollectionType to the actual type
            }

            // Observe the LiveData and update the state when it changes
            viewModel.featuredCollectionNames.observe(this) { collections ->
                collections?.let {
                    featuredCollectionsData = it
                }
            }

            var imageData by remember {
                mutableStateOf(ArrayList<Photo>())
            }

            var imagesLoaded by remember {
                mutableStateOf(false)
            }

            var selectedCollectionIndex by remember { mutableStateOf(-1) }


            viewModel.imagesLoaded.observe(this) {
                imagesLoaded = it
            }

            LaunchedEffect(selectedCollectionIndex) { // Observe collection selection changes
                if (selectedCollectionIndex != -1) {
                    viewModel.getImages(featuredCollectionsData[selectedCollectionIndex].title)
                }
            }

            viewModel.imageList.observe(this) { response ->
                Log.d("awawas", "observator triggered")
                if (response?.isSuccessful == true) {
                    Log.d("awawas", "response successful")
                    val data = response.body()
                    if (data != null) {
                        Log.d("awawas", data.toString())
                        imageData = data.photos as ArrayList<Photo> // Update with a new list object
                    }
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.white))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.white))
                ) {
                    var searchText by remember {
                        mutableStateOf("")
                    }


                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                        query = searchText,
                        onQueryChange = { newQuery ->
                            searchText = newQuery
                            if (newQuery.isNotEmpty()) {
                                viewModel.getImages(newQuery)
                                lastSearchQuery = newQuery
                                selectedCollectionIndex = -1
                            }
                        },
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        placeholder = { Text(text = stringResource(id = R.string.search)) },
                        colors = SearchBarDefaults.colors(
                            containerColor = colorResource(id = R.color.lighter_gray),
                            inputFieldColors = TextFieldDefaults.colors(
                                focusedTextColor = colorResource(id = R.color.black),
                                unfocusedTextColor = colorResource(id = R.color.gray)
                            )
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search_red),
                                contentDescription = "Search icon"
                            )
                        },
                        trailingIcon = {
                            if (searchText.length > 0) {
                                IconButton(
                                    onClick = { searchText = "" }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_close_red),
                                        contentDescription = "Clear icon"
                                    )
                                }
                            }
                        }
                    ) {}

                    Spacer(modifier = Modifier.height(20.dp))


                    //No results layout
                    if (noResultsVisible) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(id = R.string.no_results_found),
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
                                        searchText = ""
                                        viewModel.getPopularImages()
                                        selectedCollectionIndex = -1
                                    }
                                )
                            }
                        }
                    }



                    if (featuredCollectionsData.isNotEmpty() && !noResultsVisible) {
                        LazyRow(
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 16.dp)
                        ) {
                            itemsIndexed(featuredCollectionsData) { index, collection ->
                                Box(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .background(
                                            color = if (index == selectedCollectionIndex) colorResource(
                                                id = R.color.red
                                            ) else colorResource(id = R.color.lighter_gray),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clickable {
                                            selectedCollectionIndex = index
                                            viewModel.getImages(collection.title)
                                            /* Handle click on collection */
                                        }
                                ) {
                                    Text(
                                        text = collection.title,
                                        modifier = Modifier.padding(12.dp),
                                        color = if (index == selectedCollectionIndex) colorResource(
                                            id = R.color.white
                                        ) else colorResource(id = R.color.black)
                                    )
                                }
                            }
                        }
                    }


                    if (imagesLoaded) {
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
                                        val intent = Intent(this@HomeActivity, DetailsActivity::class.java)
                                        intent.putExtra("photo", photo) // Assuming photo is a Photo object
                                        startActivity(intent)
                                    }
                                ) {
                                    ImageItem(photo = photo, onItemClick = {
                                    })
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(20.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(colorResource(id = R.color.white))
                        .align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Tab(
                        selected = true,
                        onClick = { /* Handle tab click */ },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (homeTabSelected) {
                                Box(
                                    modifier = Modifier
                                        .height(3.dp)
                                        .width(30.dp)
                                        .background(colorResource(id = R.color.red))
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_home_gray),
                                contentDescription = "Home icon",
                                tint = if (homeTabSelected) {
                                    colorResource(id = R.color.red)
                                } else colorResource(id = R.color.gray)
                            )
                        }
                    }

                    Tab(
                        selected = false,
                        onClick = {
                            val intent = Intent(this@HomeActivity, BookmarksActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (!homeTabSelected) {
                                Box(
                                    modifier = Modifier
                                        .height(3.dp)
                                        .width(30.dp)
                                        .background(colorResource(id = R.color.red))
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_bookmark_gray),
                                contentDescription = "Bookmark icon",
                                tint = if (!homeTabSelected) {
                                    colorResource(id = R.color.red)
                                } else colorResource(id = R.color.gray)
                            )
                        }
                    }
                }
            }
        }
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


    /*@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(navController: NavController) {

    }*/

    /*@Composable
    @Preview
    fun HomeScreenPreview() {
        HomeScreen(navController = rememberNavController())
    }*/
}


