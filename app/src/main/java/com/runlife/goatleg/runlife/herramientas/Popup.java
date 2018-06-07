package com.runlife.goatleg.runlife.herramientas;

import android.app.Activity;
import android.app.Dialog;

/**
 * Created by javi on 26/05/2018.
 */

public class Popup {
    public static final int POPUP_NO_MODAL = 0;
    public static final int POPUP_MODAL = 1;
    public static final int POPUP_SUPERMODAL = 2;

    public static Dialog generarPopUp(Activity activity, int layout, int popupModal) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(layout);
        switch (popupModal) {
            case POPUP_NO_MODAL:
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                break;
            case POPUP_MODAL:
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                break;
            case POPUP_SUPERMODAL:
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                break;
        }
        return dialog;
    }
}
