package com.example.floppy.ui.login;

import android.graphics.Bitmap;

public interface LoginPresenter {

    Boolean isLogged();

    void validateData(String[] data,Boolean isLogin);

    void showMessage(String msg);

    void showInputUser();

    void insertInfoUser(String[] data, Bitmap bitmap);

    void insertUserOwner(String data);

    void getMyDataRemote();

    void insertUserLocal(String idUser);

    void nextActivity();

}
