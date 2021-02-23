package com.example.sunnyweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication :Application(){

    companion object{//类名+TOKEN静态
        lateinit var context: Context
        const val TOKEN = "8qXCoD2PtPEoRroM"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}