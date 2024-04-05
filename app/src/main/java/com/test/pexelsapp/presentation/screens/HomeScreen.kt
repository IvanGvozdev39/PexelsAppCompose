package com.test.pexelsapp.presentation.screens

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.test.pexelsapp.R
import com.test.pexelsapp.presentation.viewmodelfactory.HomeViewModelFactory
import javax.inject.Inject


@Inject
lateinit var vmFactory: HomeViewModelFactory


@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var homeTabSelected by remember { mutableStateOf(true) }
    var layoutManagerState: Parcelable? = null
    var connectionRecoveredRecreate = false
    var lastSearchQuery = ""




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))) {
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
                onQueryChange = { searchText = it },
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

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 6.dp)
            ) {
                itemsIndexed((0..50).toList()) { i, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp * i)
                            .padding(horizontal = 6.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {}
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