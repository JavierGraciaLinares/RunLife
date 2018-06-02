package com.example.alumno.runlife;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.alumno.runlife.fragmentsEntrenamientos.Entrenamiento;
import com.example.alumno.runlife.herramientas.Popup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class InformacionEntrenamientoActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";
    //private LinearLayout popupInformacionEntrenamiento;
    public Entrenamiento entrenamientoVisualizado;
    TextView textViewPopupHistorialDistancia;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mMap;
    Dialog popup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_entrenamiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent anIntent = getIntent();
        String idEntrenamiento = anIntent.getStringExtra(Entrenamiento.IDENTRENAMIENTO);

        popup = Popup.generarPopUp(InformacionEntrenamientoActivity.this, R.layout.popup_informacion_entrenamiento, Popup.POPUP_NO_MODAL);
        textViewPopupHistorialDistancia = (TextView) popup.findViewById(R.id.textViewPopupHistorialDistancia);
        db.collection("Entrenamiento")
                .whereEqualTo(FieldPath.documentId(), idEntrenamiento)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot entrenamientoQuery : task.getResult()) {
                                //Añadir puntos de ruta
                                ArrayList<GeoPoint> puntoDeRutaArrayList = (ArrayList<GeoPoint>) entrenamientoQuery.get("Recorrido");
                                entrenamientoVisualizado = new Entrenamiento(new Timestamp(((Date) entrenamientoQuery.get(Entrenamiento.FECHAENTRENAMIENTO)).getTime()), (double) entrenamientoQuery.get(Entrenamiento.DISTANCIARECORRIDA), puntoDeRutaArrayList, (long) entrenamientoQuery.get(Entrenamiento.TIEMPOENTRENAMIENTO), (long) entrenamientoQuery.get(Entrenamiento.VELOCIDADMEDIA), entrenamientoQuery.getId());
                                textViewPopupHistorialDistancia.setText(String.format("%.2f", ((double) entrenamientoQuery.get(Entrenamiento.DISTANCIARECORRIDA) / 1000)) + " Km");

                            }
                            //Dibujar Ruta en el Mapa
                            GeoPoint anteriorPuntoDeRuta = entrenamientoVisualizado.getRecorrido().get(0);
                            LatLng sydney = new LatLng(anteriorPuntoDeRuta.getLatitude(), anteriorPuntoDeRuta.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Inicio"));
                            CameraUpdate posicionCamara = CameraUpdateFactory.newLatLngZoom(new LatLng(anteriorPuntoDeRuta.getLatitude(), anteriorPuntoDeRuta.getLongitude()), 14);
                            mMap.animateCamera(posicionCamara);
                            for (GeoPoint puntoDeRuta : entrenamientoVisualizado.getRecorrido()) {
                                Polyline line = mMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(anteriorPuntoDeRuta.getLatitude(), anteriorPuntoDeRuta.getLongitude()), new LatLng(puntoDeRuta.getLatitude(), puntoDeRuta.getLongitude()))
                                        .width(5)
                                        .color(Color.RED));
                                anteriorPuntoDeRuta = puntoDeRuta;
                            }

                        } else {
                            Log.d(TAGDEVELOP, "Error getting documents: ", task.getException());
                        }
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
