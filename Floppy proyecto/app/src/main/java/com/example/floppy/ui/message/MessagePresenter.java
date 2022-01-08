package com.example.floppy.ui.message;

import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Chat;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.models.User;

import java.util.ArrayList;

public interface MessagePresenter {

    void getStickers();

    void getChatLocal(String idChat);

    void showStateUser(String response);

    Boolean sendMessages(String idChat, Message message);

    void showDataFriend(String nick,String photo);

    void showMessagesChat(FriendEntity friendEntity);

    void getStateUser(String idUser);

    void cancelListener();

    void getMessages(Chat chat);

    void getMessagesLocal(String idChat);

    void showMessages(ArrayList<Message> messages,String idChat);

    void showStickers(ArrayList<StickersEntity> list);

    void searchChat(User userSelect);
    /**CREAR UN NUEVO CHAT
     *@param userSelect el amigo
     * */
    void createChat(User userSelect, Message message);

    void insertFriendLocal(User friend, String idChat);

    void addSticker(String image);

    void showDialogAddSticker();

    void downloadApp();

    Boolean validateInstalledApk();

    void openApk();

    void installApk();

    boolean validateDownloadedApk();

    void showDialogAddOrDeleteSticker(Message message);

    void deleteSticker(String message);

    void searchFriend(String idUser);
}
