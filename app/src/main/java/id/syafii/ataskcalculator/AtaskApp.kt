/*
 * Created by Muhamad Syafii
 * , 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AtaskApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: AtaskApp
            private set
    }
}