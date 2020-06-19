package com.example.echangeapp

import android.app.Application
import com.example.echangeapp.di.shared.ApplicationComponent
import com.example.echangeapp.di.shared.ApplicationModule
import com.example.echangeapp.di.shared.DaggerApplicationComponent
import com.example.echangeapp.di.shared.DataModule

class ExchangeApp : Application() {

    companion object {
        lateinit var instance: ExchangeApp
        lateinit var appComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent =
            DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this))
                .dataModule(DataModule()).build()
    }
}