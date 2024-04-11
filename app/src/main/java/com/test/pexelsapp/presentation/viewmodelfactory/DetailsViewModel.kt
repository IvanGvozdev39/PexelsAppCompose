package com.test.pexelsapp.presentation.viewmodelfactory

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.test.data.room_db.PhotoDatabase
import com.test.domain.models.images.Photo
import com.test.domain.models.images.Src
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DetailsViewModel(private val context: Context) : ViewModel() {
    //    private val _imageDownloaded = MutableLiveData<Boolean>()
    var imageDownloaded by mutableStateOf(0)   //0 - initial; 1 - in process; 2 - downloaded; -1 - failure

    //    val imageDownloaded: LiveData<Boolean> get() = _imageDownloaded
    val db = Room.databaseBuilder(context, PhotoDatabase::class.java, "bookmarks-db").build()
    val photoDao = db.photoDao()

    fun downloadImage(imageUrl: String) {
        viewModelScope.launch {
            imageDownloaded = 1
            try {
                val byteArray = getImageByteArrayFromUrl(imageUrl)
                byteArray?.let { data ->
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    saveBitmapToStorage(bitmap)
                    Log.d("wwafjkawjk", "imageDownloaded true")
//                    _imageDownloaded.postValue(true) // Notify view that image is downloaded
                    imageDownloaded = 2
                }
            } catch (e: Exception) {
                Log.d("wwafjkawjk", "error occured imageDownloaded false")
//                _imageDownloaded.postValue(false) // Notify view that image download failed
                    imageDownloaded = -1
            // Handle exception
            }
        }
    }

    private suspend fun getImageByteArrayFromUrl(imageUrl: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit() // Load the image asynchronously
                    .get() // Get the Bitmap synchronously
                    ?.let { bitmap ->
                        val outputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.toByteArray()
                    }
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun saveBitmapToStorage(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val filename = "my_image_${System.currentTimeMillis()}.jpg"
            var fos: OutputStream? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
    }


    suspend fun addImageToBookmarks(image: Photo) {
        withContext(Dispatchers.IO) {
            val data = com.test.data.room_db.PhotoEntity(
                image.id,
                image.alt,
                image.avg_color,
                image.height,
                image.liked,
                image.photographer,
                image.photographer_id,
                image.photographer_url,
                image.src.original,
                image.url,
                image.width
            )
            photoDao?.insert(data)
        }
    }


    suspend fun deleteImageFromBookmarks(image: Photo) {
        withContext(Dispatchers.IO) {
            val data = com.test.data.room_db.PhotoEntity(
                image.id,
                image.alt,
                image.avg_color,
                image.height,
                image.liked,
                image.photographer,
                image.photographer_id,
                image.photographer_url,
                image.src.original,
                image.url,
                image.width
            )
            photoDao?.delete(data)
        }
    }


    suspend fun getAllImagesFromBookmarks(): List<Photo> {
        return withContext(Dispatchers.IO) {
            val data = photoDao?.getAll()
            val photoList = ArrayList<Photo>()
            if (data != null) {
                for (image in data) {
                    photoList.add(
                        Photo(
                            image.alt, image.avgColor, image.height, image.id, image.liked,
                            image.photographer, image.photographerId, image.photographerUrl,
                            Src(
                                image.src,
                                image.src,
                                image.src,
                                image.src,
                                image.src,
                                image.src,
                                image.src,
                                image.src
                            ),
                            image.url, image.width
                        )
                    )
                }
            }
            photoList
        }
    }


    /*private fun isWriteStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }*/
}
