package com.example.alumno.runlife;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by javi on 26/05/2018.
 */

public class Animaciones {
    public static void vueltaCompletaFloatinButton(Context context,FloatingActionButton floatingActionButton){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.vuelta_completa);
        floatingActionButton.startAnimation(animation);
    }
}
