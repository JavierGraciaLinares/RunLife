package com.runlife.goatleg.runlife.entrenamiento_historial;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.runlife.goatleg.runlife.R;
import com.runlife.goatleg.runlife.entrenamiento.EntrenamientoDatos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by javi on 17/05/2018.
 */

public class ArrayAdapterHistorial extends ArrayAdapter<EntrenamientoDatos> {

    private Context aContext;
    private int aResource;
    private ArrayList<EntrenamientoDatos> listaEntrenamientoDatoses;

    //Elementos de la fila
    private TextView textViewHistorialFecha;
    private TextView textViewHistorialDuracionEntrenamiento;
    private TextView textViewHistorialDistancia;
    private TextView textViewHistorialVelocidadMedia;
    private ImageView imageViewHistorialTipoEntrenamiento;
    private ImageView imageViewHistorialDuracionEntrenamiento;
    private ImageView imageViewHistorialVelocidadMedia;

    public ArrayAdapterHistorial(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<EntrenamientoDatos> listaEntrenamientoDatoses) {
        super(context, resource, listaEntrenamientoDatoses);
        aContext = context;
        aResource = resource;
        this.listaEntrenamientoDatoses = listaEntrenamientoDatoses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View returnView = convertView;

        if (returnView == null) {
            LayoutInflater aInflater = ((Activity) aContext).getLayoutInflater();
            returnView = aInflater.inflate(aResource, parent, false);
        }

        //Iniciamos Elementos de la fila
        textViewHistorialFecha = (TextView) returnView.findViewById(R.id.textViewCarreraFecha);
        textViewHistorialDuracionEntrenamiento = (TextView) returnView.findViewById(R.id.textViewHistorialDuracionEntrenamiento);
        textViewHistorialVelocidadMedia = (TextView) returnView.findViewById(R.id.textViewHistorialVelocidadMedia);
        imageViewHistorialTipoEntrenamiento = (ImageView) returnView.findViewById(R.id.imageViewCarreraDistancia);
        imageViewHistorialDuracionEntrenamiento = (ImageView) returnView.findViewById(R.id.imageViewCarreraFecha);
        imageViewHistorialVelocidadMedia = (ImageView) returnView.findViewById(R.id.imageViewHistorialVelocidadMedia);
        textViewHistorialDistancia = (TextView) returnView.findViewById(R.id.textViewHistorialDistancia);

        //Damos valor a los elementos de la fila
        textViewHistorialFecha.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(listaEntrenamientoDatoses.get(position).getHoraDelEntrenamiento()));
        long tiempoEntrenamiento = listaEntrenamientoDatoses.get(position).getTiempoEntrenamiento();
        int hours = (int) (tiempoEntrenamiento / 3600000);
        int minutes = (int) (tiempoEntrenamiento - hours * 3600000) / 60000;
        int seconds = (int) (tiempoEntrenamiento - hours * 3600000 - minutes * 60000) / 1000;
        textViewHistorialDuracionEntrenamiento.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
        textViewHistorialVelocidadMedia.setText((double)Math.round(listaEntrenamientoDatoses.get(position).getVelocidadMedia()*100d)/100d + "min/km");
        textViewHistorialDistancia.setText(listaEntrenamientoDatoses.get(position).getDistanciarecorridaEnKMString());
        return returnView;
    }
}
