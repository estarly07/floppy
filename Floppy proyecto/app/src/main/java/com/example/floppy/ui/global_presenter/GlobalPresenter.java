package com.example.floppy.ui.global_presenter;

import android.app.Dialog;
import android.content.BroadcastReceiver;

import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.message.MessagePresenter;

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

    void insertStickers(String stickersReceiver);

    void beginDownloadApp(BroadcastReceiver onDownloadComplete);

    void recordAudio(String idChat,User friend, MessagePresenter messagePresenter);

    /**
     * @param  data data[0] => path
     *             data[1] => name del archivo
     * @param friend  */
    void stopRecord(String[] data, String idChat,User friend, MessagePresenter messagePresenter);

    void sendMessage(String name,String idChat,User friend, MessagePresenter messagePresenter);

}
