package com.example.alumno.runlife.entrenamiento;

import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.alumno.runlife.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by javi on 17/05/2018.
 */

public class EntrenamientoDatos {

    public static final String FECHAENTRENAMIENTO = "FechaEntrenamiento";
    public static final String DISTANCIARECORRIDA = "DistanciaRecorrida";
    public static final String TIEMPOENTRENAMIENTO = "TiempoEntrenamiento";
    public static final String VELOCIDADMEDIA = "VelocidadMedia";
    public static final String IDENTRENAMIENTO = "IDEntrenamiento";

    public static final String ENTRENAMIENTO_TIPO = "ENTRENAMIENTO_TIPO";
    public static final int ENTRENAMIENTO_TIPO_LIBRE = 1;
    public static final int ENTRENAMIENTO_TIPO_DISTANCIA = 2;


    private String idEntrenamiento;

    private Timestamp horaDelEntrenamiento;

    private int tipoEntrenamiento;

    private long velocidadMedia;
    public double distanciaRecorrida;
    private long tiempoEntrenamiento;
    private ArrayList<GeoPoint> recorrido;

    private int distanciaObjetivo;
    private boolean enMarcha;

    public int numeroLocalizacion;
    private boolean calibrado;
    private Location localizacionAnterior;
    private Long tiempoAnterior;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public EntrenamientoDatos() {
        this.numeroLocalizacion = 0;
        this.enMarcha = false;
        this.distanciaRecorrida = 0;
        this.recorrido = new ArrayList<>();
        this.horaDelEntrenamiento = new Timestamp(System.currentTimeMillis());
        this.velocidadMedia = 0;
    }

    public EntrenamientoDatos(Timestamp horaDelEntrenamiento, double distanciaRecorrida, ArrayList<GeoPoint> recorrido, long tiempoEntrenamiento, long velocidadMedia, String idEntrenamiento) {
        this.horaDelEntrenamiento = horaDelEntrenamiento;
        this.distanciaRecorrida = distanciaRecorrida;
        this.recorrido = recorrido;
        this.tiempoEntrenamiento = tiempoEntrenamiento;
        this.velocidadMedia = velocidadMedia;
        this.idEntrenamiento = idEntrenamiento;
    }

    public double calcularKmXHMedia(long elapsedRealtime, long crono) {
        tiempoEntrenamiento = elapsedRealtime - crono;
        Log.i(MainActivity.TAGDEVELOP,"Distancia entre 2 puntos : " + distanciaRecorrida + "      tiempoEntrenamiento: " + tiempoEntrenamiento/1000);
        return ((distanciaRecorrida / (tiempoEntrenamiento / 1000))*3.6);//Km/H
    }

    public double calcularKmXHActuales(long tiempoAnterior, double distanciaEntreDosPuntos) {  //¡¡¡¡¡¡¡¡¡¡¡REVISAAAAAAR!!!
        long diferenciaTiempos = tiempoAnterior-tiempoEntrenamiento;
        Log.i(MainActivity.TAGDEVELOP,"Distancia entre 2 puntos : " + distanciaEntreDosPuntos + "      tiempo entre dos puntos: " + diferenciaTiempos/1000);
        return ((distanciaEntreDosPuntos / (diferenciaTiempos / 1000))*3.6);//Metros/Segundo
    }

    public void anyadirPuntoDeRutaRecorrido(Location localizacion) {
        recorrido.add(new GeoPoint(localizacion.getLatitude(),localizacion.getLongitude()));
    }

    public void insertarEntrenamientoEnFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> entidadEntrenamiento = new HashMap<>();
        entidadEntrenamiento.put(FECHAENTRENAMIENTO, horaDelEntrenamiento);
        entidadEntrenamiento.put("Usuario", MainActivity.cuentaGoogleUsuario.getEmail());
        entidadEntrenamiento.put(DISTANCIARECORRIDA, distanciaRecorrida);
        entidadEntrenamiento.put(TIEMPOENTRENAMIENTO, tiempoEntrenamiento);
        entidadEntrenamiento.put(VELOCIDADMEDIA, velocidadMedia);

        entidadEntrenamiento.put("Recorrido",recorrido);
        db.collection("EntrenamientoDatos")
                .add(entidadEntrenamiento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(MainActivity.TAGDEBUG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(MainActivity.TAGDEBUG, "Error adding document", e);
                    }
                });

    }


    public String getDistanciarecorridaEnKMString() {
        return String.format("%.2f", (this.getDistanciaRecorrida() / 1000)) + " km";
    }

    public Location getLocalizacionAnterior() {
        return localizacionAnterior;
    }

    public void setLocalizacionAnterior(Location localizacionAnterior) {
        this.localizacionAnterior = localizacionAnterior;
    }

    public Long getTiempoAnterior() {
        return tiempoAnterior;
    }

    public void setTiempoAnterior(Long tiempoAnterior) {
        this.tiempoAnterior = tiempoAnterior;
    }

    public int getnumeroLocalizacion() {
        return numeroLocalizacion;
    }

    public void setnumeroLocalizacionn(int primeraLocalizacion) {
        this.numeroLocalizacion = primeraLocalizacion;
    }

    public boolean isEnMarcha() {
        return enMarcha;
    }

    public void setEnMarcha(boolean enMarcha) {
        this.enMarcha = enMarcha;
    }

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public Timestamp getHoraDelEntrenamiento() {
        return horaDelEntrenamiento;
    }

    public void setHoraDelEntrenamiento(Timestamp horaDelEntrenamiento) {
        this.horaDelEntrenamiento = horaDelEntrenamiento;
    }

    public ArrayList<GeoPoint> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(ArrayList<GeoPoint> recorrido) {
        this.recorrido = recorrido;
    }

    public long getTiempoEntrenamiento() {
        return tiempoEntrenamiento;
    }

    public void setTiempoEntrenamiento(long tiempoEntrenamiento) {
        this.tiempoEntrenamiento = tiempoEntrenamiento;
    }

    public long getVelocidadMedia() {
        return velocidadMedia;
    }

    public void setVelocidadMedia(long velocidadMedia) {
        this.velocidadMedia = velocidadMedia;
    }

    public String getIdEntrenamiento() {
        return idEntrenamiento;
    }

    public void setIdEntrenamiento(String idEntrenamiento) {
        this.idEntrenamiento = idEntrenamiento;
    }

    public boolean isCalibrado() {
        return calibrado;
    }

    public void setCalibrado(boolean calibrado) {
        this.calibrado = calibrado;
    }

    public int getTipoEntrenamiento() {
        return tipoEntrenamiento;
    }

    public void setTipoEntrenamiento(int tipoEntrenamiento) {
        this.tipoEntrenamiento = tipoEntrenamiento;
    }

    public int getDistanciaObjetivo() {
        return distanciaObjetivo;
    }

    public void setDistanciaObjetivo(int distanciaObjetivo) {
        this.distanciaObjetivo = distanciaObjetivo;
    }
}
