package com.UsreLaborDispatch1.www.sync.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.UsreLaborDispatch1.www.sync.helper.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class FileReposioty private constructor(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    companion object : SingletonHolder<FileReposioty, Context>(::FileReposioty)

   private val  PdfFiles = MutableLiveData<List<String>>()

    fun getPdfFiles(): MutableLiveData<List<String>> {
        initPdfFiles()
        return PdfFiles
    }

    private val  imageFiles = MutableLiveData<List<String>>()

    fun getImageFiles(): MutableLiveData<List<String>> {
        initImageFiles()
        return imageFiles
    }


    init {

    }

    private fun initPdfFiles() {
        val downloadsFolder = context.getLocalPdfFilesFolder()
        val arraylist=  ArrayList<String>()
        val localFiles = downloadsFolder.listFiles()
        localFiles.forEach {
            arraylist.add(it.name)
        }

        PdfFiles.postValue(arraylist)
    }

    private fun initImageFiles() {
        val downloadsFolder = context.getLocalImageFilesFolder()
        val arraylist=  ArrayList<String>()
        val localFiles = downloadsFolder.listFiles()
        localFiles.forEach {
            arraylist.add(it.name)
        }

        imageFiles.postValue(arraylist)
    }

    fun deletePdfFileItem(fileName: String) {
        context?.getLocalPdfFile(fileName)?.delete()
        initPdfFiles()
    }

    fun deleteImageFileItem(fileName: String) {
        context?.getLocalImageFile(fileName)?.delete()
        initImageFiles()
    }

}