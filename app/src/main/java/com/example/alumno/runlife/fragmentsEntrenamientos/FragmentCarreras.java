package com.example.alumno.runlife.fragmentsEntrenamientos;


import android.app.Fragment;
import android.content.Intent;
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
    CollectionReference entrenamientosRef = db.collection("entrenamiento");

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent anIntent = new Intent(rootView.getContext(), InformacionEntrenamientoActivity.class);
                //anIntent.putExtra(Entrenamiento.IDENTRENAMIENTO,arrayListCarreras.get(i).getIdEntrenamiento());
                startActivity(anIntent);
            }
        });
        /*db.collection("Entrenamiento")
                .whereEqualTo("Usuario", MainActivity.cuentaGoogleUsuario.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot entrenamientoQuery : task.getResult()) {
                                Log.d(TAGDEVELOP, entrenamientoQuery.getId() + " => " + entrenamientoQuery.getData());
                                ArrayList<GeoPoint> puntoDeRutaArrayList = new ArrayList<GeoPoint>();
                                //puntoDeRutaArrayList.addAll(((HashMap<String, GeoPoint>) entrenamientoQuery.get("Recorrido")).values());
                                //puntoDeRutaArrayList = (ArrayList<PuntoDeRuta>) entrenamientoQuery.get("Recorrido");
                                //arrayListCarreras.add(new Entrenamiento(new Timestamp(((Date)entrenamientoQuery.get(Entrenamiento.FECHAENTRENAMIENTO)).getTime()), (double) entrenamientoQuery.get(Entrenamiento.DISTANCIARECORRIDA), puntoDeRutaArrayList, (long) entrenamientoQuery.get(Entrenamiento.TIEMPOENTRENAMIENTO),(long)entrenamientoQuery.get(Entrenamiento.VELOCIDADMEDIA),entrenamientoQuery.getId()));
                                arrayAdapterCarreras.add(new Carrera(1,"Titulo","Descripcion"));
                                arrayAdapterCarreras.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAGDEVELOP, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
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
