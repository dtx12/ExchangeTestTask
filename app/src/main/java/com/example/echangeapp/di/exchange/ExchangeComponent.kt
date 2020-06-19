package com.example.echangeapp.di.exchange

import com.example.echangeapp.di.scopes.ViewModel
import com.example.echangeapp.presentation.ui.exchange.ExchangeActivity
import dagger.Subcomponent

@ViewModel
@Subcomponent(modules = [ExchangeModule::class])
interface ExchangeComponent {
    fun inject(activity: ExchangeActivity)
}