package com.example.floppy.data.Conexion.BD.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floppy.domain.entities.MessageEntity;
import com.example.floppy.utils.global.GlobalUtils;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insertMessage(MessageEntity messageEntity);

    @Query("SELECT * FROM "+ GlobalUtils.messageTable +" WHERE fk_idChat = :idChat")
    List<MessageEntity> getMessages(String idChat);

    @Query("SELECT * FROM "+ GlobalUtils.messageTable +" WHERE idMessage = :idMessage")
    MessageEntity getMessage(String idMessage);
}
