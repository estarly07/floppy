package com.example.floppy.ui.message;

import android.content.Context;

import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.entities.StickersEntity;


import java.util.ArrayList;

public interface MessageView {

    void showDataFriend(String nick,String photo);

    void showMessages(ArrayList<Message> messages, String myId,String idChat);

    void showDialogAddOrDeleteSticker(Context context, Message message, Boolean delete);

    void showStateUser(String response);

    void showStickers(ArrayList<StickersEntity> stickers);

    void showDialogAddSticker();

    /**ENVIAR UN MENSAJE Y INSERTAR EL AMIGO EN LA BD O NO
     * @param insertFriend*/
    void sendAndInsertFriend(Boolean insertFriend);

    void cleanEdittext();
}
