package com.example.floppy.ui.global_presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;

import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.local.InteractorLocal;
import com.example.floppy.domain.local.InteractorSqlite;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.remote.Interactor;
import com.example.floppy.domain.remote.InteractorFirestoreImpl;
import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.GlobalView;
import com.example.floppy.ui.message.MessagePresenter;
import com.example.floppy.utils.Global.GlobalUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class GlobalPresenterImpl implements GlobalPresenter {
    private User            user = User.getInstance();
    private GlobalView      globalView;
    private Context         context;
    private Interactor      interactor;
    private InteractorLocal interactorLocal;

    public GlobalPresenterImpl(GlobalView globalView, Context context, Activity activity) {
        this.globalView      = globalView;
        this.context         = context;
        this.interactorLocal = new InteractorSqlite(context);
        this.interactor      = new InteractorFirestoreImpl(context,this,  activity);/**INTERACTOR QUE LLAMA A FIRESTORE*/
    }

    @Override
    public void showMessage(String msg) {
        globalView.showToast(msg);
    }

    @Override
    public void showEstadoDialogo(ArrayList<Estado> estados) {
        globalView.showStateDialog(estados);
    }

    @Override
    public void showHandlingGeneral(boolean show) {
        globalView.showHandlingGeneral(show);
    }


    @Override
    public void showUserImageDialog(User friend) {
        globalView.showUsesDialog(friend);
    }

    @Override
    public void showTollbar(boolean show) {
        globalView.animToolbar(show);
    }


    @Override
    public void showContacts(ArrayList<User> users) {
        globalView.showContacts(users);
    }

    @Override
    public void updateState(Estado_User estado_user) {
        interactor.updateEstado(User.getInstance().getIdUser(), estado_user);
    }

    @Override
    public void nextActivity() {
        globalView.nextActivity();
    }

    @Override
    public void addAllStickers(String stickersReceiver, Dialog dialog) {
        new Thread(() -> {
            Map map = new Gson().fromJson(stickersReceiver,Map.class);
            ArrayList<String> stickers = (ArrayList<String>) map.get("stickers");
            for (String url:
                 stickers) {
                StickersEntity stickersEntity = new StickersEntity();
                stickersEntity.urlImage = url;
                stickersEntity.fk_idUser = User.getInstance().getIdUser();
                interactorLocal.insertSticker(stickersEntity);
            }
            dialog.dismiss();

        }).start();
    }

    @Override
    public void insertStickers(String stickersReceiver) {
        if(stickersReceiver.contains("stickers:")) { globalView.showAlertDialog(); }
    }

    @Override
    public void beginDownloadApp(BroadcastReceiver onDownloadComplete) {
        globalView.beginDownload(onDownloadComplete);
    }

    @Override
    public void recordAudio(String idChat, MessagePresenter messagePresenter) {
        globalView.recordAudio(GlobalUtils.getDateNow()+idChat, idChat, messagePresenter );
    }

    @Override
    public void stopRecord(String[] data, String idChat, MessagePresenter messagePresenter) {
        globalView.stopAudio();
        globalView.showHandlingGeneral(true);
        new Thread(() -> interactor.savedAudio(data[1], Uri.fromFile(new File(data[0]+"/"+data[1]+".mp3")),idChat,messagePresenter)).start();
    }

    @Override
    public void sendMessage(String name, String idChat, MessagePresenter messagePresenter) {
        Message message = new Message();
        message.setMessage(name);
        message.setTypeMessage(Message.TypesMessages.RECORD);
        messagePresenter.sendMessages(idChat,message);
        globalView.showHandlingGeneral(false);
    }


}