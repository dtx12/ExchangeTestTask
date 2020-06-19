package com.example.echangeapp.di.shared

import com.example.echangeapp.di.exchange.ExchangeComponent
import com.example.echangeapp.di.exchange.ExchangeModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun plus(exchangeModule: ExchangeModule): ExchangeComponent
}