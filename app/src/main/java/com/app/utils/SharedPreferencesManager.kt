package com.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object SharedPreferencesManager {
    private var mInstance: SharedPreferences? = null

    private fun getInstance(): SharedPreferences {
        if (mInstance == null)
            mInstance = PreferenceManager.getDefaultSharedPreferences(com.app.BaseApplication.instance.applicationContext)
        return mInstance!!
    }

    fun getInt(key: String): Int {
        return getInstance().getInt(key, 0)
    }

    fun put(key: String, value: Int) {
        val editor = getInstance().edit()
        editor.putInt(key, value)
        // Commit the edits!
        editor.apply()
    }

    fun put(key: String, value: Long) {
        val editor = getInstance().edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String): Long {
        return getInstance().getLong(key, 0)
    }

    fun put(key: String, value: Float) {
        val editor = getInstance().edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String): Float? {
        return getInstance().getFloat(key, 0f)
    }

    fun put(key: String, value: Boolean) {
        val editor = getInstance().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return getInstance().getBoolean(key, false)
    }

    fun put(key: String, value: String) {
        val editor = getInstance().edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return getInstance().getString(key, "")!!
    }

    fun getLanguageString(key: String): String {
        return getInstance().getString(key, "en")!!
    }

    fun clearAllPreferences(mContext: Context) {
        val editor = getInstance().edit()
        editor.clear()
        editor.apply()
    }
}