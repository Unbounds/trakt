package com.unbounds.trakt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TraktApplication : Application() {

    companion object {
        private lateinit var INSTANCE: TraktApplication
        fun get() = INSTANCE
    }
}
