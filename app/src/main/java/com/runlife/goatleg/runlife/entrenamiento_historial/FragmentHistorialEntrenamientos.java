package com.runlife.goatleg.runlife.entrenamiento_historial;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.runlife.goatleg.runlife.MainActivity;
import com.runlife.goatleg.runlife.R;
import com.runlife.goatleg.runlife.entrenamiento.EntrenamientoDatos;
import com.runlife.goatleg.runlife.herramientas.Comprobadores;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistorialEntrenamientos extends Fragment {
    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";

    private ArrayList<EntrenamientoDatos> arrayListHistorialEntrenamientoDatoses = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ListView listViewlistaHistorial;
    private ArrayAdapterHistorial arrayAdapterHistorial;

    public FragmentHistorialEntrenamientos() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_listas, container, false);

        this.listViewlistaHistorial = rootView.findViewById(R.id.listViewlistaHistorial);

        if (!Comprobadores.internetActivado(rootView.getContext())) {
            Toast.makeText(rootView.getContext(), getResources().getString(R.string.comprobarInternet_string), Toast.LENGTH_SHORT).show();
        }

        arrayAdapterHistorial = new ArrayAdapterHistorial(rootView.getContext(), R.layout.row_historial_entrenamientos, arrayListHistorialEntrenamientoDatoses);
        listViewlistaHistorial.setAdapter(arrayAdapterHistorial);

        listViewlistaHistorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent anIntent = new Intent(rootView.getContext(), InformacionEntrenamientoActivity.class);
                anIntent.putExtra(EntrenamientoDatos.IDENTRENAMIENTO, arrayListHistorialEntrenamientoDatoses.get(i).getIdEntrenamiento());
                startActivity(anIntent);
            }
        });
        db.collection("EntrenamientoDatos")
                .whereEqualTo("Usuario", MainActivity.cuentaGoogleUsuario.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot entrenamientoQuery : task.getResult()) {
                                Log.d(TAGDEVELOP, entrenamientoQuery.getId() + " => " + entrenamientoQuery.getData());
                                ArrayList<GeoPoint> puntoDeRutaArrayList = new ArrayList<GeoPoint>();
                                arrayListHistorialEntrenamientoDatoses.add(new EntrenamientoDatos(new Timestamp(((Date)entrenamientoQuery.get(EntrenamientoDatos.FECHAENTRENAMIENTO)).getTime()), (double) entrenamientoQuery.get(EntrenamientoDatos.DISTANCIARECORRIDA), puntoDeRutaArrayList, (long) entrenamientoQuery.get(EntrenamientoDatos.TIEMPOENTRENAMIENTO),(long)entrenamientoQuery.get(EntrenamientoDatos.VELOCIDADMEDIA),entrenamientoQuery.getId()));
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
