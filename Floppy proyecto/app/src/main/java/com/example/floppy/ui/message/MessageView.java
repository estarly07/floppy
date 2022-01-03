package com.example.floppy.ui.message;

import com.example.floppy.data.Models.Message;
import com.example.floppy.data.Entitys.StickersEntity;


import java.util.ArrayList;

public interface MessageView {

    void showDataFriend(String nick,String photo);

    void showMessages(ArrayList<Message> messages, String myId,String idChat);

    void showStateUser(String response);

    void showStickers(ArrayList<StickersEntity> stickers);

    void showDialogAddSticker();

    /**ENVIAR UN MENSAJE Y INSERTAR EL AMIGO EN LA BD O NO
     * @param insertFriend*/
    void sendAndInsertFriend(Boolean insertFriend);
}
