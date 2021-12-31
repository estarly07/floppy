package com.example.floppy.ui.menu;

import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.Estado;
import com.example.floppy.data.Models.User;

import java.util.ArrayList;

public interface MenuView  {
    /**
     * MOSTRAR UN RECYCLER HORIZONTAL CON TODOS LOS ESTADOS
     */
    void showState(ArrayList<ArrayList<Estado>> estados);
    /**
     * MOSTRAR UN RECYCLER CON LOS CHATS DEL AMIGO DEL USUARIO
     *  @param friend array de los amigos
     * @param friendEntity data local
     */
    void showChats(User friend, FriendEntity friendEntity);
}
