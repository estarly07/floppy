package com.example.floppy.data.Conexion.BD.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.utils.Global.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FriendDao {
    @Insert
    void insertFriend(FriendEntity friendEntity);

    @Query("SELECT * FROM "+ GlobalUtils.userTable+" AS u JOIN "+GlobalUtils.friendTable+" AS f ON f.idOwner = u.idUser WHERE u.idUser = :idOwner")
    List<FriendEntity> getFriends(String idOwner);

    @Query("SELECT * FROM "+GlobalUtils.friendTable+" WHERE idFriend =:idFriend")
    FriendEntity getFriend(String idFriend);

    @Query("UPDATE "+GlobalUtils.friendTable+" SET nick = :nick")
    void updateFriendNick(String nick);
}
