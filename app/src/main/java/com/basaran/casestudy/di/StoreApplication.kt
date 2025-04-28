package com.basaran.casestudy.di

import android.app.Application
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UserManager.init(this)
    }
}