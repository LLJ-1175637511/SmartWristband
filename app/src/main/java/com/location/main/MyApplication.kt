package com.location.main

import android.app.Application
import com.llj.baselib.IOTLib
import com.llj.baselib.bean.UserConfigBean


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val bean = UserConfigBean(
            userId = "19230",
            appKey = "d0af7adbd2",
            deviceId = "25878",
            clientId = "1208",
            clientSecret = "e70ea36459"
        )
        IOTLib.init(applicationContext,bean)

    }
}