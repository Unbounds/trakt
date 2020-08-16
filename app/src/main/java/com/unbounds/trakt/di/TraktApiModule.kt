package com.unbounds.trakt.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.unbounds.trakt.BuildConfig
import com.unbounds.trakt.service.api.AuthInterceptor
import com.unbounds.trakt.service.api.TraktApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class TraktApiModule {

    @Singleton
    @Provides
    fun providesTraktAPI(@Named("Trakt") retrofit: Retrofit) = retrofit.create(TraktApi::class.java)

    @Singleton
    @Provides
    @Named("Trakt")
    fun providesOkHttpClient(authInterceptor: AuthInterceptor) = OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val newRequest = chain.request()
                        .newBuilder()
                        .addHeader("trakt-api-key", BuildConfig.CLIENT_ID)
                        .addHeader("trakt-api-version", "2")
                        .build()

                chain.proceed(newRequest)
            }
            .addInterceptor(authInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("Trakt")
    fun providesRetrofit(@Named("Trakt") okHttpClient: OkHttpClient) = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("${BuildConfig.BASE_API_URL}/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
}

