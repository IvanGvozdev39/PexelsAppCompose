package com.test.pexelsapp.app

import android.app.Application
import com.test.pexelsapp.di.AppComponent
import com.test.pexelsapp.di.AppModule
import com.test.pexelsapp.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}