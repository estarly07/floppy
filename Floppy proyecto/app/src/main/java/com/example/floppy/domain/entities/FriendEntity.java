package com.example.floppy.domain.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.floppy.utils.global.GlobalUtils;

@Entity(
        tableName   = GlobalUtils.friendTable,
        foreignKeys =
        @ForeignKey
                (
                        onDelete      = ForeignKey.CASCADE,
                        parentColumns = "idUser",
                        childColumns  = "idOwner",
                        entity        = UserEntity.class
                )
)
public class FriendEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name ="idFriend" )
    public String idFriend;

    @NonNull
    @ColumnInfo(name ="idOwner" )
    public String idOwner;

    @NonNull
    @ColumnInfo(name ="nick" )
    public String nick;

    @NonNull
    @ColumnInfo(name ="idChat" )
    public String idChat;
}
