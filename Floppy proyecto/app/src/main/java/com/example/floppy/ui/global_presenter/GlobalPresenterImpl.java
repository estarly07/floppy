package com.example.floppy.ui.global_presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;

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
import com.example.floppy.utils.global.GlobalUtils;
import com.example.floppy.utils.Permission;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GlobalPresenterImpl implements GlobalPresenter {
    private User            user = User.getInstance();
    private GlobalView      globalView;
    private Context         context;
    private Interactor      interactor;
    private InteractorLocal interactorLocal;
    private Activity        activity;

    public GlobalPresenterImpl(GlobalView globalView, Context context, Activity activity) {
        this.globalView      = globalView;
        this.context         = context;
        this.activity        = activity;
        this.interactorLocal = new InteractorSqlite(context);
        this.interactor      = new InteractorFirestoreImpl(context,this);/**INTERACTOR QUE LLAMA A FIRESTORE*/
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
    public void showHandlingGeneral(boolean show, String title) {
        globalView.showHandlingGeneral(show, title);
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
        interactor.updateState(User.getInstance().getIdUser(), estado_user.toString());
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
    public void recordAudio(String idChat, User friend, MessagePresenter messagePresenter) {
        if(!Permission.Companion.validatePermissionToRecord(activity)){
            Permission.Companion.initValidatePermissionToRecord(activity);
        }else
            globalView.recordAudio(GlobalUtils.getDateNow()+idChat, idChat, friend, messagePresenter);
    }

    @Override
    public void sendImage(String idChat, Uri uri, User friend, MessagePresenter messagePresenter) {
        globalView.showHandlingGeneral(true, "Espera a que \ntermine de enviar la imagen");
        System.out.println("CHAT "+idChat);
        File file= new File(context.getExternalFilesDir(null), "/"+ com.estarly.data.Global.GlobalUtils.TypeFile.IMAGE.getDir());
        file.mkdirs();
        String name = GlobalUtils.getDateNow()+"_"+idChat+".jpg";

        try {
            File img = new File(file.getPath(),name);
            FileOutputStream out = new FileOutputStream(img);
            MediaStore.Images.Media.getBitmap(this.activity.getContentResolver(), uri)
                    .compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            saveImageInStorage(img);
            uri = Uri.fromFile(img);
        } catch (IOException e) { e.printStackTrace(); }
        Uri finalUri = uri;
        new Thread(() -> interactor.savedFile(
                name,
                finalUri,
                idChat,
                friend,
                Message.TypesMessages.IMAGE, com.estarly.data.Global.GlobalUtils.TypeFile.IMAGE, messagePresenter)).start();
    }

    private void saveImageInStorage(File file){
        MediaScannerConnection.scanFile(context,
                new String[] { file.toString() } , null,
                (path, uri) -> {
                });
    }

    @Override
    public void showImage(boolean send, String uri) { globalView.showImage(send,Uri.parse(uri)); }

    @Override
    public void stopRecord(String[] data, String idChat, User friend, MessagePresenter messagePresenter) {
        globalView.stopAudio();
        globalView.showHandlingGeneral(true,"Espera a que se envie el audio" );
        new Thread(() -> interactor.savedFile(
                data[1],
                Uri.fromFile(new File(data[0]+"/"+data[1]+".mp3")),
                idChat,
                friend ,
                Message.TypesMessages.RECORD, com.estarly.data.Global.GlobalUtils.TypeFile.AUDIO, messagePresenter)).start();
    }

    @Override
    public void sendMessage(String name, String idChat, User friend, Message.TypesMessages typesMessage, MessagePresenter messagePresenter) {
        Message message = new Message();
        message.setMessage(name);
        message.setTypeMessage(typesMessage);
        if(idChat.equals("")){ messagePresenter.createChat(friend,message); }
        else{
            if (interactorLocal.getFriend(friend.getIdUser()) == null) {
                System.out.println("HOLE");
                messagePresenter.insertFriendLocal(friend, idChat);
            }
            messagePresenter.sendMessages(idChat, message);
        }
        globalView.showHandlingGeneral(false,"" );
    }

    @Override
    public void getImage(String idChat, User friend, MessagePresenter messagePresenter) { globalView.getMessage(idChat,friend, messagePresenter); }


}