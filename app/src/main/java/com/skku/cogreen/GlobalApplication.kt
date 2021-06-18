package com.skku.cogreen

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    companion object{
        lateinit var prefs:SharedPreference
    }

    override fun onCreate() {

        KakaoSdk.init(this, getString(R.string.native_key))

        prefs= SharedPreference(applicationContext)
        super.onCreate()

    }
}