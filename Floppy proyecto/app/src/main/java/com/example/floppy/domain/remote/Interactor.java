package com.example.floppy.domain.remote;

import android.net.Uri;

import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.message.MessagePresenter;
import com.example.floppy.ui.contacts.ContactsPresenter;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.ui.login.LoginPresenter;
import com.example.floppy.ui.menu.MenuPresenter;

import java.util.ArrayList;
import java.util.Map;

public interface Interactor {

    void Login(User user, LoginPresenter presenter);

    /**VALIDAR QUE EL USUARIO NO EXISTA*/
    void validateUser(User user, LoginPresenter presenter);

    void insertUserRemote(Map<String,Object> map, LoginPresenter presenter);

    void getEstados(String id, MenuPresenter presenter, GlobalPresenter presenterMaster);

    void getMyData(LoginPresenter presenter,String idUser);

    void searchChat(Map<String, Object> chat, MessagePresenter messagePresenter);

    void createChat(Map<String, Object> chat, User friend, Message message, MessagePresenter messagePresenter);

    void getMessages(MessagePresenter presenter, Map<String, Object> chat);

    void getAllUsers(ContactsPresenter contactsPresenter,String idUser);

    void sendMessages(String idChat, String conversation);

    void getEstadoUser(String idUser);

    void cancelListener();

    void updateState(String idUser, String state);

    void showStickers(MessagePresenter presenter, ArrayList<StickersEntity> list);

    void getFriends(String idFriend,MenuPresenter presenter);

    void listenerChatFriend(FriendEntity friend, MenuPresenter menuPresenter);

    void destroyAllListenersFriends();

    void friendIsWriting(FriendEntity friendEntity, MenuPresenter menuPresenter, String message);

    void savedAudio(String name, Uri uri, String idChat,User friend, MessagePresenter messagePresenter);

    void downloadFile(String message, Message.TypesMessages record);
}
