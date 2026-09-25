package com.premiummcpe.launcher

import android.app.Application
import com.premiummcpe.launcher.data.download.VersionInstallManager

class PremiumMCPEApp : Application() {
    override fun onCreate() {
        super.onCreate()
        VersionInstallManager.init(this)
    }
}
