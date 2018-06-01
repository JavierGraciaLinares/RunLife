package com.example.alumno.runlife.adapters;

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

import com.example.alumno.runlife.Carrera;
import com.example.alumno.runlife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by javi on 30/05/2018.
 */

public class ArrayAdapterCarreras extends ArrayAdapter<Carrera> {

    public Context aContext;
    public int aResource;
    public ArrayList<Carrera> listaCarreras;

    //Elementos de la fila
    public TextView aTextViewCarreraDistancia;
    public TextView aTextViewCarreraFecha;
    public TextView aTextViewCarreraLugar;
    public TextView aTextViewCarreraNombre;

    public ArrayAdapterCarreras(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Carrera> listaCarreras) {
        super(context, resource, listaCarreras);
        aContext = context;
        aResource = resource;
        this.listaCarreras = listaCarreras;
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
        aTextViewCarreraDistancia = (TextView) returnView.findViewById(R.id.textViewCarreraDistancia);
        aTextViewCarreraFecha = (TextView) returnView.findViewById(R.id.textViewCarreraFecha);
        aTextViewCarreraLugar = (TextView) returnView.findViewById(R.id.textViewCarreraLugar);
        aTextViewCarreraNombre = (TextView) returnView.findViewById(R.id.textViewCarreraNombre);

        aTextViewCarreraDistancia.setText(listaCarreras.get(position).getDistancia());
        aTextViewCarreraFecha.setText(new SimpleDateFormat("dd-MM-yyy").format(listaCarreras.get(position).getFecha()) + "   " + listaCarreras.get(position).getHora());
        aTextViewCarreraLugar.setText(listaCarreras.get(position).getLugar());
        aTextViewCarreraNombre.setText(listaCarreras.get(position).getNombre());


        return returnView;
    }
}
