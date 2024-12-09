package com.UsreLaborDispatch1.www.sync.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.UsreLaborDispatch1.www.sync.helper.dialPhoneNumber
import com.pakistanitests.ui.api.ConstantKey
import java.lang.Exception

class CallReceiver:BroadcastReceiver()
{
    override fun onReceive(context: Context?, data: Intent?) {
      try {
          val  phoneNumber = data?.getStringExtra(ConstantKey.NUMBER)
          context?.dialPhoneNumber(phoneNumber)
      }
      catch (ex:Exception){
          Log.e("ex",ex.message)
      }
    }

}