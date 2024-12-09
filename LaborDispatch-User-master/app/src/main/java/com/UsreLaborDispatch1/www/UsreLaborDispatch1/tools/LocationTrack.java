package com.UsreLaborDispatch1.www.UsreLaborDispatch1.tools;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.widget.Toast;


public class LocationTrack extends Service implements LocationListener {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private final Context mContext;


    boolean checkGPS = false;


    boolean checkNetwork = false;

    boolean canGetLocation = false;

    Location loc;
    double latitude;
    double longitude;
    LocationUpdate locationUpdate;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = (1000 * 60)/6;
    protected LocationManager locationManager;
    private String provider;

    public LocationTrack(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public LocationTrack(Context mContext, LocationUpdate locationUpdate) {
        this.mContext = mContext;
        this.locationUpdate = locationUpdate;
        getLocation();


    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // get GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS) {
                Toast.makeText(mContext, "GPS is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;

                this.provider = this.locationManager.getBestProvider(new Criteria(), true);
                if (checkGPS) {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.e("no permission","no permission");
                    }
                    locationManager.requestLocationUpdates(
                            this.provider,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {

//                        loc = locationManager
//                                .getLastKnownLocation(  this.provider);


//                        if(loc==null)
//                            loc = getLastKnownLocation();

                        if (locationUpdate != null) {
                           locationUpdate.onLocationUpdate(loc);
                        }

                    }


                }




            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("no permission","no permission");
        }
        Location location = null;
        for (String lastKnownLocation : this.locationManager.getProviders(true)) {
            Location lastKnownLocation2 = this.locationManager.getLastKnownLocation(lastKnownLocation);
            if (lastKnownLocation2 != null) {
                if (location == null || lastKnownLocation2.getAccuracy() < location.getAccuracy()) {
                    location = lastKnownLocation2;
                }
            }
        }
        return location;
    }


    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


        alertDialog.setTitle("GPS is not Enabled!");

        alertDialog.setMessage("Do you want to turn on GPS?");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    public void stopListener() {
        if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(LocationTrack.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        if(locationUpdate!=null)
        locationUpdate.onLocationUpdate(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

   public interface LocationUpdate{ public void onLocationUpdate(Location location);}
}
