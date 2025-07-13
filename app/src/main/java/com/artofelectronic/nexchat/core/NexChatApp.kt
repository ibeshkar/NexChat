package com.artofelectronic.nexchat.core

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NexChatApp : Application() {

    companion object {
        private lateinit var instance: NexChatApp

        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}