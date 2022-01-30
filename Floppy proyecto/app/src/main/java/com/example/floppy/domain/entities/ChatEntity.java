package com.example.floppy.domain.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.floppy.utils.global.GlobalUtils;

@Entity(
        tableName   = GlobalUtils.chatTable,
        foreignKeys ={
                @ForeignKey
                        (
                            onDelete      = ForeignKey.CASCADE,
                            parentColumns = "idFriend",
                            childColumns  = "idFriend",
                            entity        = FriendEntity.class
                        ),
                @ForeignKey
                        (
                            onDelete      = ForeignKey.CASCADE,
                            parentColumns = "idUser",
                            childColumns  = "idOwner",
                            entity        = UserEntity.class
                        )
        }

)
public class ChatEntity {
    @PrimaryKey
    @NonNull
    public String idChat  ;

    @ColumnInfo(name = "idOwner")
    public String idOwner ;

    @ColumnInfo(name = "idFriend")
    public String idFriend;
}
