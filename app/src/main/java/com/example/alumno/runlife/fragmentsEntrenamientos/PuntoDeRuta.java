package com.example.alumno.runlife.fragmentsEntrenamientos;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by javi on 17/05/2018.
 */

public class PuntoDeRuta {
    GeoPoint geopunto;
    Timestamp tiempo;

    public PuntoDeRuta(Location localizacion) {
        this.geopunto = new GeoPoint(localizacion.getLatitude(),localizacion.getLongitude());
        this.tiempo = new Timestamp(System.currentTimeMillis());
    }

    public PuntoDeRuta(GeoPoint geopunto) {
        this.geopunto = geopunto;
        this.tiempo = new Timestamp(System.currentTimeMillis());
    }


    public GeoPoint getGeopunto() {
        return geopunto;
    }

    public void setGeopunto(GeoPoint geopunto) {
        this.geopunto = geopunto;
    }

    public Timestamp getTiempo() {
        return tiempo;
    }

    public void setTiempo(Timestamp tiempo) {
        this.tiempo = tiempo;
    }
}
