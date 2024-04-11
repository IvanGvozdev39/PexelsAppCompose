package com.test.pexelsapp.presentation.viewmodelfactory

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.test.data.room_db.PhotoDatabase
import com.test.domain.models.images.Photo
import com.test.domain.models.images.Src
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarksViewModel(private val context: Context) : ViewModel() {
    val db = Room.databaseBuilder(context, PhotoDatabase::class.java, "bookmarks-db").build()
    val photoDao = db.photoDao()
    val imagesLoaded = MutableLiveData(false)
    var imageList: MutableLiveData<ArrayList<Photo>> = MutableLiveData()

    init {
        viewModelScope.launch {
            getAllImagesFromBookmarks()
        }
    }

    suspend fun getAllImagesFromBookmarks() {
        withContext(Dispatchers.IO) {
            val data = photoDao!!.getAll()
            val photoList = ArrayList<Photo>()
            for (image in data) {
                photoList.add(
                    Photo(
                        image.alt, image.avgColor, image.height, image.id, image.liked,
                        image.photographer, image.photographerId, image.photographerUrl, Src(
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
            imageList.postValue(photoList)
        }
    }
}