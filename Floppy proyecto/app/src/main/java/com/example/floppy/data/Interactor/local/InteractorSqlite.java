package com.example.floppy.data.Interactor.local;

import android.content.Context;

import com.example.floppy.data.Conexion.BD.Dao.FriendDao;
import com.example.floppy.data.Conexion.BD.Dao.StickerDao;
import com.example.floppy.data.Conexion.BD.Dao.UserDao;
import com.example.floppy.data.Conexion.BD.Database.SqliteDb;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Entitys.UserEntity;
import com.example.floppy.ui.message.MessagePresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class InteractorSqlite implements InteractorLocal{
    private Context    context;
    private StickerDao stickerDao;
    private FriendDao  friendDao;
    private UserDao    userDao;

    public InteractorSqlite(Context context) {
        this.context = context;
        stickerDao   = SqliteDb.getInstance(context).stickerDao();
        userDao      = SqliteDb.getInstance(context).userDao();
        friendDao    = SqliteDb.getInstance(context).friendDao();
    }


    @Override
    public void insertUser(UserEntity userEntity) { userDao.insertUser(userEntity); }

    @Override
    public void insertFriend(FriendEntity friendEntity) { friendDao.insertFriend(friendEntity); }

    @Override
    public void insertSticker(StickersEntity stickersEntity) { stickerDao.insertSticker(stickersEntity);}

    @Override
    public UserEntity getUser(String idUser) { return userDao.getOwner(idUser); }

    @Override
    public List<FriendEntity> getFriends(String idUser) { return friendDao.getFriends(idUser); }

    @Override
    public FriendEntity getFriend(String idFriend) { return friendDao.getFriend(idFriend); }

    @Override
    public void getStickers(MessagePresenterImpl messagePresenter, String idUser) { messagePresenter.showStickers((ArrayList<StickersEntity>) stickerDao.getStickers(idUser)); }


}
