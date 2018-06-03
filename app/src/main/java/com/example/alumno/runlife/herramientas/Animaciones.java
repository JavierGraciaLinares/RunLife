package com.example.alumno.runlife.herramientas;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.alumno.runlife.R;

/**
 * Created by javi on 26/05/2018.
 */

public class Animaciones {
    public static void vueltaCompletaFloatinButton(Context context,FloatingActionButton floatingActionButton){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.vuelta_completa);
        floatingActionButton.startAnimation(animation);
    }

    public static void descenderElemento(Context context, LinearLayout linearLayout){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bajar_elemento);
        linearLayout.startAnimation(animation);
    }

    public static void ascenderElemento(Context context, LinearLayout linearLayout){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.subir_elemento);
        linearLayout.startAnimation(animation);
    }
}
