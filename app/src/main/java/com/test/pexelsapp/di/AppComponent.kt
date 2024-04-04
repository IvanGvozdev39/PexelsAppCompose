package com.test.pexelsapp.di

import com.test.pexelsapp.presentation.BookmarksFragment
import com.test.pexelsapp.presentation.DetailsFragment
import com.test.pexelsapp.presentation.HomeFragment
import dagger.Component

@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {

    fun inject(homeFragment: HomeFragment)
    fun inject(detailsFragment: DetailsFragment)
    fun inject(bookmarksFragment: BookmarksFragment)

}