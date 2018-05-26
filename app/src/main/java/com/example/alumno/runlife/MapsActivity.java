package com.example.alumno.runlife;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FrameLayout implements OnMapReadyCallback {

    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";

    private GoogleMap mMap;

    public MapsActivity(@NonNull Context context) {
        super(context);
    }

   /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(41.6563497, -0.9147491000000001);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        Log.i(TAGDEBUG,"DESDE MAPASAAAAAAAAA");*/
        //Log.i(TAGDEBUG,"DESDE MAPAS: " + InformacionEntrenamientoActivity.entrenamientoVisualizado.getIdEntrenamiento());
/*
        Polyline line = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(41.6563497, -0.9147491000000001), new LatLng(41.644172 , -0.914604))
                .add(new LatLng(41.648758 , -0.914582), new LatLng(41.644172 , -0.914604))
                .add(new LatLng(41.649383  , -0.914582), new LatLng(41.649383  , -0.911696))
                .add(new LatLng(41.651235   , -0.910436), new LatLng(41.649383  , -0.911696))
                .width(5)
                .color(Color.RED));*/
    }


}
