package com.pakistanitests.ui.api

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.AdminLaborDispatch1.www.api.ApiClient
import com.UsreLaborDispatch1.www.sync.data.PROGRESS_UPDATE
import com.UsreLaborDispatch1.www.sync.helper.SingletonHolder

import com.pakistanitests.ui.api.ConstantKey.TARGET_TOPIC

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationRepository private constructor(context: Context) {

    companion object : SingletonHolder<NotificationRepository, Context>(::NotificationRepository)
    val progressUpdate = MutableLiveData<PROGRESS_UPDATE>(PROGRESS_UPDATE.IDLE)

     fun sendNotificationToUser(notification: Notification) {
        val rootModel =
            RootModel(TARGET_TOPIC, notification)
        val apiService =
            ApiClient.client!!.create(ApiInterface::class.java)
        val responseBodyCall =
            apiService.sendNotification(rootModel)

        responseBodyCall?.enqueue(object : Callback<ResponseBody?> {
            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
            }

            override fun onResponse(
                call: Call<ResponseBody?>?,
                response: Response<ResponseBody?>?
            ) {
                progressUpdate.postValue(PROGRESS_UPDATE.SUCCESS)
            }

        })
    }
}