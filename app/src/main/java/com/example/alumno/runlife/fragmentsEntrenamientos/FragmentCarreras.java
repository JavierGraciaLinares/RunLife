package com.example.alumno.runlife.fragmentsEntrenamientos;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.alumno.runlife.Carrera;
import com.example.alumno.runlife.InformacionEntrenamientoActivity;
import com.example.alumno.runlife.R;
import com.example.alumno.runlife.adapters.ArrayAdapterCarreras;
import com.example.alumno.runlife.jsonLector.IJsonCarrera;
import com.example.alumno.runlife.jsonLector.JsonAsyncTask;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URI;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCarreras extends Fragment implements IJsonCarrera {
    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";

    ArrayList<Carrera> arrayListCarreras = new ArrayList<>();
    private FragmentCarreras fragmentCarreras = this;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference entrenamientosRef = db.collection("entrenamientoDatos");

    ListView listViewlistaHistorial;
    ArrayAdapterCarreras arrayAdapterCarreras;

    public FragmentCarreras() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_listas, container, false);

        this.listViewlistaHistorial = rootView.findViewById(R.id.listViewlistaHistorial);


        arrayAdapterCarreras = new ArrayAdapterCarreras(rootView.getContext(), R.layout.row_carrera, arrayListCarreras);
        listViewlistaHistorial.setAdapter(arrayAdapterCarreras);

        listViewlistaHistorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //(Intent.ACTION_VIEW, arrayListCarreras.get(i).getLink());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, arrayListCarreras.get(i).getLink());
                startActivity(webIntent);
            }
        });

        JsonAsyncTask jsonAsyncTask = new JsonAsyncTask(fragmentCarreras);
        jsonAsyncTask.execute();
        arrayAdapterCarreras.notifyDataSetChanged();
        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void addCarreraJson(Carrera carrera) {
        arrayListCarreras.add(carrera);
        arrayAdapterCarreras.notifyDataSetChanged();
    }
}
