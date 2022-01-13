package com.example.floppy.ui;

import android.content.BroadcastReceiver;

import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.message.MessagePresenter;

import java.util.ArrayList;

public interface GlobalView {
    void showAlertDialog();

    void showToast(String msg);

    /**
     * VISTA DEUN PROGRESS EL CUAL ESTA EN LA MITAD DE LA VISTA
     */
    void showHandlingGeneral(boolean show);

    /**
     * MOSTRAR EN UN DIALOGO LOS ESTADOS QUE TIENE UN USUARIO
     *
     * @param estados un array list de los estados delos usuarios
     */
    void showStateDialog(ArrayList<Estado> estados);

    /**
     * MOSTRAR UN DIALOGO DE LA IMAGEN DEL USER EN LOS ITEMS DEL CHAT
     *
     * @param friend
     */
    void showUsesDialog(User friend);

    /**
     * QUITAR TOOLBAR AL HACER SCROLL
     * @param show
     */
    void animToolbar(boolean show);

    void showContacts(ArrayList<User> users);

    void nextActivity();

    void beginDownload(BroadcastReceiver broadcastReceiver);

    void recordAudio(String name, String idChat, MessagePresenter messagePresenter);

    void stopAudio();
}
