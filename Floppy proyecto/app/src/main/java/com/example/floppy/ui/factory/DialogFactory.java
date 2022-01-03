package com.example.floppy.ui.factory;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.floppy.R;

public class DialogFactory {
    public enum TypeDialog {
        ADD_STICKER,
        SHOW_PHOTO_USER,
        SHOW_STATE_USER,
        ADD_ALL_STICKER,
        STICKER,//mostrar un sialog para añadir o eliminar un sticker
    }

    private static DialogFactory dialogFactory;

    public static DialogFactory getInstance() {
        if (dialogFactory != null) { return dialogFactory; }
        dialogFactory = new DialogFactory();
        return dialogFactory;
    }


    public Dialog getDialog(Context context, TypeDialog typeDialog) {
        View view = null;
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);

        switch (typeDialog) {
            case ADD_STICKER:
                {
                view = LayoutInflater.from(context).inflate(R.layout.dialog_add_sticker, null, false);
                }
                break;
            case SHOW_PHOTO_USER:
                view = LayoutInflater.from(context).inflate(R.layout.dialogo_user, null, false);
                break;
            case ADD_ALL_STICKER:
                view = LayoutInflater.from(context).inflate(R.layout.alertdialog_add_stickers, null, false);
                break;
            case STICKER:
                view = LayoutInflater.from(context).inflate(R.layout.alertdialog_delete_or_add_sticker, null, false);
                break;

        }
        dialog.setContentView(view);
        return dialog;
    }

}
