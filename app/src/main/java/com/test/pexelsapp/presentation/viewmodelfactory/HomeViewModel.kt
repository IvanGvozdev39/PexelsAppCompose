package com.test.pexelsapp.presentation.viewmodelfactory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.data.repository.ImageRepositoryImpl
import com.test.domain.models.images.Collection
import com.test.domain.models.images.ImageResponse
import com.test.domain.use_cases.LoadCuratedImagesUseCase
import com.test.domain.use_cases.LoadFeaturedCollectionsUseCase
import com.test.domain.use_cases.LoadPopularImagesUseCase
import com.test.domain.use_cases.SearchUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(
    private val imageRepository: ImageRepositoryImpl,
    private val loadFeaturedCollectionsUseCase: LoadFeaturedCollectionsUseCase,
    private val loadCuratedImagesUseCase: LoadCuratedImagesUseCase,
    private val loadPopularImagesUseCase: LoadPopularImagesUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    var imageList: MutableLiveData<Response<ImageResponse>?> = MutableLiveData()
    val noNetwork: MutableLiveData<Boolean> = MutableLiveData()
//    var cleanImageRV: MutableLiveData<Int> = MutableLiveData(0)
//    var imageListFeaturedCollection: MutableLiveData<Response<CollectionMediaResponse>> = MutableLiveData()
    private val _featuredCollectionNames = MutableLiveData<List<com.test.domain.models.images.Collection>?>()
    val featuredCollectionNames: LiveData<List<Collection>?> get() = _featuredCollectionNames

    enum class LastOperation {
        NONE, CURATED_PHOTOS, POPULAR_IMAGES, SEARCH
    }
    private var lastOperation: LastOperation = LastOperation.NONE

    init {
        getFeaturedCollectionNames()
        getCuratedPhotos()
    }


    private fun getFeaturedCollectionNames() {
        viewModelScope.launch {
            when (val result = loadFeaturedCollectionsUseCase.execute()) {
                is com.test.domain.result.Result.Success-> {
                    _featuredCollectionNames.postValue(result.data!!)
                }
                is com.test.domain.result.Result.Error -> {
                    _featuredCollectionNames.postValue(null)
                }
            }
        }
    }


//    fun getImagesFromCollection(collectionId: String) {
//        viewModelScope.launch {
//            val response = imageRepository.getImagesFromCollection(collectionId)
//            imageListFeaturedCollection.postValue(response)
//        }
//    }


    fun getCuratedPhotos() {
        viewModelScope.launch {
            lastOperation = LastOperation.CURATED_PHOTOS
            val result = loadCuratedImagesUseCase.execute()
            handleResult(result)
        }
    }

    fun getPopularImages() {
        viewModelScope.launch {
            lastOperation = LastOperation.POPULAR_IMAGES
            val result = loadPopularImagesUseCase.execute()
            handleResult(result)
        }
    }

    fun getImages(query: String) {
        viewModelScope.launch {
            lastOperation = LastOperation.SEARCH
            val result = searchUseCase.execute(query)
            handleResult(result)
        }
    }


    private fun handleResult(result: com.test.domain.result.Result<ImageResponse>) {
        when (result) {
            is com.test.domain.result.Result.Success -> {
                val response = Response.success(result.data)
                imageList.postValue(response)
                noNetwork.postValue(false)
            }
            is com.test.domain.result.Result.Error -> {
                imageList.postValue(null)
                noNetwork.postValue(true)
            }
        }
    }


    fun retryLastOperation(lastSearchQuery: String) {
        when (lastOperation) {
            LastOperation.CURATED_PHOTOS -> getCuratedPhotos()
            LastOperation.POPULAR_IMAGES -> getPopularImages()
            LastOperation.SEARCH -> {
                getImages(lastSearchQuery.toString())
            }
            else -> Unit
        }
    }


}