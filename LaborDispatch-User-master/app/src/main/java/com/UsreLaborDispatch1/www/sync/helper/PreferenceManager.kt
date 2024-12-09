package com.UsreLaborDispatch1.www.sync.helper

import android.content.Context


const val SHARED_PRIVATE = "SHARED_PRIVATE"

const val USER_PIN = "USER_PIN"
const val COMPANY_ID = "COMPANY_ID"
const val TRAIL_START_DATETIME = "TRAIL_START_DATETIME"

class PreferenceManager private constructor(context: Context) {
    companion object : SingletonHolder<PreferenceManager, Context>(::PreferenceManager)

    private val sharedPreferences = context.getSharedPreferences(SHARED_PRIVATE, Context.MODE_PRIVATE)


    var userPin: String?
        get() {
            return sharedPreferences.getString(USER_PIN, "")
        }
        set(value) {
            sharedPreferences.edit().putString(USER_PIN, value).apply()
        }

    var companyId: String
        get() {
            val comp = sharedPreferences.getString(COMPANY_ID, "")
            comp?.apply { return comp }
            return ""
        }
        set(value) {
            sharedPreferences.edit().putString(COMPANY_ID, value).apply()
        }

    var trailStartTime: Long
        get() {
            val startTime = sharedPreferences.getLong(TRAIL_START_DATETIME, 0L)
            if (startTime == 0L) {
                trailStartTime = System.currentTimeMillis()
                return System.currentTimeMillis()
            }
            return startTime
        }
        set(value) {
            sharedPreferences.edit().putLong(TRAIL_START_DATETIME, value).apply()
        }

}