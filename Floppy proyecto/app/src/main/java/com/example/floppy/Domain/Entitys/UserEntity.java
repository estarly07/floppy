package com.example.floppy.Domain.Entitys;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.floppy.utils.Global.GlobalUtils;

@Entity(tableName = GlobalUtils.userTable)
public class UserEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idUser")
    public String idUser;
    @NonNull
    @ColumnInfo(name = "name")
    public String name;
    @NonNull
    @ColumnInfo(name = "photo")
    public String photo;
    @NonNull
    @ColumnInfo(name = "messageUser")
    public String messageUser;

}
