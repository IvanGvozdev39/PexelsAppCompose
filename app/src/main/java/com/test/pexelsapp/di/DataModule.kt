package com.test.pexelsapp.di

import com.test.data.repository.ImageRepositoryImpl
import com.test.domain.repository.ImageRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideImageRepository(): ImageRepository {
        return ImageRepositoryImpl()
    }

}