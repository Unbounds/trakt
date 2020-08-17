package com.unbounds.trakt.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unbounds.trakt.utils.Event
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun providesTokenStorage(application: Application) = application.getSharedPreferences("Login", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    @Named("auth")
    fun providesAuthErrorMutableLiveData() = MutableLiveData<Event<Unit>>()

    @Singleton
    @Provides
    @Named("auth")
    fun providesAuthErrorLiveData(@Named("auth") mutable: MutableLiveData<Event<Unit>>): LiveData<Event<Unit>> = mutable
}

