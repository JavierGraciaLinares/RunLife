package com.example.alumno.runlife.herramientas;

import android.app.Activity;
import android.app.Dialog;

/**
 * Created by javi on 26/05/2018.
 */

public class Popup {
    public static final boolean POPUP_NO_MODAL = false;
    public static final boolean POPUP_MODAL = true;

    public static Dialog generarPopUp(Activity activity, int layout, boolean popupModal) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(layout);
        if (popupModal) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
        }
        return dialog;
    }
}
