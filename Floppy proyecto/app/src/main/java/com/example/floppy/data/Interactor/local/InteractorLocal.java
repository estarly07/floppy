package com.example.floppy.data.Interactor.local;

import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Entitys.UserEntity;
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
