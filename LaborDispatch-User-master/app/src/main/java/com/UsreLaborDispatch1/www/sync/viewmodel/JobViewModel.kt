package com.UsreLaborDispatch1.www.sync.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.UsreLaborDispatch1.www.sync.model.Job
import com.UsreLaborDispatch1.www.sync.model.JobLog
import com.UsreLaborDispatch1.www.sync.repo.JobRepository
import java.io.File

class JobViewModel(context:Application): AndroidViewModel(context)
{

  val jobRepository = JobRepository.getInstance(context)

  fun getJobs() = jobRepository.updateAndGetJobs()

  fun getJobLogs() = jobRepository.updateAndGetJobLogs()

  fun addJob(job: Job)
  {
      jobRepository.addJob(job)
  }

  fun addJobLog(joblog: JobLog)
  {
    jobRepository.addJobLog(joblog)
  }

    fun updateJob(job: Job) {
      jobRepository.updateJob(job)
    }

    fun updateJobMajor(job: Job) {
      jobRepository.updateJobMajor(job)
    }

  fun deleteJob(job: String) {
    jobRepository.deleteJob(job)
  }

  fun deleteJobLog(uid: String) {
    jobRepository.deleteJobLog(uid)
  }

  fun updateJobLog(uid: String,jobLog: JobLog) {
    jobRepository.updateJobLog(uid,jobLog)
  }

  fun savePdfFile(fileName:String,file: File): MutableLiveData<Uri?> = jobRepository.savePdfFile(fileName,file)

  fun saveImageFile(fileName:String,file: File): MutableLiveData<Uri?> = jobRepository.saveImageFile(fileName,file)


  fun getImageFiles(context:Context) = jobRepository.getImageFiles(context)
  fun getPdfFiles(context: Context)= jobRepository.getPdfFiles(context)

  fun getImageUrl(fileName: String)= jobRepository.getImageUrl(fileName)

  fun resetData() =  jobRepository.resetData()



  fun getPdfUrl(fileName: String)= jobRepository.getPdfUrl(fileName)

}