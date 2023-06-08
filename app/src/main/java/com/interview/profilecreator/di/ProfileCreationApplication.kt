package com.interview.profilecreator.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProfileCreationApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}