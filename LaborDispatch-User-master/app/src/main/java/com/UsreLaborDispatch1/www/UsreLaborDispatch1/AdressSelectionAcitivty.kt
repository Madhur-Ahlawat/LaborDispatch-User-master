package com.UsreLaborDispatch1.www.UsreLaborDispatch1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.databinding.ActivityAdressSelectionAcitivtyBinding
import com.UsreLaborDispatch1.www.sync.data.JobLocation
import com.UsreLaborDispatch1.www.sync.data.NODES
import com.UsreLaborDispatch1.www.sync.helper.showMsg
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD



class AdressSelectionAcitivty : AppCompatActivity(), LocationListener {

    lateinit var binder: ActivityAdressSelectionAcitivtyBinding
    var supportMapFragment: SupportMapFragment? = null
    var targetCoords: LatLng? = null
    var currentCoords: LatLng? = null
    var jobViewModel: JobViewModel? = null

    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null

    var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityAdressSelectionAcitivtyBinding.inflate(layoutInflater)
        setContentView(binder.root)

        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)

        locationManager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
            0f, this)

        val fmanager: FragmentManager = supportFragmentManager
        val fragment: Fragment? = fmanager.findFragmentById(R.id.map)
        supportMapFragment = fragment as SupportMapFragment


        binder.etStreetNo.inputType = InputType.TYPE_NULL
        binder.etStreetName.inputType = InputType.TYPE_NULL
        binder.etCity.inputType = InputType.TYPE_NULL
        binder.etState.inputType = InputType.TYPE_NULL
        binder.etZipCode.inputType = InputType.TYPE_NULL
        binder.etTell.inputType = InputType.TYPE_NULL


        if(intent.hasExtra(NODES.JOB_LOCATION))
        {
            filldata(intent.getSerializableExtra(NODES.JOB_LOCATION) as JobLocation)
        }
    }

    private fun filldata(jobLocation: JobLocation) {
        targetCoords = LatLng(jobLocation.latitude,jobLocation.longitude)
        processCoordinates()

        binder.etStreetNo.setText(jobLocation.streetNo)
        binder.etStreetName.setText(jobLocation.streetName)
        binder.etCity.setText(jobLocation.city)
        binder.etState.setText(jobLocation.state)
        binder.etZipCode.setText(jobLocation.zipCode)
        binder.etTell.setText(jobLocation.tellePhone)

    }



    var adress: String = ""


    fun processCoordinates() {
        if (targetCoords != null) {
            setMapMaker()
//            targetCoords = LatLng(31.418715, 73.079109)
//            setMapMaker()//31.418715,73.079109
        }
        else
            showMsg("Unable to find Adress")
    }




    var googleMap: GoogleMap? = null

    private fun setMapMaker() {

        supportMapFragment?.getMapAsync { mMap ->
            this.googleMap = mMap

            //  mMap.clear() //clear old markers
            if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@getMapAsync
            }
            mMap.isMyLocationEnabled = true

            if (targetCoords != null) {
                targetCoords?.apply {
                    val googlePlex = CameraPosition.builder()
                            .target(
                                    com.google.android.gms.maps.model.LatLng(
                                            latitude,
                                            longitude
                                    )
                            )
                            .zoom(12f)
                            //.tilt(45f)
                            //  .bearing(bearing)
                            .build()
                    mMap.addMarker(
                        MarkerOptions()
                            .position(
                                com.google.android.gms.maps.model.LatLng(
                                    latitude,
                                    longitude
                                )
                            )
                            .title("Car Location")
                    )
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex))

                }
            }
                currentCoords?.apply {
//                mMap.clear()
                    mMap.addMarker(
                        MarkerOptions()
                            .position(
                                com.google.android.gms.maps.model.LatLng(
                                    latitude,
                                    longitude
                                )
                            )
                            .title("Current Location")
                    )
                }


            // .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.spider)));
        }
    }

    companion object {
        fun start(activity: Activity, jobLocation: JobLocation?) {
            val intent = Intent(activity,AdressSelectionAcitivty::class.java)
            jobLocation?.apply {
                intent.putExtra(NODES.JOB_LOCATION, this)
            }
            activity.startActivity(intent)
        }
    }

    override fun onLocationChanged(l: Location) {
        currentCoords = LatLng(l.latitude, l.longitude)
        if (isFirst) {
            setMapMaker()
            isFirst = false
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onProviderDisabled(p0: String?) {
        TODO("Not yet implemented")
    }


}