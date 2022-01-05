package com.example.floppy.Domain.Entitys;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.floppy.utils.Global.GlobalUtils;

@Entity(
    tableName   = GlobalUtils.stickersTable,
    foreignKeys =
    @ForeignKey
            (
                onDelete      = ForeignKey.CASCADE,
                parentColumns = "idUser",
                childColumns  = "fk_idUser",
                entity        = UserEntity.class
            )
)
public class StickersEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int id;

    @NonNull
    @ColumnInfo(name = "urlImage")
    public String urlImage;

    @ColumnInfo(name = "fk_idUser")
    public String fk_idUser;
}
