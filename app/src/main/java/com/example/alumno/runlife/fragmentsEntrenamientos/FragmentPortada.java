package com.example.alumno.runlife.fragmentsEntrenamientos;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alumno.runlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortada extends Fragment {


    public FragmentPortada() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portada, container, false);
    }

}
