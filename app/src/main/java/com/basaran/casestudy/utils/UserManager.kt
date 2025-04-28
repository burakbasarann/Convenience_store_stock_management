package com.basaran.casestudy.utils

import android.content.Context
import android.content.SharedPreferences

object UserManager {

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString("USER_ID", userId).apply()
    }

    fun getUserId(): String {
        return prefs.getString("USER_ID", "").toString()
    }
}
