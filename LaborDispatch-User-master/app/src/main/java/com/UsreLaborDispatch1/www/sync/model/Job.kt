package com.UsreLaborDispatch1.www.sync.model

import com.google.firebase.database.Exclude
import java.io.Serializable


open class Job : Serializable, FileInfo{

    var jobNumber: String = ""
    var jobDesc: String = ""
    var jobRate: String = ""
    var jobFlag: String = ""
    var jobPin: String = ""
    var job_hours = ""
    var start_time = ""
    var stop_time = ""
    var starting_teg = ""
    var stoping_teg = ""
    var start_latitude = ""
    var start_longitude = ""
    var pdfUrl = ""
    var pdfFileName = ""
    var companyId = ""
    var priceQuote = ""
    var quantity =""
    open var adress =""
    var jobDate =""

    constructor()
    constructor(
        jobNumber: String,
        jobDesc: String,
        jobRate: String,
        jobFlag: String,
        jobPin: String,
        job_hours: String,
        start_time: String,
        stop_time: String,
        starting_teg: String,
        stoping_teg: String,
        start_latitude: String,
        start_longitude: String,
        pdfUrl: String,
        pdfFileName: String,
        companyId: String,
        priceQuote: String,
        quantity: String,
        adress: String,
        jobDate: String
    ) {
        this.jobNumber = jobNumber
        this.jobDesc = jobDesc
        this.jobRate = jobRate
        this.jobFlag = jobFlag
        this.jobPin = jobPin
        this.job_hours = job_hours
        this.start_time = start_time
        this.stop_time = stop_time
        this.starting_teg = starting_teg
        this.stoping_teg = stoping_teg
        this.start_latitude = start_latitude
        this.start_longitude = start_longitude
        this.pdfUrl = pdfUrl
        this.pdfFileName = pdfFileName
        this.companyId = companyId
        this.priceQuote = priceQuote
        this.quantity = quantity
        this.adress = adress
        this.jobDate = jobDate
    }


    @Exclude
    fun getMinorUpdate():HashMap<String,Any>
    {
        val hashmap = HashMap<String,Any>()
        hashmap.put("jobFlag",jobFlag)
        hashmap.put("job_hours",job_hours)
        hashmap.put("start_time",start_time)
        hashmap.put("stop_time",stop_time)
        hashmap.put("starting_teg",starting_teg)
        hashmap.put("stoping_teg",stoping_teg)
        hashmap.put("start_latitude",start_latitude)
        hashmap.put("start_longitude",start_longitude)
        return hashmap
    }

    @Exclude
    fun getMajorUpdate():HashMap<String,Any>
    {
        val hashmap = HashMap<String,Any>()
        hashmap.put("jobDesc",jobDesc)
        hashmap.put("jobPin",jobPin)
        hashmap.put("job_hours",job_hours)
        hashmap.put("jobRate",jobRate)

        if(!pdfUrl.isNullOrEmpty()) {
            hashmap.put("pdfUrl", pdfUrl)
            hashmap.put("pdfFileName", pdfFileName)
        }
        return hashmap
    }

    @Exclude
    override fun getFileName():String {
        return pdfFileName
    }

    @Exclude
    override fun getFileAdress():String {
        return pdfUrl
    }


}