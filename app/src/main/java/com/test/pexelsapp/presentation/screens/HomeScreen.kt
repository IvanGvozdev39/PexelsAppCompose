package com.test.pexelsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.pexelsapp.R

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        var searchText by remember {
            mutableStateOf("")
        }

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            placeholder = { Text(text = "Search") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.LightGray),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_red),
                    contentDescription = "Search icon"
                )
            },
            trailingIcon = {
                if (searchText.length > 0) {
//                if (searchText.isNotEmpty()) {
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
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
        ) {
            items(10) { index ->
                // Placeholder item, replace with actual content
                Text(text = "Item $index")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Color.White)
//                .elevation(10.dp)
        ) {
            Tab(
                selected = true,
                onClick = { /* Handle tab click */ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Red)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home_gray),
                        contentDescription = "Home icon",
                        tint = Color.Red
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Red)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_gray),
                        contentDescription = "Bookmark icon"
                    )
                }
            }
        }
    }
}



//navController.navigate(Screen.DetailScreen.route)