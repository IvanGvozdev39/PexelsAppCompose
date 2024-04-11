package com.test.pexelsapp.di

import com.test.pexelsapp.presentation.screens.BookmarksActivity
import com.test.pexelsapp.presentation.screens.DetailsActivity
import com.test.pexelsapp.presentation.screens.HomeActivity
import dagger.Component

@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {

    fun inject(homeActivity: HomeActivity)
    fun inject(detailsActivity: DetailsActivity)
    fun inject(bookmarksFragment: BookmarksActivity)

}