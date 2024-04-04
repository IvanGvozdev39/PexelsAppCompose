package com.test.pexelsapp.di

import com.test.domain.repository.ImageRepository
import com.test.domain.use_cases.LoadCuratedImagesUseCase
import com.test.domain.use_cases.LoadFeaturedCollectionsUseCase
import com.test.domain.use_cases.LoadPopularImagesUseCase
import com.test.domain.use_cases.SearchUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideLoadFeaturedCollectionsUseCase(imageRepository: ImageRepository): LoadFeaturedCollectionsUseCase {
        return LoadFeaturedCollectionsUseCase(imageRepository)
    }

    @Provides
    fun provideLoadCuratedPhotosUseCase(imageRepository: ImageRepository): LoadCuratedImagesUseCase {
        return LoadCuratedImagesUseCase(imageRepository)
    }

    @Provides
    fun provideLoadPopularImagesUseCase(imageRepository: ImageRepository): LoadPopularImagesUseCase {
        return LoadPopularImagesUseCase(imageRepository)
    }

    @Provides
    fun provideSearchUseCase(imageRepository: ImageRepository): SearchUseCase {
        return SearchUseCase(imageRepository)
    }

}