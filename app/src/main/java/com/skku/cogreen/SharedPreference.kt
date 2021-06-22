package com.skku.cogreen

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPreference (context: Context){
    private val prefNm="pref"
    private val prefs=context.getSharedPreferences(prefNm,MODE_PRIVATE)

    var token:String?
        get() = prefs.getString("token",null)
        set(value){
            prefs.edit().putString("token",value).apply() }


    var missionIndex:Int?
        get() = prefs.getInt("missionIndex",0)
        set(value){
            if (value != null) {
                prefs.edit().putInt("missionIndex",value).apply()
            }
        }


    var submit:Boolean
        get() = prefs.getBoolean("submit",true)
        set(value){
            prefs.edit().putBoolean("token",value).apply() }
}