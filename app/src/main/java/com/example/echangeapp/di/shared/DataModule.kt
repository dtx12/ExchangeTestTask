package com.example.echangeapp.di.shared

import com.example.echangeapp.data.network.ExchangeRateApi
import com.example.echangeapp.data.repositories.AvailableCurrencyRepositoryImpl
import com.example.echangeapp.data.repositories.ExchangeRateRepositoryImpl
import com.example.echangeapp.domain.repositories.AvailableCurrencyRepository
import com.example.echangeapp.domain.repositories.ExchangeRatesRepository
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class DataModule {

    companion object {
        private const val BASE_URL = "https://api.exchangerate-api.com/v4/"
    }


    @Provides
    @Singleton
    fun provideExchangeApi(retrofit: Retrofit): ExchangeRateApi {
        return retrofit.create(ExchangeRateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideExchangeRateRepo(repo: ExchangeRateRepositoryImpl): ExchangeRatesRepository {
        return repo
    }

    @Provides
    @Singleton
    fun provideAvailableCurrencyRepository(repo: AvailableCurrencyRepositoryImpl): AvailableCurrencyRepository {
        return repo
    }
}