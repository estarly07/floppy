package com.example.floppy.data.Interactor.remote;

import com.example.floppy.data.Entitys.FriendEntity;
import com.example.floppy.data.Models.Chat;
import com.example.floppy.data.Models.Estado_User;
import com.example.floppy.data.Models.Message;
import com.example.floppy.data.Entitys.StickersEntity;
import com.example.floppy.data.Models.User;
import com.example.floppy.ui.message.MessagePresenter;
import com.example.floppy.ui.contacts.ContactsPresenter;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.ui.login.LoginPresenter;
import com.example.floppy.ui.menu.MenuPresenter;

import java.util.ArrayList;

public interface Interactor {

    void Login(User user, LoginPresenter presenter);

    /**VALIDAR QUE EL USUARIO NO EXISTA*/
    void validateUser(User user, LoginPresenter presenter);

    void insertUserRemote(User user, LoginPresenter presenter);

    void getEstados(String id, MenuPresenter presenter, GlobalPresenter presenterMaster);

    void getMyData(LoginPresenter presenter);

    void searchChat(Chat chat, MessagePresenter messagePresenter);

    void createChat(Chat chat, User friend, Message message, MessagePresenter messagePresenter);

    void getMessages(MessagePresenter presenter, Chat chat);

    void getAllUsers(ContactsPresenter contactsPresenter);

    void sendMessages(String idChat, Message message,String date);

    void getEstadoUser(String idUser);

    void cancelListener();

    void updateEstado(String idUser, Estado_User estado_user);

    void showStickers(MessagePresenter presenter, ArrayList<StickersEntity> list);

    void getFriends(String idFriend,MenuPresenter presenter);

    void listenerChatFriend(FriendEntity friendEntity,MenuPresenter menuPresenter);

    void destroyAllListenersFriends();

    void friendIsWriting(FriendEntity friendEntity, MenuPresenter menuPresenter, Message message);
}
