package com.example.floppy.Domain.local;

import com.example.floppy.Domain.Entitys.FriendEntity;
import com.example.floppy.Domain.Entitys.StickersEntity;
import com.example.floppy.Domain.Entitys.UserEntity;
import com.example.floppy.ui.message.MessagePresenterImpl;

import java.util.List;

public interface InteractorLocal {

    void insertUser  (UserEntity userEntity);

    void insertFriend(FriendEntity friendEntity);

    void insertSticker(StickersEntity stickersEntity);

    UserEntity getUser(String idUser);

    List<FriendEntity> getFriends(String idUser);

    FriendEntity getFriend(String idFriend);

    void getStickers(MessagePresenterImpl messagePresenter,String idUser);

    void deleteSticker(String sticker);

    void updateNickFriend(String nick,String idFriend);
}
