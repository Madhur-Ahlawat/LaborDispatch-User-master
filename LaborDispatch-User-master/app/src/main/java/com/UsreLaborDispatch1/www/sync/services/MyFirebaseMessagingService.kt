package com.UsreLaborDispatch1.www.sync.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.MainActivity
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.R
import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager
import com.UsreLaborDispatch1.www.sync.helper.showMsg
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pakistanitests.ui.api.ConstantKey

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception


private const val TAG = "MyFirebaseMessagingServ"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)

        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic(ConstantKey.TOPIC_USER)

        sendRegistrationToServer(refreshedToken)
    }
    private fun sendRegistrationToServer(token: String?) {
        Log.e("TOKEN", token!!)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, remoteMessage.data.toString())

        var title = ""
        var body = ""
        var jobPin =""
        var phoneNumber =""

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            title = remoteMessage.data.get(ConstantKey.TITLE)!!
            body = remoteMessage.data.get(ConstantKey.BODY)!!
            jobPin = remoteMessage.data.get(ConstantKey.JOB_PIN)!!
            phoneNumber = remoteMessage.data.get(ConstantKey.NUMBER)!!
        }else if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!)
            title = remoteMessage.notification!!.title!!
            body = remoteMessage.notification!!.body!!
        }

        if (jobPin == PreferenceManager.getInstance(this).userPin)
            title?.let { title ->
                body?.apply {
                    showNotification(title, this, phoneNumber)
                }
            }

    }

    fun showNotification(title: String, body: String, phoneNumber: String?) {
        try {
            Log.e(TAG, "noitificatoin started")
            val notificationId = 100
            val chanelid = "DAILY_TEST"
            val intent = Intent(this, MainActivity::class.java)

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // you must create a notification channel for API 26 and Above
                val name = "UserLaborDispatch"
                val description = "UserLaborDispatch Notification"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(chanelid, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }

            val mBuilder = NotificationCompat.Builder(this, chanelid)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // cancel the notification when clicked

            //add a btn to the Notification with a corresponding intent
            phoneNumber?.run {
                if (!isNullOrEmpty()) {
                    val callIntent = Intent(this@MyFirebaseMessagingService,CallReceiver::class.java)
                    callIntent.putExtra(ConstantKey.NUMBER,phoneNumber)
                    val pendingIntentCall = PendingIntent.getBroadcast(getApplicationContext(), 234324243, callIntent, 0)
                    mBuilder.addAction(R.drawable.ic_phone, "Call Customer", pendingIntentCall)
                }
            }

            val notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, mBuilder.build());
            Log.e(TAG, "noitificatoin")
        } catch (ex: Exception) {
            showMsg(ex.localizedMessage)
        }

    }

}