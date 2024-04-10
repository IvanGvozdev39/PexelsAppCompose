package com.test.pexelsapp.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.pexelsapp.R

class BookmarksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .padding(vertical = 10.dp)
                    ) {
                        IconButton(
                            onClick = {
                                onBackPressedDispatcher.onBackPressed()
                            },
                            modifier = Modifier
                                .size(45.dp)
                                .padding(start = 20.dp)
                                .align(Alignment.CenterStart)
                                .clip(RoundedCornerShape(20.dp))
                                .background(color = colorResource(id = R.color.lighter_gray)),
                            content = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "back"
                                )
                            }
                        )

                        Text(
                            text = stringResource(id = R.string.bookmarks),
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = 16.sp
                        )
                    }

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
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
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
                                    .height(4.dp)
                                    .width(30.dp)
                                    .background(colorResource(id = R.color.red))
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_bookmark_gray),
                                contentDescription = "Home icon",
                                tint = colorResource(id = R.color.red)
                            )
                        }
                    }
                }
            }
        }
    }
}