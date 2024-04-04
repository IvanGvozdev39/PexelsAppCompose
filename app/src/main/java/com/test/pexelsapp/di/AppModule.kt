package com.test.pexelsapp.di

import android.content.Context
import com.test.data.repository.ImageRepositoryImpl
import com.test.domain.use_cases.LoadCuratedImagesUseCase
import com.test.domain.use_cases.LoadFeaturedCollectionsUseCase
import com.test.domain.use_cases.LoadPopularImagesUseCase
import com.test.domain.use_cases.SearchUseCase
import com.test.pexelsapp.presentation.viewmodelfactory.BookmarksViewModelFactory
import com.test.pexelsapp.presentation.viewmodelfactory.DetailsViewModelFactory
import com.test.pexelsapp.presentation.viewmodelfactory.HomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideDetailsViewModelFactory(context: Context): DetailsViewModelFactory {
        return DetailsViewModelFactory(context)
    }

    @Provides
    fun provideBookmarksViewModelFactory(context: Context): BookmarksViewModelFactory {
        return BookmarksViewModelFactory(context)
    }


    @Provides
    fun provideHomeViewModelFactory(
        imageRepository: ImageRepositoryImpl,
        loadFeaturedCollectionsUseCase: LoadFeaturedCollectionsUseCase,
        loadCuratedPhotosUseCase: LoadCuratedImagesUseCase,
        loadPopularImagesUseCase: LoadPopularImagesUseCase,
        searchUseCase: SearchUseCase
    ): HomeViewModelFactory {
        return HomeViewModelFactory(
            imageRepository,
            loadFeaturedCollectionsUseCase,
            loadCuratedPhotosUseCase,
            loadPopularImagesUseCase,
            searchUseCase
        )
    }

}