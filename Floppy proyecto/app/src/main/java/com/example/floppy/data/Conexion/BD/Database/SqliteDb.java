package com.example.floppy.data.Conexion.BD.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.floppy.data.Conexion.BD.Dao.ChatDao;
import com.example.floppy.data.Conexion.BD.Dao.FriendDao;
import com.example.floppy.data.Conexion.BD.Dao.MessageDao;
import com.example.floppy.data.Conexion.BD.Dao.StickerDao;
import com.example.floppy.data.Conexion.BD.Dao.UserDao;
import com.example.floppy.domain.entities.ChatEntity;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.entities.MessageEntity;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.entities.UserEntity;
import com.example.floppy.utils.Global.GlobalUtils;

@Database(
        entities = {
                UserEntity     .class,
                StickersEntity .class,
                FriendEntity   .class,
                ChatEntity     .class,
                MessageEntity  .class
        },
        version  = 1)
public abstract class SqliteDb extends RoomDatabase {
    public abstract StickerDao stickerDao();
    public abstract UserDao    userDao   ();
    public abstract FriendDao  friendDao ();
    public abstract ChatDao    chatDao   ();
    public abstract MessageDao messageDao();

    private static SqliteDb INSTANCE = null;
    public static SqliteDb getInstance(Context context){
        if (INSTANCE!= null){
            return INSTANCE;
        }
        synchronized(context){
            INSTANCE = Room.databaseBuilder(
                    context    .getApplicationContext(),
                    SqliteDb   .class,
                    GlobalUtils.nameDatabase
            ).build();
            return  INSTANCE;
        }

    }
}
