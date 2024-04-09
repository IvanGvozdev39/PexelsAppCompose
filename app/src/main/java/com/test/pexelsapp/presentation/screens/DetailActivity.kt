package com.test.pexelsapp.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.test.pexelsapp.R

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DetailScreen()
        }
    }
}

@Composable
fun DetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(vertical = 10.dp)
        ) {
            IconButton(
                onClick = { /* Handle back button click */ },
                modifier = Modifier
                    .size(45.dp)
                    .padding(start = 20.dp)
                    .align(Alignment.CenterStart),
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back"
                    )
                }
            )

            Text(
                text = "Photographer Name",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            val painter = rememberImagePainter(
                data = photo.src.original,
                builder = {
                    placeholder(Color(android.graphics.Color.parseColor(photo.avg_color)))
                }
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                alignment = Alignment.CenterStart
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
        ) {
            Button(
                onClick = { /* Handle download button click */ },
                modifier = Modifier
                    .width(180.dp)
                    .padding(start = 20.dp)
                    .align(Alignment.CenterStart)
            ) {
                Text(text = "Download")
            }

            IconButton(
                onClick = { /* Handle bookmarks button click */ },
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 20.dp)
                    .align(Alignment.CenterEnd),
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "bookmark"
                    )
                }
            )
        }
    }
}
