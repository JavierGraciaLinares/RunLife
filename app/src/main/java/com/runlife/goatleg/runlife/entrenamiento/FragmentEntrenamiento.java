package com.runlife.goatleg.runlife.entrenamiento;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.runlife.goatleg.runlife.entrenamiento_historial.FragmentHistorialEntrenamientos;
import com.runlife.goatleg.runlife.herramientas.Animaciones;
import com.runlife.goatleg.runlife.MainActivity;
import com.runlife.goatleg.runlife.R;
import com.runlife.goatleg.runlife.herramientas.Popup;
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
public class FragmentEntrenamiento extends Fragment implements TextToSpeech.OnInitListener {

    public static final int RESPONSE = 1;
    private boolean textToSpeechEnabled = false;
    private TextToSpeech textToSpeech;

    private EntrenamientoDatos entrenamientoDatos;

    private Chronometer cronometro;
    private TextView textViewDistanciaRecorrida;
    private TextView textViewVelocidadMedia;
    public TextView textViewObjetivoEntrenamiento;

    private TextView textViewPopupPreparandoEspereHead;
    private TextView textViewPopupPreparandoEspereBody;
    private FloatingActionButton buttonEmpezarEntrenamiento;
    private Dialog popup;

    //Campos PopUpConfiguracionDistancia
    private Dialog popupConfiguracion;
    private TextView textViewDistanciaEntrenamiento;
    private Button buttonOkDistancia;

    private FusedLocationProviderClient aClient;
    private LocationRequest aRequest;
    private LocationCallback aCallback;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public FragmentEntrenamiento() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entrenamientoDatos = new EntrenamientoDatos();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_entrenamiento, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        iniciarElementosDeLaVentana();
        configuracionSolicitudGPS();
        establecreAccionesBotones();
        establecerTipoEntrenamiento();
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

                    if (entrenamientoDatos.numeroLocalizacion == 0) {
                        entrenamientoDatos.setLocalizacionAnterior(localizacionActual);
                    }
                    distanciaEntreDosPuntos = entrenamientoDatos.getLocalizacionAnterior().distanceTo(localizacionActual);
                    Log.i(MainActivity.TAGDEVELOP, "Distancia entre 2 puntos: " + distanciaEntreDosPuntos + " metros");

