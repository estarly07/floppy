package com.example.floppy.data.Conexion.BD.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.floppy.domain.entities.UserEntity;
import com.example.floppy.utils.Global.GlobalUtils;

@Dao
public interface UserDao {
    @Insert
    void insertUser(UserEntity userEntity);

    @Query("SELECT * FROM "+GlobalUtils.userTable+" WHERE idUser = :idUser")
    UserEntity getOwner(String idUser);

}
