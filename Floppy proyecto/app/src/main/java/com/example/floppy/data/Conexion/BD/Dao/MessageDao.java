package com.example.floppy.data.Conexion.BD.Dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.floppy.domain.entities.MessageEntity;

@Dao
public interface MessageDao {

    @Insert
    void insertMessage(MessageEntity messageEntity);
}
