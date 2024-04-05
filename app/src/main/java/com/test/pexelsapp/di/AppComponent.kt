package com.test.pexelsapp.di

import com.test.pexelsapp.presentation.BookmarksFragment
import com.test.pexelsapp.presentation.DetailsFragment
import com.test.pexelsapp.presentation.screens.HomeActivity
import dagger.Component

@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {

    fun inject(homeActivity: HomeActivity)
    fun inject(detailsFragment: DetailsFragment) //TODO: Change to Activity
    fun inject(bookmarksFragment: BookmarksFragment)

}