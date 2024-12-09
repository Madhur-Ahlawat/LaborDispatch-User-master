package com.UsreLaborDispatch1.www.sync.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.UsreLaborDispatch1.www.sync.repo.FileReposioty

class FilesViewModel(context: Application): AndroidViewModel(context) {

   private val fileReposioty = FileReposioty.getInstance(context)

    fun getPdfFiles() =  fileReposioty.getPdfFiles()

    fun getImageFiles() =  fileReposioty.getImageFiles()
    fun deletePdfItem(fileName: String) = fileReposioty.deletePdfFileItem(fileName)

    fun deleteImageItem(fileName: String) = fileReposioty.deleteImageFileItem(fileName)

}