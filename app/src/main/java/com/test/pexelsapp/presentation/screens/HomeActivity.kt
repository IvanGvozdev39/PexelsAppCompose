package com.test.pexelsapp.presentation.screens

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
            val searchBarFocusRequester = remember { FocusRequester() }

            var featuredCollections by remember {
                mutableStateOf(listOf<com.test.domain.models.images.Collection>()) // Change YourCollectionType to the actual type
            }

            // Observe the LiveData and update the state when it changes
            viewModel.featuredCollectionNames.observe(this) { collections ->
                collections?.let {
                    featuredCollections = it
                }
            }

            val imageData by remember {
                mutableStateOf(ArrayList<Photo>()) // Change YourPhotoType to the actual type
            }

            // Observe the LiveData and update the state when it changes
            viewModel.imageList.observe(this) { response ->
                if (response?.isSuccessful == true) {
                    val data = response.body()
                    if (data != null) {
                        imageData.clear()
                        imageData.addAll(data.photos)
                        // Update UI adapter or other UI components
                        noResultsVisible = data.photos.isEmpty()
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
                        onQueryChange = {
                            searchText = it
                            if (searchText.trim().isEmpty()) {
//                                searchBarCloseIcon.visibility = View.GONE
                            } else {
                                //TODO:
                                viewModel.getImages(searchText)
//                                searchBarCloseIcon.visibility = View.VISIBLE
//                                featuredCollectionsAdapter.resetSelection()
//                                context?.let { imageRVAdapter.setImageData(ArrayList<Photo>(), it) }
                                viewModel.getImages(searchText)
                                lastSearchQuery = searchText
//                                imageRV.scrollToPosition(0)
                            }
                                        },
                        onSearch = {searchBarFocusRequester.freeFocus()},
                        active = false,
                        onActiveChange = {if (it) searchBarFocusRequester.requestFocus()},
                        placeholder = { Text(text = stringResource(id = R.string.search)) },
                        colors = SearchBarDefaults.colors(
                            containerColor = colorResource(id = R.color.lighter_gray),
                            inputFieldColors = TextFieldDefaults.colors(
                                focusedTextColor = colorResource(id = R.color.black),
                                unfocusedTextColor = colorResource(id = R.color.gray)
                            )
                        ),
                        leadingIcon = {
                            if (searchText.length > 0) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search_red),
                                    contentDescription = "Search icon"
                                )
                            }
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.no_results_found),
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                            //Explore Button
                            Button(
                                onClick = {
                                    noResultsVisible = false
                                    searchText = ""
                                    searchBarFocusRequester.freeFocus()
                                    viewModel.getPopularImages()
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.explore),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                            }
                        }
                    }


                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 6.dp),
                        modifier = Modifier.padding(bottom = 65.dp)
                    ) {
                        items(imageData) { photo ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                ImageItem(photo = photo, onItemClick = {

                                //TODO: Replace with an activity-appropriate intent:
                                /*val bundle = Bundle().apply {
                                        putParcelable("photo", photo)
                                    }*/

                                })
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(20.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(Color.White)
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
                                        .height(4.dp)
                                        .width(30.dp)
                                        .background(Color.Red)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_home_gray),
                                contentDescription = "Home icon",
                                tint = if (homeTabSelected) {
                                    Color.Red
                                } else colorResource(id = R.color.gray)
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
                            if (!homeTabSelected) {
                                Box(
                                    modifier = Modifier
                                        .height(4.dp)
                                        .width(30.dp)
                                        .background(Color.Red)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_bookmark_gray),
                                contentDescription = "Home icon",
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

        Box(
            modifier = Modifier
                .padding(4.dp)
                .aspectRatio(1f)
                .clickable { onItemClick(photo) }
        ) {
            Image(
                painter = painter,
                contentDescription = null, // content description for screen readers, can be null if image is purely decorative
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // scale type for the image
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


