package com.example.floppy.Domain.Entitys;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.floppy.utils.Global.GlobalUtils;

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
    @ColumnInfo(name ="idFriend" )
    public String idFriend;

    @NonNull
    @ColumnInfo(name ="idOwner" )
    public String idOwner;

    @NonNull
    @ColumnInfo(name ="nick" )
    public String nick;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name ="idChat" )
    public String idChat;
}
