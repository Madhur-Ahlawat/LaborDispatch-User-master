package com.pakistanitests.ui.api

import com.google.gson.annotations.SerializedName


class RootModel(
    //  "to" changed to token
    @field:SerializedName("to") var token: String,
    notification: Notification

) {

    @SerializedName("data")
    private var notification: Notification



    fun getNotification(): Notification {
        return notification
    }

    fun setNotification(notification: Notification) {
        this.notification = notification
    }



    init {
        this.notification = notification
    }
}