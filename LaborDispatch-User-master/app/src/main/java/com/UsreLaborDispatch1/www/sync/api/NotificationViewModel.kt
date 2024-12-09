package com.pakistanitests.repo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.UsreLaborDispatch1.www.sync.data.PROGRESS_UPDATE
import com.pakistanitests.ui.api.Notification
import com.pakistanitests.ui.api.NotificationRepository

class NotificationViewModel(context: Application) : AndroidViewModel(context) {

    private val notificationRepository: NotificationRepository

    init {
        notificationRepository = NotificationRepository.getInstance(context)
    }

    fun getProgressUpdate(): MutableLiveData<PROGRESS_UPDATE> {
        return notificationRepository.progressUpdate
    }

    fun sendNotificatoin(notification: Notification) {
        Log.e("Job"," "+notification.companyId)
        notificationRepository.sendNotificationToUser(notification)
    }



}