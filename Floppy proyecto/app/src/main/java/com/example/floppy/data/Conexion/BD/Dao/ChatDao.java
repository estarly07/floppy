package com.example.floppy.data.Conexion.BD.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floppy.domain.entities.ChatEntity;
import com.example.floppy.utils.global.GlobalUtils;

import java.util.ArrayList;

@Dao
public interface ChatDao {

    @Insert
    void insertChat(ChatEntity chatEntity);

    @Query("SELECT * FROM "+ GlobalUtils.chatTable +" WHERE idChat = :idChat")
    ChatEntity  getChat(String idChat);

}
