package com.UsreLaborDispatch1.www.sync.data

import java.io.Serializable

data class JobLocation (val streetNo:String,
                       val streetName:String,
                        val city:String,
                        val state:String,
                        val zipCode:String,
                        val tellePhone:String,
                        val longitude:Double,
                        val latitude:Double):Serializable{

    override fun toString(): String {
        return "$streetNo, $streetName, $city, $state, $zipCode, $tellePhone"
    }
}