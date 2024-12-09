package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.model.LatLng;

public class MapActivity extends AppCompatActivity {


    SupportMapFragment supportMapFragment;
    public static final String COORDINATES = "coordinates";
    public static void start(Activity activity , LatLng coordinates)
    {
        Intent intent = new Intent(activity,MapActivity.class);
        intent.putExtra(COORDINATES,coordinates);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        supportMapFragment = (SupportMapFragment)fragment;
        //

        if(getIntent().hasExtra(COORDINATES))
        {
            LatLng coordinates = (LatLng) getIntent().getSerializableExtra(COORDINATES);
            setMapMaker(coordinates);
        }
    }

    private void setMapMaker(final LatLng coordinates) {

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {


                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new com.google.android.gms.maps.model.LatLng(coordinates.getLatitude(),coordinates.getLongitude()))
                        .zoom(10)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

                mMap.addMarker(new MarkerOptions()
                        .position(new com.google.android.gms.maps.model.LatLng(coordinates.getLatitude(),coordinates.getLongitude()))
                        .title("Job Started")
                        .icon(getMarkerIcon("#0000FF")));
                // .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.spider)));


            }
        });
    }
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
