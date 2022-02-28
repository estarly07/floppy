package com.example.floppy.ui.global_presenter;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.net.Uri;

import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.message.MessagePresenter;

import java.util.ArrayList;

public interface GlobalPresenter {
    void showMessage(String msg);

    void showEstadoDialogo(ArrayList<Estado> estados);

    void showHandlingGeneral(boolean show,String title);

    void showUserImageDialog(User friend);

    void showTollbar(boolean show);

    void showContacts(ArrayList<User> users);

    void updateState(Estado_User estado_user);

    void nextActivity();

    void addAllStickers(String stickersReceiver, Dialog dialog);

    void insertStickers(String stickersReceiver);

    void beginDownloadApp(BroadcastReceiver onDownloadComplete);

    void recordAudio(String idChat,User friend, MessagePresenter messagePresenter);

    void sendImage(String idChat,Uri uri, User friend, MessagePresenter messagePresenter);

    void showImage(boolean send, String uri);

    /**
     * @param  data data[0] => path
     *             data[1] => name del archivo
     * @param friend  */
    void stopRecord(String[] data, String idChat,User friend, MessagePresenter messagePresenter);

    void sendMessage(String name,String idChat,User friend,Message.TypesMessages typesMessage, MessagePresenter messagePresenter);

    void getImage(String idChat, User friend, MessagePresenter messagePresenter);
}
