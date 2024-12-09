package com.UsreLaborDispatch1.www.sync.repo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper
import com.UsreLaborDispatch1.www.sync.data.NODES
import com.UsreLaborDispatch1.www.sync.data.PROGRESS_UPDATE
import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager
import com.UsreLaborDispatch1.www.sync.helper.SingletonHolder
import com.UsreLaborDispatch1.www.sync.model.FileInfo
import com.UsreLaborDispatch1.www.sync.model.Job
import com.UsreLaborDispatch1.www.sync.model.JobLog
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext


class JobRepository private constructor(context: Context) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    companion object : SingletonHolder<JobRepository, Context>(::JobRepository)

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance()
    val progressUpdate = MutableLiveData<PROGRESS_UPDATE>(PROGRESS_UPDATE.IDLE)

    val jobs = MutableLiveData<List<Job>>()
    fun updateAndGetJobs(): MutableLiveData<List<Job>> {
        if (jobs.value.isNullOrEmpty())
            loadJobs()
        return jobs
    }

    val jobLogs = MutableLiveData<List<JobLog>>()
    fun updateAndGetJobLogs(): MutableLiveData<List<JobLog>> {
        if (jobLogs.value.isNullOrEmpty())
            loadJobLogs()
        return jobLogs
    }

    val preferenceManager: PreferenceManager

    fun getFirebaseDatabase(): DatabaseReference {
//        var companyId = preferenceManager.companyId
//        if(companyId.isNullOrEmpty())
//            companyId = "qw4hd"
        return firebaseDatabase.reference//.child(companyId)
    }

    fun getFirebaseStorage(): StorageReference {
        var companyId = preferenceManager.companyId
        if (companyId.isNullOrEmpty())
            companyId = "qw4hd"
        return firebaseStorage.reference.child(companyId)
    }

    init {
        preferenceManager = PreferenceManager.getInstance(context)
    }

    private fun loadJobLogs() {
        progressUpdate.postValue(PROGRESS_UPDATE.WORKING)
        val firebaseNode = getFirebaseDatabase().child(NODES.JOB_LOG)

        firebaseNode.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                processSnapShotLog(snapshot)
                progressUpdate.postValue(PROGRESS_UPDATE.SUCCESS)
            }
        })
    }

    private fun loadJobs() {
        progressUpdate.postValue(PROGRESS_UPDATE.WORKING)
        val firebaseNode = getFirebaseDatabase().child(NODES.JOBS)

        firebaseNode.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                processSnapShot(snapshot)
                progressUpdate.postValue(PROGRESS_UPDATE.SUCCESS)
            }
        })
    }

    private fun processSnapShot(snapshot: DataSnapshot) {
        var jobsList = ArrayList<Job>()
        launch {
            snapshot?.children?.forEach {
                val item = it.getValue(Job::class.java)
                item?.let {
                    jobsList.add(it)
                }

            }
            jobs.postValue(jobsList)
        }
    }

    private fun processSnapShotLog(snapshot: DataSnapshot) {
        var jobsList = ArrayList<JobLog>()
        launch {
            snapshot?.children?.forEach {
                val item = it.getValue(JobLog::class.java)
                item?.let {
                    jobsList.add(it)
                }

            }
            jobLogs.postValue(jobsList)
        }
    }

    fun addJob(job: Job) {
        val firebaseNode = getFirebaseDatabase().child(NODES.JOBS).child("${job.jobNumber}")
        firebaseNode.setValue(job).addOnSuccessListener {
            //"Success".logErrorMessage()
        }.addOnFailureListener {
            progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
        }
    }

    fun addJobLog(joblog: JobLog) {
        val firebaseNode = getFirebaseDatabase().child(NODES.JOB_LOG).child("${joblog.uid}")
        firebaseNode.setValue(joblog).addOnSuccessListener {
            //"Success".logErrorMessage()
        }.addOnFailureListener {
            progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
        }
    }

    fun updateJob(job: Job) {
        val firebaseNode = getFirebaseDatabase().child(NODES.JOBS).child("${job.jobNumber}")
        firebaseNode.updateChildren(job.getMinorUpdate()).addOnSuccessListener {
            //"Success".logErrorMessage()
        }.addOnFailureListener {
            progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
        }

    }

    fun updateJobMajor(job: Job) {
        val firebaseNode = getFirebaseDatabase().child(NODES.JOBS).child("${job.jobNumber}")
        firebaseNode.updateChildren(job.getMajorUpdate()).addOnSuccessListener {
            //"Success".logErrorMessage()
        }.addOnFailureListener {
            progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
        }
    }

    fun deleteJob(job: String) {
        val firebaseNode = getFirebaseDatabase().child(NODES.JOBS).child("${job}")
        firebaseNode.removeValue()
    }

    fun deleteJobLog(uid: String) {
        val firebaseNode = getFirebaseDatabase().child(NODES.JOB_LOG).child("$uid")
        firebaseNode.removeValue().addOnSuccessListener {
            progressUpdate.postValue(PROGRESS_UPDATE.SUCCESS)
        }
                .addOnFailureListener {
                    progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
                }
    }

    fun updateJobLog(uid: String, jobLog: JobLog) {
        val firebaseNode = getFirebaseDatabase().child(NODES.JOB_LOG).child("${uid}")
        firebaseNode.updateChildren(jobLog.getMajorUpdate()).addOnSuccessListener {
            //"Success".logErrorMessage()
        }.addOnFailureListener {
            progressUpdate.postValue(PROGRESS_UPDATE.ERROR)
        }
    }


    fun savePdfFile(fileName: String, file: File): MutableLiveData<Uri?> {
        val savedPdfDocument = MutableLiveData<Uri?>()

        val reference: StorageReference = getFirebaseStorage().child(NODES.FILE_EXT).child(fileName)
        //val file = File(path)

        val data: ByteArray = file.readBytes()
        val uploadTask = reference.putBytes(data)
        uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> {
            @Throws(Exception::class)
            override fun then(task: Task<UploadTask.TaskSnapshot?>): Task<Uri?>? {
                if (!task.isSuccessful()) {

                }

                // Continue with the task to get the download URL
                return reference.downloadUrl
            }
        }).addOnCompleteListener(object : OnCompleteListener<Uri?> {
            override fun onComplete(task: Task<Uri?>) {
                if (task.isSuccessful()) {
                    val downloadUri: Uri? = task.getResult()
                    savedPdfDocument.postValue(downloadUri)
                } else {
                    savedPdfDocument.postValue(null)
                }
            }
        })

        return savedPdfDocument
    }

    fun saveImageFile(fileName: String, file: File): MutableLiveData<Uri?> {
        val savedImageDocument = MutableLiveData<Uri?>()

        val reference: StorageReference = getFirebaseStorage().child(NODES.IMAGE_FOLDER).child(fileName)
        //val file = File(path)

        val data: ByteArray = file.readBytes()
        val uploadTask = reference.putBytes(data)
        uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> {
            @Throws(Exception::class)
            override fun then(task: Task<UploadTask.TaskSnapshot?>): Task<Uri?>? {
                if (!task.isSuccessful()) {

                }

                // Continue with the task to get the download URL
                return reference.downloadUrl
            }
        }).addOnCompleteListener(object : OnCompleteListener<Uri?> {
            override fun onComplete(task: Task<Uri?>) {
                if (task.isSuccessful()) {
                    val downloadUri: Uri? = task.getResult()
                    savedImageDocument.postValue(downloadUri)
                } else {
                    savedImageDocument.postValue(null)
                }
            }
        })

        return savedImageDocument
    }

    fun getImageFiles(context:Context): MutableLiveData<List<JobLog>> {
        var images: MutableLiveData<List<JobLog>> = MutableLiveData()
        val databaseHelper = DatabaseHelper.getInstance(context)

       images.postValue( databaseHelper.getImages(preferenceManager.companyId,preferenceManager.userPin))

        return images
    }

    fun getImageUrl(fileName: String): MutableLiveData<Uri> {
        val images = MutableLiveData<Uri>()
        val reference = getFirebaseStorage().child(NODES.IMAGE_FOLDER).child(fileName)
        reference.downloadUrl.addOnSuccessListener {
            images.postValue(it)
        }
        return images;
    }

    fun resetData() {
        jobLogs.value = (null)
        jobs.value = (null)
    }

    fun getPdfFiles(context: Context): MutableLiveData<List<Job>> {
        var images: MutableLiveData<List<Job>> = MutableLiveData()
        val databaseHelper = DatabaseHelper.getInstance(context)

        images.postValue( databaseHelper.getPdfFiles(preferenceManager.companyId,preferenceManager.userPin))

        return images
    }

    fun getPdfUrl(fileName: String): MutableLiveData<Uri> {
        val images = MutableLiveData<Uri>()
        val reference = getFirebaseStorage().child(NODES.FILE_EXT).child(fileName)
        reference.downloadUrl.addOnSuccessListener {
            images.postValue(it)
        }
        return images
    }


}