package com.example.floppy.ui.global_presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Interactor.local.InteractorLocal;
import com.example.floppy.data.Interactor.local.InteractorSqlite;
import com.example.floppy.data.Interactor.remote.Interactor;
import com.example.floppy.data.Interactor.remote.InteractorFirestoreImpl;
import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Models.Estado_User;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.GlobalView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
}