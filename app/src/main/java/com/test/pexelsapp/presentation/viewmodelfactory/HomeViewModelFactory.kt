package com.test.pexelsapp.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.data.repository.ImageRepositoryImpl
import com.test.domain.use_cases.LoadCuratedImagesUseCase
import com.test.domain.use_cases.LoadFeaturedCollectionsUseCase
import com.test.domain.use_cases.LoadPopularImagesUseCase
import com.test.domain.use_cases.SearchUseCase

class HomeViewModelFactory(
    val imageRepository: ImageRepositoryImpl,
    val loadFeaturedCollectionsUseCase: LoadFeaturedCollectionsUseCase,
    val loadCuratedPhotosUseCase: LoadCuratedImagesUseCase,
    val loadPopularImagesUseCase: LoadPopularImagesUseCase,
    val searchUseCase: SearchUseCase

) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                imageRepository,
                loadFeaturedCollectionsUseCase,
                loadCuratedPhotosUseCase,
                loadPopularImagesUseCase,
                searchUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}