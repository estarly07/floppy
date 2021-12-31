package com.example.floppy.data.Conexion.BD.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Models.User;
import com.example.floppy.utils.Global.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface StickerDao {
    @Insert
    void insertSticker(StickersEntity sticker);

    @Query("SELECT * FROM " + GlobalUtils.stickersTable + " WHERE fk_idUser = :fk_idUser")
    List<StickersEntity> getStickers(String fk_idUser);

}
