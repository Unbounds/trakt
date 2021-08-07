package com.unbounds.trakt.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.unbounds.trakt.BuildConfig
import com.unbounds.trakt.service.api.TmdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TmdbApiModule {

    @Singleton
    @Provides
    fun providesTmdbAPI(@Named("TMDB") retrofit: Retrofit) = retrofit.create(TmdbApi::class.java)

    @Singleton
    @Provides
    @Named("TMDB")
    fun providesOkHttpClient() = OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val newUrl = chain.request().url
                        .newBuilder()
                        .addQueryParameter("api_key", BuildConfig.TMDB_KEY)
                        .build()

                val newRequest = chain.request()
                        .newBuilder()
                        .url(newUrl)
                        .build()

                chain.proceed(newRequest)
            }
            .build()

    @Singleton
    @Provides
    @Named("TMDB")
    fun providesRetrofit(@Named("TMDB") okHttpClient: OkHttpClient) = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("${BuildConfig.TMDB_API_URL}/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
}

