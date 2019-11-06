package com.jdlozanom.webarticleextractor

import android.app.Application
import com.bitly.Bitly

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Bitly.initialize(this, getString(R.string.bitly_token))

    }
}