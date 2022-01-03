package com.example.floppy.ui.message;

import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.Chat;
import com.example.floppy.data.Models.Message;
import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Models.User;

import java.util.ArrayList;

public interface MessagePresenter {

    void getStickers();

    void showStateUser(String response);

    Boolean sendMessages(String idChat, Message message);

    void showDataFriend(String nick,String photo);

    void showMessagesChat(FriendEntity friendEntity);

    void getStateUser(String idUser);

    void cancelListener();

    void getMessages(Chat chat);

    void showMessages(ArrayList<Message> messages,String idChat);

    void showStickers(ArrayList<StickersEntity> list);

    void searchChat(User userSelect);
    /**CREAR UN NUEVO CHAT*/
    void createChat(User userSelect, Message message);

    void insertFriendLocal(User friend, String idChat);

    void addSticker(String image);

    void showDialogAddSticker();

    void downloadApp();

    Boolean validateInstalledApk();

    void openApk();

    void installApk();

    boolean validateDownloadedApk();
}
