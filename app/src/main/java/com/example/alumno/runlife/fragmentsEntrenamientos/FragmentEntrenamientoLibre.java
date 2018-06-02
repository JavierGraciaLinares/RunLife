package com.example.alumno.runlife.fragmentsEntrenamientos;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.alumno.runlife.Animaciones;
import com.example.alumno.runlife.R;
import com.example.alumno.runlife.herramientas.Popup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class FragmentEntrenamientoLibre extends Fragment implements TextToSpeech.OnInitListener {

    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";
    public static final int RESPONSE = 1;
    private boolean textToSpeechEnabled = false;
    private TextToSpeech textToSpeech;


    Entrenamiento entrenamiento = new Entrenamiento();

    Chronometer cronometro;
    TextView textViewDistanciaRecorrida;
    TextView textViewVelocidadMedia;
    TextView textViewVelocidadActual;
    TextView textViewPopupPreparandoEspereHead;
    TextView textViewPopupPreparandoEspereBody;
    FloatingActionButton buttonEmpezarEntrenamiento;
    private Dialog popup;

    private FusedLocationProviderClient aClient;
    private LocationRequest aRequest;
    private LocationCallback aCallback;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public FragmentEntrenamientoLibre() {
        //textViewDistanciaRecorrida = (TextView) getView().findViewById(R.id.textViewDistanciaRecorrida);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_entrenamiento_libre, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        iniciarElementosDeLaVentana();
        configuracionSolicitudGPS();
        establecreAccionesBotones();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void entrenamientoOperaciones() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, RESPONSE);
        } else {
            aCallback = new LocationCallback() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location localizacionActual = locationResult.getLastLocation();
                    double distanciaEntreDosPuntos;

                    if (entrenamiento.numeroLocalizacion == 0) {
                        entrenamiento.setLocalizacionAnterior(localizacionActual);
                    }
                    distanciaEntreDosPuntos = entrenamiento.getLocalizacionAnterior().distanceTo(localizacionActual);
                    entrenamiento.setLocalizacionAnterior(localizacionActual);
                    Log.i(TAGDEBUG, "Distancia entre 2 puntos: " + distanciaEntreDosPuntos + " metros");

                    if (entrenamiento.numeroLocalizacion < 5 || (distanciaEntreDosPuntos > 10 && !entrenamiento.isEnMarcha())) {
                        entrenamiento.numeroLocalizacion++;
                    } else if (!entrenamiento.isEnMarcha()) {
                        calibracionGPSFinalizada();
                    } else {
                        Log.i(TAGDEBUG, "Distancia entre 2 puntos: " + distanciaEntreDosPuntos + " metros");
                        if (distanciaEntreDosPuntos > 10 && distanciaEntreDosPuntos < 50) {
                            // Distancia Recorrida
                            entrenamiento.setDistanciaRecorrida(entrenamiento.distanciaRecorrida += distanciaEntreDosPuntos);
                            textViewDistanciaRecorrida.setText(entrenamiento.getDistanciarecorridaEnKMString());


                        }
                        //                ¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡METER DENTRO DEL IF!!!!!!!!!!!!!!!!!
                        //Insertar PUNTO DE RUTA
                        entrenamiento.anyadirPuntoDeRutaRecorrido(localizacionActual);
                        //Calcular y Mostrar VELOCIDAD MEDIA
                        textViewVelocidadMedia.setText(String.format("%.2f", entrenamiento.calcularKmXHMedia(SystemClock.elapsedRealtime(), cronometro.getBase(), distanciaEntreDosPuntos)) + "Km/h"); //metros/segundo* tranformación para min/km
                        textViewVelocidadActual.setText(String.format("%.2f", entrenamiento.calcularKmXHActuales(cronometro.getBase(), distanciaEntreDosPuntos)) + "Km/h");

                        Log.i(TAGDEVELOP, "Tiempo Anterior: " + entrenamiento.getTiempoAnterior() + "   Tiempo Actual: " + SystemClock.elapsedRealtime());
                    }

                    entrenamiento.setTiempoAnterior(SystemClock.elapsedRealtime());
                    //Log.i(TAGDEBUG, "Posicion " + localizacionActual.getLatitude() + " " + localizacionActual.getLongitude() + "   Distancia: " + entrenamiento.getDistanciaRecorrida() + " metros");
                    Log.i(TAGDEBUG, "                                              - Distancia: " + entrenamiento.getDistanciaRecorrida() + " metros");
                }
            };
            aClient.requestLocationUpdates(aRequest, aCallback, null);
        }
    }

    private void pausarEntrenamiento() {
        entrenamiento.setTiempoPausa(cronometro.getBase() - SystemClock.elapsedRealtime());
        cronometro.stop();
        entrenamiento.setEnMarcha(false);
        aClient.removeLocationUpdates(aCallback);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == RESPONSE) && (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED) {
            aClient.requestLocationUpdates(aRequest, aCallback, null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this.getContext(), "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, RESPONSE);
        } else {

        }
    }


    private void iniciarElementosDeLaVentana() {
        textViewDistanciaRecorrida = (TextView) getView().findViewById(R.id.textViewDistanciaRecorrida);
        textViewVelocidadMedia = (TextView) getView().findViewById(R.id.textViewVelocidadMedia);
        textViewVelocidadActual = (TextView) getView().findViewById(R.id.textViewVelocidadActual);
        cronometro = (Chronometer) getView().findViewById(R.id.chronometerEntrenamientoLibre);
        buttonEmpezarEntrenamiento = (FloatingActionButton) getView().findViewById(R.id.buttonEmpezarEntrenamiento);
        textToSpeech = new TextToSpeech(getContext(), this);
    }

    private void configuracionSolicitudGPS() {
        aClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        aRequest = new LocationRequest();
        aRequest.setInterval(10000);
        aRequest.setFastestInterval(2000);
        aRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void establecreAccionesBotones() {
        buttonEmpezarEntrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (entrenamiento.isEnMarcha()) {
                    establecerBotonStart();
                    entrenamiento.insertarEntrenamientoEnFirebase();
                    pausarEntrenamiento();
                } else {
                    prepararPopUpEntrenamiento();
                }
            }
        });
    }

    private void establecerBotonStop() {
        Animaciones.vueltaCompletaFloatinButton(getContext(), buttonEmpezarEntrenamiento);
        buttonEmpezarEntrenamiento.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimary)));
        buttonEmpezarEntrenamiento.setImageResource(R.drawable.stop);
    }

    private void establecerBotonStart() {
        Animaciones.vueltaCompletaFloatinButton(getContext(), buttonEmpezarEntrenamiento);
        buttonEmpezarEntrenamiento.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorStart)));
        buttonEmpezarEntrenamiento.setImageResource(R.drawable.play);


    }

    private void prepararPopUpEntrenamiento() {
        Animaciones.vueltaCompletaFloatinButton(getContext(), buttonEmpezarEntrenamiento);
        popup = Popup.generarPopUp(getActivity(), R.layout.popup_preparando_entrenamiento, Popup.POPUP_MODAL);
        textViewPopupPreparandoEspereHead = (TextView) popup.findViewById(R.id.textViewPopupPreparandoEspereHead);
        textViewPopupPreparandoEspereBody = (TextView) popup.findViewById(R.id.textViewPopupPreparandoEspereBody);
        popup.show();
        entrenamientoOperaciones();

    }

    private void calibracionGPSFinalizada() {
        LottieAnimationView animationPrepararEntrenamiento = popup.findViewById(R.id.animationPrepararEntrenamiento);
        animationPrepararEntrenamiento.setVisibility(LottieAnimationView.INVISIBLE);
        popup.findViewById(R.id.animationPrepararEntrenamientoCompletado).setVisibility(LottieAnimationView.VISIBLE);
        LottieAnimationView animationPrepararEntrenamientoCompletado = popup.findViewById(R.id.animationPrepararEntrenamientoCompletado);
        animationPrepararEntrenamientoCompletado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empezarEntrenamiento();
                popup.cancel();
            }
        });

        textViewPopupPreparandoEspereHead.setText(R.string.okgps_string);
        textViewPopupPreparandoEspereBody.setText(R.string.okgpsbody_string);
    }

    private void empezarEntrenamiento() {
        entrenamiento.setEnMarcha(true);
        cronometro.setBase(entrenamiento.getTiempoPausa() + SystemClock.elapsedRealtime());
        cronometro.start();
        entrenamiento.setTiempoPausa(0);
        sonidoComienzo();
        decirConVoz(getResources().getString(R.string.comienzoEntrenamiento_voice));
    }

    private void sonidoComienzo() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.sonido_comienzo);
        mediaPlayer.start();
    }


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAGDEBUG, "Lenguaje no soportado");
                textToSpeechEnabled = false;
            } else {
                textToSpeechEnabled = true;
            }

        } else {
            Log.e(TAGDEBUG, "Error Text To Speach");
        }
    }

    private void decirConVoz(String texto) {
        textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
        super.onDestroy();
    }
}
