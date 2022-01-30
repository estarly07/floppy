package com.example.floppy.data.Conexion.BD.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.utils.global.GlobalUtils;

import java.util.List;

@Dao
public interface StickerDao {
    @Insert
    void insertSticker(StickersEntity sticker);

    @Query("SELECT * FROM " + GlobalUtils.stickersTable + " WHERE fk_idUser = :fk_idUser")
    List<StickersEntity> getStickers(String fk_idUser);

    @Query("DELETE FROM "+ GlobalUtils.stickersTable + " WHERE urlImage = :sticker")
    void deleteSticker(String sticker);

}
