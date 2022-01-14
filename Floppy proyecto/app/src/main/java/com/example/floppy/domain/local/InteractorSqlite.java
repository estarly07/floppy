package com.example.floppy.domain.local;

import android.content.Context;

import com.example.floppy.data.Conexion.BD.Dao.ChatDao;
import com.example.floppy.data.Conexion.BD.Dao.FriendDao;
import com.example.floppy.data.Conexion.BD.Dao.MessageDao;
import com.example.floppy.data.Conexion.BD.Dao.StickerDao;
import com.example.floppy.data.Conexion.BD.Dao.UserDao;
import com.example.floppy.data.Conexion.BD.Database.SqliteDb;
import com.example.floppy.domain.entities.ChatEntity;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.entities.MessageEntity;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.entities.UserEntity;
import com.example.floppy.ui.message.MessagePresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class InteractorSqlite implements InteractorLocal{
    private Context    context;
    private StickerDao stickerDao;
    private FriendDao  friendDao;
    private UserDao    userDao;
    private ChatDao    chatDao;
    private MessageDao messageDao;

    public InteractorSqlite(Context context) {
        this.context = context;
        stickerDao   = SqliteDb.getInstance(context).stickerDao();
        userDao      = SqliteDb.getInstance(context).userDao();
        friendDao    = SqliteDb.getInstance(context).friendDao();
        chatDao      = SqliteDb.getInstance(context).chatDao();
        messageDao   = SqliteDb.getInstance(context).messageDao();
    }


    @Override
    public void insertUser(UserEntity userEntity) { userDao.insertUser(userEntity); }

    @Override
    public void insertFriend(FriendEntity friendEntity) { friendDao.insertFriend(friendEntity); }

    public void insertChat(ChatEntity chatEntity) { chatDao.insertChat(chatEntity); }

    @Override
    public ChatEntity getChat(String idChat) { return chatDao.getChat(idChat);}

    @Override
    public List<MessageEntity> getMessages(String idChat) { return messageDao.getMessages(idChat);}

    @Override
    public MessageEntity getMessage(String idMessage) {
        return messageDao.getMessage(idMessage);
    }

    @Override
    public void insertMessage(MessageEntity messageEntity) { messageDao.insertMessage(messageEntity); }

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

    @Override
    public void deleteSticker(String sticker) { stickerDao.deleteSticker(sticker); }

    @Override
    public void updateNickFriend(String nick, String idFriend) { friendDao.updateFriendNick(nick,idFriend ); }


}
