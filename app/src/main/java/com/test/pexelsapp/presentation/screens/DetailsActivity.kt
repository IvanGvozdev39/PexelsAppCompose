package com.test.pexelsapp.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.test.domain.models.images.Photo
import com.test.pexelsapp.R
import com.test.pexelsapp.app.App
import com.test.pexelsapp.presentation.viewmodelfactory.DetailsViewModel
import com.test.pexelsapp.presentation.viewmodelfactory.DetailsViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsActivity : ComponentActivity() {

    @Inject
    lateinit var vmFactory: DetailsViewModelFactory
    private lateinit var viewModel: DetailsViewModel
    private var photo: Photo? = null
    private lateinit var src: String


    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[DetailsViewModel::class.java]
        photo = intent.getParcelableExtra("photo")

        if (photo != null)
            src = photo!!.src.original ?: ""





        setContent {
//            var downloadInProcess by remember { mutableStateOf(false) }
            var inBookmarks by remember { mutableStateOf(false) }

            if (photo != null) {
                var dbPhotos: ArrayList<Photo>
                lifecycleScope.launch {
                    dbPhotos = viewModel.getAllImagesFromBookmarks() as ArrayList<Photo>
                    for (dbPhoto in dbPhotos) {
                        Log.d("awksaflw", photo?.src?.original + "\n" + dbPhoto.src.original)
                        if (photo?.src?.original?.equals(dbPhoto.src.original) == true) {
                            inBookmarks = true
                        }
                    }
                }
            }


            /*viewModel.imageDownloaded.observe(this@DetailsActivity) { downloaded ->
                if (downloaded) {
                    if (downloadInProcess) {
                        downloadInProcess = false
                        Toast.makeText(this@DetailsActivity, getString(R.string.downloaded_successfully), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DetailsActivity, getString(R.string.download_failed), Toast.LENGTH_SHORT).show()
                }
            }*/


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
                            .padding(vertical = 10.dp, horizontal = 20.dp)
                    ) {
                        IconButton(
                            onClick = {
                                onBackPressedDispatcher.onBackPressed()
                            },
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.CenterStart)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = colorResource(id = R.color.lighter_gray)),
                            content = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "back"
                                )
                            }
                        )

                        Text(
                            text = photo?.photographer ?: "",
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = 16.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        val color =
                            Color(android.graphics.Color.parseColor(photo?.avg_color ?: "#ECF0F4"))

                        val painter = rememberImagePainter(
                            data = src,
                            builder = {
                                placeholder(ColorDrawable(color.toArgb()))
                            }
                        )

                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio( // Calculate aspect ratio based on ImageState
                                    if (painter.state is ImagePainter.State.Success) {
                                        val imageBitmap =
                                            (painter.state as ImagePainter.State.Success).result.drawable.toBitmap()
                                        val imageWidth = imageBitmap.width
                                        val imageHeight = imageBitmap.height
                                        imageWidth.toFloat() / imageHeight.toFloat()
                                    } else {
                                        // Use a default aspect ratio for the placeholder (optional)
                                        1.0f
                                    }
                                )
                                .clip(shape = RoundedCornerShape(20.dp)) // Apply rounded corners
                                .align(Alignment.CenterStart),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    // Define colors for convenience
                    val backgroundColor = Color(0xFFE0E0E0) // Lighter gray
                    val circleColor = Color.Red


                    Row(
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp)
                            .padding(start = 20.dp)
                            .align(Alignment.CenterStart)
                            .clip(RoundedCornerShape(50))
                            .background(backgroundColor)
                            .clickable {
//                                Log.d("oeadffa", "In the onClick " + downloadInProcess.toString())
                                if (viewModel.imageDownloaded != 1) {
                                    if (isWriteStoragePermissionGranted()) {
//                                        downloadInProcess = true
                                        viewModel.downloadImage(src)
                                    } else {
                                        // Request WRITE_EXTERNAL_STORAGE permission
                                        requestPermissions(
                                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
                                        )
                                    }
                                }
                            }
                    ) {
                        // Red circle with icon
                        Box(
                            modifier = Modifier
                                .size(50.dp) // Adjust circle size as needed
                                .clip(CircleShape)
                                .background(circleColor)
                                .align(Alignment.CenterVertically),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = if (viewModel.imageDownloaded == 1) R.drawable.ic_clock else R.drawable.ic_download),
                                tint = colorResource(id = R.color.white),
                                modifier = Modifier.padding(13.dp),
                                contentDescription = "Download"
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp)) // Optional spacing between circle and text

                        // Button text
                        Text(
                            text = if (viewModel.imageDownloaded == 1)
                                stringResource(id = R.string.downloading)
                            else if (viewModel.imageDownloaded == 2)
                                stringResource(id = R.string.downloaded)
                            else
                                stringResource(id = R.string.download),
                            color = colorResource(id = R.color.black),
                            modifier = Modifier
                                .align(Alignment.CenterVertically) // Center text vertically
                                .padding(start = 5.dp)
                        )
                    }


                    IconButton(
                        onClick = {
                            photo?.let {
                                if (!inBookmarks) {
                                    lifecycleScope.launch { viewModel.addImageToBookmarks(it) }
                                    inBookmarks = true
                                } else {
                                    lifecycleScope.launch { viewModel.deleteImageFromBookmarks(it) }
                                    inBookmarks = false
                                }
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 24.dp)
                            .align(Alignment.CenterEnd)
                            .clip(CircleShape)
                            .background(color = colorResource(id = R.color.lighter_gray)),
                        content = {
                            Icon(
                                painter = painterResource(id = if (!inBookmarks) R.drawable.ic_bookmark else R.drawable.ic_bookmark_added),
                                contentDescription = "bookmark",
                                tint = colorResource(id = R.color.black) // Set the color of the bookmark icon
                            )
                        }
                    )
                }
            }
        }
    }



    private fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            permissionCheck == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the download
                viewModel.downloadImage(src)
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        private const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 100
    }
}
