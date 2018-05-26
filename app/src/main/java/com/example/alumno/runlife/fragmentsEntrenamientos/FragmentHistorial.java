package com.example.alumno.runlife.fragmentsEntrenamientos;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alumno.runlife.InformacionEntrenamientoActivity;
import com.example.alumno.runlife.MainActivity;
import com.example.alumno.runlife.R;
import com.example.alumno.runlife.adapters.ArrayAdapterHistorial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistorial extends Fragment {
    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";

    ArrayList<Entrenamiento> arrayListHistorialEntrenamientos = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference entrenamientosRef = db.collection("entrenamiento");

    ListView listViewlistaHistorial;
    ArrayAdapterHistorial arrayAdapterHistorial;

    public FragmentHistorial() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_fragment_historial, container, false);

        this.listViewlistaHistorial = rootView.findViewById(R.id.listViewlistaHistorial);


        arrayAdapterHistorial = new ArrayAdapterHistorial(rootView.getContext(), R.layout.row_historial_entrenamientos, arrayListHistorialEntrenamientos);
        listViewlistaHistorial.setAdapter(arrayAdapterHistorial);

        listViewlistaHistorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent anIntent = new Intent(rootView.getContext(), InformacionEntrenamientoActivity.class);
                anIntent.putExtra(Entrenamiento.IDENTRENAMIENTO,arrayListHistorialEntrenamientos.get(i).getIdEntrenamiento());
                startActivity(anIntent);
            }
        });
        db.collection("Entrenamiento")
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
                                arrayListHistorialEntrenamientos.add(new Entrenamiento(new Timestamp(((Date)entrenamientoQuery.get(Entrenamiento.FECHAENTRENAMIENTO)).getTime()), (double) entrenamientoQuery.get(Entrenamiento.DISTANCIARECORRIDA), puntoDeRutaArrayList, (long) entrenamientoQuery.get(Entrenamiento.TIEMPOENTRENAMIENTO),(long)entrenamientoQuery.get(Entrenamiento.VELOCIDADMEDIA),entrenamientoQuery.getId()));
                                arrayAdapterHistorial.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAGDEVELOP, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
