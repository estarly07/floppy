package com.example.floppy.ui;

import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Models.User;

import java.util.ArrayList;

public interface GlobalView {
    void showToast(String msg);

    /**
     * VISTA DEUN PROGRESS EL CUAL ESTA EN LA MITADD DE LA VISTA
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
     * @show
     */
    void animToolbar(boolean show);

    void showContacts(ArrayList<User> users);

    void nextActivity();
}