                    if (entrenamientoDatos.numeroLocalizacion < 5 || (distanciaEntreDosPuntos > 15 && !entrenamientoDatos.isEnMarcha())) {
                        entrenamientoDatos.numeroLocalizacion++;
                        entrenamientoDatos.setLocalizacionAnterior(localizacionActual);
                    } else if (!entrenamientoDatos.isEnMarcha()) {
                        calibracionGPSFinalizada();
                    } else if (entrenamientoDatos.getTipoEntrenamiento() == EntrenamientoDatos.ENTRENAMIENTO_TIPO_DISTANCIA && entrenamientoDatos.getDistanciaRecorrida() >= entrenamientoDatos.getDistanciaObjetivo()) {
                        decirConVoz(getResources().getString(R.string.entrenamientoFinalizado_string));
                        finalizarEntrenamiento();
                    } else {
                        Log.i(MainActivity.TAGDEVELOP, "Distancia entre 2 puntos: " + distanciaEntreDosPuntos + " metros");
                        if (distanciaEntreDosPuntos > 20 && distanciaEntreDosPuntos < 80) {
                            // Distancia Recorrida
                            if (entrenamientoDatos.getnumeroLocalizacion() == 5) {
                                entrenamientoDatos.setDistanciaRecorrida(entrenamientoDatos.distanciaRecorrida += distanciaEntreDosPuntos);
                                textViewDistanciaRecorrida.setText(entrenamientoDatos.getDistanciarecorridaEnKMString());
                            }
                            //Fuera del if pruebas, dentro correcto
                            //Insertar PUNTO DE RUTA
                            entrenamientoDatos.anyadirPuntoDeRutaRecorrido(localizacionActual);
                            //Calcular y Mostrar VELOCIDAD MEDIA
                            textViewVelocidadMedia.setText(String.format("%.2f", entrenamientoDatos.calcularKmXHMedia(SystemClock.elapsedRealtime(), cronometro.getBase())) + "Km/h"); //metros/segundo* tranformación para min/km

                            entrenamientoDatos.setLocalizacionAnterior(localizacionActual);
                            entrenamientoDatos.numeroLocalizacion++;
                        }
                    }
                    entrenamientoDatos.setTiempoAnterior(SystemClock.elapsedRealtime());
                }
            };
            aClient.requestLocationUpdates(aRequest, aCallback, null);
        }
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
        }
    }


    private void iniciarElementosDeLaVentana() {
        textViewDistanciaRecorrida = (TextView) getView().findViewById(R.id.textViewDistanciaRecorrida);
        textViewVelocidadMedia = (TextView) getView().findViewById(R.id.textViewVelocidadMedia);
        textViewObjetivoEntrenamiento = (TextView) getView().findViewById(R.id.textViewObjetivoEntrenamiento);
        textViewObjetivoEntrenamiento = (TextView) getView().findViewById(R.id.textViewObjetivoEntrenamiento);
        cronometro = (Chronometer) getView().findViewById(R.id.chronometerEntrenamientoLibre);
        buttonEmpezarEntrenamiento = (FloatingActionButton) getView().findViewById(R.id.buttonEmpezarEntrenamiento);
        textToSpeech = new TextToSpeech(getContext(), this);
    }

    private void configuracionSolicitudGPS() {
        aClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        aRequest = new LocationRequest();
        aRequest.setInterval(5000);
        aRequest.setFastestInterval(2500);
        aRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void establecreAccionesBotones() {
        buttonEmpezarEntrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (entrenamientoDatos.isEnMarcha()) {
                    finalizarEntrenamiento();
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

    private void establecerTipoEntrenamiento() {
        if (getArguments() != null) {
            entrenamientoDatos.setTipoEntrenamiento(getArguments().getInt(EntrenamientoDatos.ENTRENAMIENTO_TIPO));
            switch (entrenamientoDatos.getTipoEntrenamiento()) {
                case EntrenamientoDatos.ENTRENAMIENTO_TIPO_LIBRE:
                    Toast.makeText(getContext(), getResources().getText(R.string.entrenamiento_String) + ": " + getResources().getText(R.string.libreString), Toast.LENGTH_SHORT).show();
                    textViewObjetivoEntrenamiento.setText(getResources().getText(R.string.libreString).toString());
                    break;
                case EntrenamientoDatos.ENTRENAMIENTO_TIPO_DISTANCIA:
                    Toast.makeText(getContext(), getResources().getText(R.string.entrenamiento_String) + ": " + getResources().getText(R.string.distanciaString), Toast.LENGTH_SHORT).show();
                    popupConfiguracion = Popup.generarPopUp(getActivity(), R.layout.popup_distancia_entrenamiento, Popup.POPUP_SUPERMODAL);
                    buttonOkDistancia = (Button) popupConfiguracion.findViewById(R.id.buttonOkDistancia);
                    textViewDistanciaEntrenamiento = (TextView) popupConfiguracion.findViewById(R.id.textViewDistanciaEntrenamiento);
                    buttonOkDistancia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!textViewDistanciaEntrenamiento.getText().toString().isEmpty() && Integer.parseInt(textViewDistanciaEntrenamiento.getText().toString()) > 0) {
                                entrenamientoDatos.setDistanciaObjetivo(Integer.parseInt(textViewDistanciaEntrenamiento.getText().toString()));
                                popupConfiguracion.cancel();
                                textViewObjetivoEntrenamiento.setText(entrenamientoDatos.getDistanciaObjetivo() + " m");
                            }
                        }
                    });
                    popupConfiguracion.show();

                    break;
                default:
                    break;
            }
        }
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
        entrenamientoDatos.setEnMarcha(true);
        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start();
        sonidoComienzo();
        decirConVoz(getResources().getString(R.string.comienzoEntrenamiento_voice));
        establecerBotonStop();
        entrenamientoDatos.setnumeroLocalizacion(5);
    }

    private void finalizarEntrenamiento() {
        entrenamientoDatos.insertarEntrenamientoEnFirebase();
        FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, new FragmentHistorialEntrenamientos());
        fragmentTransaction.commit();
    }


    // Sonidos

    private void sonidoComienzo() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.sonido_comienzo);
        mediaPlayer.start();
    }

    private void decirConVoz(String texto) {
        if (textToSpeechEnabled) {
            textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(MainActivity.TAGDEVELOP, "Lenguaje no soportado");
                textToSpeechEnabled = false;
            } else {
                textToSpeechEnabled = true;
            }

        } else {
            Log.e(MainActivity.TAGDEVELOP, "Error Text To Speach");
        }
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
