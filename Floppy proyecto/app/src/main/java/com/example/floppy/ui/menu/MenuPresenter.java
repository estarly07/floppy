package com.example.floppy.ui.menu;

import com.example.floppy.Domain.Models.Estado;
import com.example.floppy.Domain.Entitys.FriendEntity;
import com.example.floppy.Domain.Models.Message;
import com.example.floppy.Domain.Models.User;

import java.util.ArrayList;

public interface MenuPresenter {

    void showState(ArrayList<ArrayList<Estado>> estados);

    void getStates(MenuPresenter presenter);

    /**
     * OBTENER LOS AMIGOS DE LA BD REMOTA
     */
    void getMyFriends();

    /**
     * AÑADIR LOS CONTACTOS QUE TIENE EL USUARIO DE AMIGOS EN UN ARRAY
     *
     * @param friend usuario que se va agregar a la lista que tiene el adapter
     */
    void addChats(User friend);

    void friendIsWriting(FriendEntity friendEntity, Message message);

    void listenerChatFriend(FriendEntity friendEntity);

    void destroyAllListenersFriends();
}
