package com.example.floppy.domain.local;

import com.example.floppy.domain.entities.ChatEntity;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.entities.MessageEntity;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.entities.UserEntity;
import com.example.floppy.ui.message.MessagePresenterImpl;

import java.util.ArrayList;
import java.util.List;

public interface InteractorLocal {

    void insertUser  (UserEntity userEntity);

    void insertFriend(FriendEntity friendEntity);

    void insertChat(ChatEntity chatEntity);

    ChatEntity getChat(String idChat);

    List<MessageEntity> getMessages(String idChat);

    MessageEntity getMessage(String idMessage);

    void insertMessage(MessageEntity messageEntity);

    void insertSticker(StickersEntity stickersEntity);

    UserEntity getUser(String idUser);

    List<FriendEntity> getFriends(String idUser);

    FriendEntity getFriend(String idFriend);

    void getStickers(MessagePresenterImpl messagePresenter,String idUser);

    void deleteSticker(String sticker);

    void updateNickFriend(String nick,String idFriend);
}
