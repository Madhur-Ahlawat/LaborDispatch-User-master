package com.UsreLaborDispatch1.www.sync.model

import com.google.firebase.database.Exclude

class JobLog : Job,FileInfo {

    var uid: String? = null

    var stop_latitude = ""
    var stop_longitude = ""

//    override var pdfUrl = ""
//        @Exclude
//        get() {return pdfUrl}

    open var imageUrl = ""
    open var imageFileName = ""


//    override var pdfFileName = ""
//        @Exclude
//        get() {return pdfFileName}

    constructor()
    constructor(
        jobNumber: String,
        jobDesc: String,
        jobRate: String,
        jobFlag: String,
        jobPin: String,
        uid: String,
        job_hours: String,
        start_time: String,
        stop_time: String,
        starting_teg: String,
        stoping_teg: String,
        start_latitude: String,
        start_longitude: String,
        stop_latitude: String,
        stop_longitude: String,
        priceQuote: String,
        quantity: String,
        pdfFile: String,
        imageFileName: String,
        imageUrl: String,
        companyId: String,
        jobDate: String
    ) : super(
        jobNumber,
        jobDesc,
        jobRate,
        jobFlag,
        jobPin,
        job_hours,
        start_time,
        stop_time,
        starting_teg,
        stoping_teg,
        start_latitude,
        start_longitude,
        pdfFile,
        imageFileName,
        imageUrl,
        priceQuote,
        quantity,
        companyId,
        jobDate
    ){
        this.uid = uid
        this.stop_latitude = stop_latitude
        this.stop_longitude = stop_longitude
        this.pdfFileName = pdfFileName

        this.imageFileName = imageFileName
        this.imageUrl = imageUrl

        this.companyId = companyId
    }

//    constructor(jobNumber: String, jobDesc: String, jobRate: String, jobFlag: String, jobPin: String, uid: String?, job_hours: String, start_time: String, stop_time: String, starting_teg: String, stoping_teg: String, start_latitude: String, start_longitude: String, stop_latitude: String, stop_longitude: String,
//                priceQuote:String, quantity:String,pdfFile:String ="",
//                imageFileName:String="",imageUrl:String="",
//                companyId:String, jobDate:String) : super(jobNumber, jobDesc, jobRate, jobFlag, jobPin, job_hours, start_time, stop_time, starting_teg, stoping_teg,start_latitude,start_longitude,priceQuote,quantity,jobDate) {
//        this.uid = uid
//        this.stop_latitude = stop_latitude
//        this.stop_longitude = stop_longitude
//        this.pdfFileName = pdfFile
//
//        this.imageFileName = imageFileName
//        this.imageUrl = imageUrl
//
//        this.companyId = companyId
//    }


    @Exclude
    override fun getFileName():String {
        return imageFileName
    }

    @Exclude
    override fun getFileAdress():String {
        return imageUrl
    }



    override var adress = ""
        @Exclude
        get() {return field}


}