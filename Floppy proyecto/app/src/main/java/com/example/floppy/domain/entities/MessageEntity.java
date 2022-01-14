package com.example.floppy.domain.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.models.StateMessage;
import com.example.floppy.utils.Global.GlobalUtils;

@Entity(
        tableName   = GlobalUtils.messageTable,
        foreignKeys ={
                @ForeignKey
                        (
                                onDelete      = ForeignKey.CASCADE,
                                parentColumns = "idChat",
                                childColumns  = "fk_idChat",
                                entity        = ChatEntity.class
                        ),
        }

)
public class MessageEntity {

    @PrimaryKey
    @NonNull
    public String idMessage;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "user")
    public String user;/**USUARIO QUE LO ENVIO*/

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "hour")
    public String hour;

    @ColumnInfo(name = "typeMessage")
    public Message.TypesMessages typeMessage;

    @ColumnInfo(name = "state")
    public StateMessage state;

    @ColumnInfo(name="fk_idChat")
    public String fk_idChat;

}
