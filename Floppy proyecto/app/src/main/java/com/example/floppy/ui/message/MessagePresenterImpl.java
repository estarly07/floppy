package com.example.floppy.ui.message;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.example.floppy.BuildConfig;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Entitys.UserEntity;
import com.example.floppy.data.Interactor.local.InteractorLocal;
import com.example.floppy.data.Interactor.local.InteractorSqlite;
import com.example.floppy.data.Interactor.remote.Interactor;
import com.example.floppy.data.Interactor.remote.InteractorFirestoreImpl;
import com.example.floppy.data.Models.Chat;
import com.example.floppy.data.Models.Message;
import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Models.User;
import com.example.floppy.data.Services.ServiceDownload;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.utils.Extensions;
import com.example.floppy.utils.Global.GlobalUtils;

import java.io.File;
import java.util.ArrayList;

public class MessagePresenterImpl implements MessagePresenter {
    private Context         context;
    private MessageView     messageView;
    private Interactor      interactor;
    private InteractorLocal interactorLocal;
    private GlobalPresenter globalPresenter;

    public MessagePresenterImpl(Context context, Activity activity, MessageView messageView, GlobalPresenter globalPresenter) {
        this.context         = context;
        this.messageView     = messageView;
        this.interactor      = new InteractorFirestoreImpl(context, globalPresenter, activity);
        this.interactorLocal = new InteractorSqlite(context);
        this.globalPresenter = globalPresenter;
    }

    public void showStateUser(String response) {
        messageView.showStateUser(response);
    }

    public Boolean sendMessages(String idChat, Message message) {
        if (message.getMessage().trim().isEmpty()) { return false; }
        message.setUser(User.getInstance().getIdUser());
        message.setHora(GlobalUtils.getHora());
        new Thread(() -> interactor.sendMessages(idChat, message)).start();
        return true;
    }

    public void showDataFriend(String nick, String photo) { messageView.showDataFriend(nick, photo); }


    public void getStateUser(String idUser) {
        interactor.getEstadoUser(idUser);
    }


    public void cancelListener() { interactor.cancelListener(); }

    @Override
    public void getMessages(Chat chat) {
        interactor.getMessages(this, chat);
    }

    @Override
    public void showMessages(ArrayList<Message> messages, String idChat) { messageView.showMessages(messages, User.getInstance().getIdUser(), idChat); }

    public void getStickers() {
        new Thread(() -> {
            interactorLocal.getStickers(this, User.getInstance().getIdUser());
        }).start();
    }
    public void showStickers(ArrayList<StickersEntity> list) { messageView.showStickers(list); }

    @Override
    public void searchChat(User userSelect) {
        String[] users = new String[2];
        users [0]      = User.getInstance().getIdUser();
        users [1]      = userSelect.getIdUser();
        Chat chat      = new Chat();

        chat.setIdChat  ("");
        chat.setUsers   (users);
        chat.setMensajes(new Message[]{});

        new Thread(() -> interactor.searchChat(chat,this)).start();
    }

    @Override
    public void createChat(User userSelect, Message message) {
        String[] users = new String[2];
        users [0]      = User.getInstance().getIdUser();
        users [1]      = userSelect.getIdUser();
        Chat chat      = new Chat();

        chat.setUsers   (users);
        chat.setMensajes(new Message[]{});

        new Thread(() -> interactor.createChat(chat, userSelect, message, this)).start();
    }

    @Override
    public void insertFriendLocal(User friend, String idChat) {
        UserEntity   userEntity   = new UserEntity();
        FriendEntity friendEntity = new FriendEntity();

        userEntity.idUser       = friend.getIdUser();
        userEntity.messageUser  = friend.getMessageUser();
        userEntity.name         = "";
        userEntity.photo        = friend.getPhoto();

        interactorLocal.insertUser(userEntity);

        friendEntity.idFriend   = friend.getIdUser();
        friendEntity.idOwner    = User.getInstance().getIdUser();
        friendEntity.idChat     = idChat;
        friendEntity.nick       = friend.getName();

        interactorLocal.insertFriend(friendEntity);
    }

    @Override
    public void addSticker(String image) {
        if(image.isEmpty() || Extensions.Companion.validateUrl(image)){ return; }
        new Thread(() -> {
            StickersEntity stickersEntity = new StickersEntity();
            stickersEntity.fk_idUser = User.getInstance().getIdUser();
            stickersEntity.urlImage  = image;

            interactorLocal.insertSticker(stickersEntity);
        getStickers(); }).start();
    }

    @Override
    public void showDialogAddSticker() { messageView.showDialogAddSticker();}

    @Override
    public void downloadApp() {
        ServiceDownload serviceDownload = new ServiceDownload(this);
        globalPresenter.beginDownloadApp(serviceDownload.onDownloadComplete);
        serviceDownload.beginDownload(context);
    }

    @Override
    public Boolean validateInstalledApk() { return Extensions.Companion.validateInstallApk(GlobalUtils.packageStickerApk, context); }

    @Override
    public void openApk() {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(GlobalUtils.packageStickerApk);
        if(intent == null){
            //TODO no open app message
        }else{
            context.startActivity(intent);
        }
    }

    @Override
    public void installApk() {
        File file = new File(context.getExternalFilesDir(null), GlobalUtils.nameStickerApk);
        Intent intentApk ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            intentApk = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intentApk.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentApk.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentApk.setData(path);
        }else{
            Uri apkUri = Uri.fromFile(file);
            intentApk = new Intent(Intent.ACTION_VIEW);
            intentApk.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intentApk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intentApk);
        } catch (ActivityNotFoundException e) {
        }
    }

    @Override
    public boolean validateDownloadedApk() {
        File file = new File(context.getExternalFilesDir(null), GlobalUtils.nameStickerApk);
        return file.exists();
    }

    @Override
    public void searchFriend(String idUser) {
        new Thread(() -> {
            messageView.sendAndInsertFriend(interactorLocal.getFriend(idUser) == null);
        }).start();
    }


    public void showMessagesChat(FriendEntity friendEntity) {
        String[] users = new String[2];
        users [0]      = User.getInstance().getIdUser();
        users [1]      = friendEntity.idFriend;
        Chat chat      = new Chat();

        chat.setIdChat  (friendEntity.idChat);
        chat.setUsers   (users);
        chat.setMensajes(new Message[]{});
        new Thread(() -> interactor.getMessages(this, chat)).start();
    }
}
