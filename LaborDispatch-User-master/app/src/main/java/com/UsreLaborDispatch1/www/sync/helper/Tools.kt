package com.UsreLaborDispatch1.www.sync.helper

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.R
import com.squareup.picasso.Picasso
import java.io.File


object Tools {

    fun checkInternetConnection(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            } else {
                try {
                    val activeNetworkInfo = connectivityManager.activeNetworkInfo
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                        Log.i("update_statut", "Network is available : true")
                        return true
                    }
                } catch (e: Exception) {
                    Log.i("update_statut", "" + e.message)
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ")
        return false
    }


    fun Context.isInternetConnected(): Boolean {
        if (checkInternetConnection(this))
            return true
        else {
            showMsg(getString(R.string.connection_error))
            return false
        }
    }


    fun Context.showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}

fun Context.showMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.downloadFile(fileName: String, url: String): Long? {

    try {
        //var fileName = url.substring(url.lastIndexOf('/') + 1)
        // fileName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1)
        //val file: File = Util.createDocumentFile(fileName, context)
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

        val request = DownloadManager.Request(Uri.parse(url))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file)) // Uri of the destination file
                .setTitle(fileName) // Title of the Download Notification
                .setDescription("Downloading") // Description of the Download Notification
                //.setRequiresCharging(false) // Set if charging is required to begin the download
                .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true) // Set if download is allowed on roaming network
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
        showMsg("Download Started")
        return downloadManager?.enqueue(request)

    } catch (ex: java.lang.Exception) {
        showMsg(ex.localizedMessage)
        return -2;
    }





}

fun Context.showImage(imageFile: String) {

    val alertDialog: AlertDialog
    val builder =
            AlertDialog.Builder(this)
    val view: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_banner, null)
    builder.setView(view)
    val imgHeader = view.findViewById<ImageView>(R.id.imgHeader)
val imgClose = view.findViewById<ImageButton>(R.id.imgClose)

    Picasso.get().load(imageFile).into(imgHeader)
    alertDialog = builder.create()
    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    alertDialog.show()
    imgClose.setOnClickListener{
        alertDialog.dismiss()
    }

}

fun Context.showImage(uri: Uri) {

    val alertDialog: AlertDialog
    val builder =
            AlertDialog.Builder(this)
    val view: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_banner, null)
    builder.setView(view)
    val imgHeader = view.findViewById<ImageView>(R.id.imgHeader)
    val imgClose = view.findViewById<ImageButton>(R.id.imgClose)

    Picasso.get().load(uri).placeholder(R.drawable.ic_loading).error(R.drawable.ic_error).into(imgHeader)
    alertDialog = builder.create()
    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    alertDialog.show()
    imgClose.setOnClickListener{
        alertDialog.dismiss()
    }

}

fun Context.getLocalPdfFile(pdfFileName: String): File {
    return File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), pdfFileName)
}

fun Context.getLocalPdfFilesFolder(): File {
    return File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "")
}

fun Context.getLocalImageFilesFolder(): File {
    return File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "")
}

fun Context.getLocalImageFile(pdfFileName: String): File {
    return File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), pdfFileName)
}


fun String.getFileNameFromUrl(): String {
    return substring(this.lastIndexOf('/') + 1)
}

fun Context.dialPhoneNumber(phoneNumber:String?)
{
    phoneNumber?.apply {
        val callIntent = Intent(Intent.ACTION_DIAL)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_NEW_TASK)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
}


