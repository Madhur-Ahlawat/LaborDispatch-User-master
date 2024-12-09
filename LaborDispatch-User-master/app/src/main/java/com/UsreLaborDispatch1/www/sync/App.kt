package com.AdminLaborDispatch1.www.sync

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.google.firebase.messaging.FirebaseMessaging
import com.pakistanitests.ui.api.ConstantKey
import com.pakistanitests.ui.api.ConstantKey.TOPIC_USER

class App: MultiDexApplication()
{
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_USER)

    }
}