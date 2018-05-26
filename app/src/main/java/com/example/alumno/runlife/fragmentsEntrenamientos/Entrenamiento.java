package com.example.alumno.runlife.fragmentsEntrenamientos;

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

public class Entrenamiento {
    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";

    public static final String FECHAENTRENAMIENTO = "FechaEntrenamiento";
    public static final String DISTANCIARECORRIDA = "DistanciaRecorrida";
    public static final String TIEMPOENTRENAMIENTO = "TiempoEntrenamiento";
    public static final String VELOCIDADMEDIA = "VelocidadMedia";
    public static final String IDENTRENAMIENTO = "IDEntrenamiento";

    String idEntrenamiento;

    Timestamp horaDelEntrenamiento;
    int numeroLocalizacion;
    boolean enMarcha;
    boolean calibrado;
    long tiempoPausa;
    long velocidadMedia;
    double distanciaRecorrida;
    long tiempoEntrenamiento;
    ArrayList<GeoPoint> recorrido;


    Location localizacionAnterior;
    Long tiempoAnterior;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Entrenamiento() {
        this.numeroLocalizacion = 0;
        this.enMarcha = false;
        this.tiempoPausa = 0;
        this.distanciaRecorrida = 0;
        this.recorrido = new ArrayList<>();
        this.horaDelEntrenamiento = new Timestamp(System.currentTimeMillis());
        this.velocidadMedia = 0;
    }

    public Entrenamiento(Timestamp horaDelEntrenamiento, double distanciaRecorrida, ArrayList<GeoPoint> recorrido, long tiempoEntrenamiento, long velocidadMedia, String idEntrenamiento) {
        this.horaDelEntrenamiento = horaDelEntrenamiento;
        this.distanciaRecorrida = distanciaRecorrida;
        this.recorrido = recorrido;
        this.tiempoEntrenamiento = tiempoEntrenamiento;
        this.velocidadMedia = velocidadMedia;
        this.idEntrenamiento = idEntrenamiento;
    }

    public double calcularMinutosXKilometros(long elapsedRealtime, long crono, double distanciaEntreDosPuntos) {  //¡¡¡¡¡¡¡¡¡¡¡REVISAAAAAAR!!!
        tiempoEntrenamiento = elapsedRealtime - crono;
        /*int hours = (int) (timeAux / 3600000);
          int minutes = (int) (timeAux - hours * 3600000) / 60000;
          int seconds = (int) (timeAux - hours * 3600000 - minutes * 60000) / 1000;
          Log.i(TAGDEVELOP, "Crono: " + hours + ":" + minutes + ":" + seconds);*/
        return ((distanciaEntreDosPuntos / (tiempoEntrenamiento / 1000)) * 16.666666666667);
    }

    public void anyadirPuntoDeRutaRecorrido(Location localizacion) {
        //recorrido.add(new PuntoDeRuta(localizacion));
        recorrido.add(new GeoPoint(localizacion.getLatitude(),localizacion.getLongitude()));
    }

    public String getDistanciarecorridaEnKMString() {
        return String.format("%.2f", (this.getDistanciaRecorrida() / 1000)) + " km";
    }

    public void insertarEntrenamientoEnFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> entidadEntrenamiento = new HashMap<>();
        entidadEntrenamiento.put(FECHAENTRENAMIENTO, horaDelEntrenamiento);
        entidadEntrenamiento.put("Usuario", MainActivity.cuentaGoogleUsuario.getEmail());
        entidadEntrenamiento.put(DISTANCIARECORRIDA, distanciaRecorrida);
        entidadEntrenamiento.put(TIEMPOENTRENAMIENTO, tiempoEntrenamiento);
        entidadEntrenamiento.put(VELOCIDADMEDIA, velocidadMedia);
        Map<String, Object> entidadListaRuta = new HashMap<>();
        int i = 0;
        /*for (PuntoDeRuta puntoDeRuta : recorrido) {
            Map<String, Object> entidadPuntoRuta = new HashMap<>();
            entidadPuntoRuta.put("Geopunto", puntoDeRuta.getGeopunto());
            entidadPuntoRuta.put("Tiempo", puntoDeRuta.getTiempo());
            entidadListaRuta.put(Integer.toString(i), entidadPuntoRuta);
            i++;
        }*/
        entidadEntrenamiento.put("Recorrido",recorrido);
        db.collection("Entrenamiento")
                .add(entidadEntrenamiento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAGDEBUG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAGDEBUG, "Error adding document", e);
                    }
                });

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

    public long getTiempoPausa() {
        return tiempoPausa;
    }

    public void setTiempoPausa(long tiempoPausa) {
        this.tiempoPausa = tiempoPausa;
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
}
