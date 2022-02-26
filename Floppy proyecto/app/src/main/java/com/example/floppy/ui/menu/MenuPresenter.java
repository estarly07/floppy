package com.example.floppy.ui.menu;

import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.User;

import java.util.ArrayList;

public interface MenuPresenter {

    void showState(String states);

    void getStates(MenuPresenter presenter);

    /**
     * OBTENER LOS AMIGOS DE LA BD REMOTA
     */
    void getMyFriends();

    /**
     * AÑADIR LOS CONTACTOS QUE TIENE EL USUARIO DE AMIGOS EN UN ARRAY
     *
     * @param user usuario que se va agregar a la lista que tiene el adapter
     */
    void addChats(String user);

    void friendIsWriting(FriendEntity friendEntity, String message);

    void listenerChatFriend(FriendEntity friendEntity);

    void destroyAllListenersFriends();

    void searchChat(ArrayList<FriendEntity> friendEntities, ArrayList<User> friends,String name);
}
