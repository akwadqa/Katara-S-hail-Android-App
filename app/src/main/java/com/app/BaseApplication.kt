package com.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver

class BaseApplication : Application(), LifecycleObserver {

    init {
        instance = this
    }


    companion object {
        lateinit var instance: com.app.BaseApplication
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

    }


    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }
}