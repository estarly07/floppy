package com.example.floppy.data.Conexion.BD.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.floppy.data.Conexion.BD.Dao.FriendDao;
import com.example.floppy.data.Conexion.BD.Dao.StickerDao;
import com.example.floppy.data.Conexion.BD.Dao.UserDao;
import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Entitys.UserEntity;
import com.example.floppy.utils.Global.GlobalUtils;

@Database(
        entities = {
                UserEntity     .class,
                StickersEntity .class,
                FriendEntity   .class
        },
        version  = 1)
public abstract class SqliteDb extends RoomDatabase {
    public abstract StickerDao stickerDao();
    public abstract UserDao    userDao();
    public abstract FriendDao  friendDao();

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
