package com.pakistanitests.ui.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers

import retrofit2.http.POST


interface ApiInterface {
    @Headers("Authorization: key= ${ConstantKey.SERVER_KEY}" , "Content-Type:application/json")
    @POST("fcm/send")
    fun sendNotification(@Body root: RootModel?): Call<ResponseBody?>?
}