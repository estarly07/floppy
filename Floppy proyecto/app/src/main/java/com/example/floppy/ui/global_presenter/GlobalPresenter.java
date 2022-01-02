package com.example.floppy.ui.global_presenter;

import android.app.Dialog;

import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Models.Estado_User;
import com.example.floppy.data.Models.User;

import java.util.ArrayList;

public interface GlobalPresenter {
    void showMessage(String msg);

    void showEstadoDialogo(ArrayList<Estado> estados);

    void showHandlingGeneral(boolean show);

    void showUserImageDialog(User friend);

    void showTollbar(boolean show);

    void showContacts(ArrayList<User> users);

    void updateState(Estado_User estado_user);

    void nextActivity();

    void addAllStickers(String stickersReceiver, Dialog dialog);
}
