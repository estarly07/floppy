package com.example.floppy.ui.login;

import android.graphics.Bitmap;

public interface LoginPresenter {

    Boolean isLogged();

    void validateData(String[] data,Boolean isLogin);

    void showMessage(String msg);

    void showInputUser();

    void insertInfoUser(String[] data, Bitmap bitmap);

    void insertUserOwner();

    void getMyDataRemote();

    void insertUserLocal();

    void nextActivity();

}
