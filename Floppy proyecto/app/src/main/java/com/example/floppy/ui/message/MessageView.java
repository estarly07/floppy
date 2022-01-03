package com.example.floppy.ui.message;

import android.content.Context;

import com.example.floppy.data.Models.Message;
import com.example.floppy.data.Entitys.StickersEntity;


import java.util.ArrayList;

public interface MessageView {

    void showDataFriend(String nick,String photo);

    void showMessages(ArrayList<Message> messages, String myId,String idChat);

    void showDialogAddOrDeleteSticker(Context context, Message message, Boolean delete);

    void showStateUser(String response);

    void showStickers(ArrayList<StickersEntity> stickers);

    void showDialogAddSticker();
}
