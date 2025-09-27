package org.example.projectrr

import android.app.Application
import org.example.projectrr.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            config = {
                androidContext(applicationContext)
            },
        )
    }
}