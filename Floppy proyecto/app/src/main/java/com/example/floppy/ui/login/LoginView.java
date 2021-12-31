package com.example.floppy.ui.login;


public interface LoginView {

    void showToast(String msg);

    /**
     * MOSTRAR  O NO,UN PROGRESS
     *
     * @param show booleano para saber si mostrar o no
     */
    void handlingLogin(boolean show);

    /**
     * VISTA PARA INGRESAR LOS DATOS DEL USER
     */
    void showInputUser();


    /**
     * VISTA DEUN PROGRESS EL CUAL ESTA EN LA MITADD DE LA VISTA
     */
    void showHandlingGeneral(boolean show);

    void showHandlingInsertData(boolean show);

    void nextActivity();
}
