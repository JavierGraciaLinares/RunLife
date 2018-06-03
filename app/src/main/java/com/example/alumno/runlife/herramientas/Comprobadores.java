package com.example.alumno.runlife.herramientas;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by javi on 03/06/2018.
 */

public class Comprobadores {
    public static boolean gpsActivado(Context context){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
