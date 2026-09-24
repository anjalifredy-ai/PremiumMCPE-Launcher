package com.premiummcpe.launcher

import android.app.Application

class PremiumMCPEApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Future: init crash reporter, DI, native preloader paths etc.
    }
}
