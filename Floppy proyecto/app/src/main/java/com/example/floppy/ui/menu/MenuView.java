package com.example.floppy.ui.menu;

import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Estado;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.models.User;

import java.util.ArrayList;

public interface MenuView  {
    /**
     * MOSTRAR UN RECYCLER HORIZONTAL CON TODOS LOS ESTADOS
     */
    void showState(ArrayList<ArrayList<Estado>> estados);
    /**
     * MOSTRAR UN RECYCLER CON LOS CHATS DEL AMIGO DEL USUARIO
     * @param friends array de los amigos
     * @param friendEntities amigos guardados en local
     */
    void showChats(ArrayList<User> friends, ArrayList<FriendEntity> friendEntities);

    void friendIsWriting(FriendEntity friendEntity, Message message);

    void showChatFounds(ArrayList<FriendEntity> friendSearch,ArrayList<User> users);
}
