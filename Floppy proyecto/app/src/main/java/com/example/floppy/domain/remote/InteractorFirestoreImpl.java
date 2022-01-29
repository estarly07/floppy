package com.example.floppy.domain.remote;

import android.content.Context;
import android.net.Uri;

import com.estarly.data.remote.Firestore;
import com.example.floppy.domain.entities.FriendEntity;
import com.example.floppy.domain.models.Chat;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.entities.StickersEntity;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.message.MessagePresenter;
import com.example.floppy.ui.contacts.ContactsPresenter;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.ui.login.LoginPresenter;
import com.example.floppy.ui.menu.MenuPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class InteractorFirestoreImpl implements Interactor{
    final private GlobalPresenter   presenterMaster;
    final private Firestore         firestore;
          private CountDownLatch    countDownLatch;

    public InteractorFirestoreImpl(Context context, GlobalPresenter presenterMaster) {
        firestore = new Firestore(context);
        this.presenterMaster = presenterMaster;

    }

    @Override
    public void Login(User user, LoginPresenter presenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.login(new String[]{user.getEmail(),user.getPass()},countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }

        if (firestore.getInputResult().getResponse()) {
            user.setIdUser(firestore.getInputResult().getResult());
            presenter.getMyDataRemote();
            presenter.nextActivity();
        }
        else { presenter.showMessage("");
        }
    }

    @Override
    public void validateUser(User user, LoginPresenter presenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.login(new String[]{user.getEmail(),user.getPass()},countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }

        if (!firestore.getInputResult().getResponse()) {
            presenter.showInputUser(); }
        else { presenter.showMessage("Ese usuario ya existe");
        }
    }

    @Override
    public void insertUserRemote(Map<String, Object> map, LoginPresenter presenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.registerUserDataBase(new String[]{map.get("email").toString(), map.get("pass").toString()},countDownLatch);
        try {
            countDownLatch.await();

            if (firestore.getInputResult().getResponse()) {
                map.put("idUser", firestore.getInputResult().getResult());
                countDownLatch = new CountDownLatch(1);
                firestore.updateUserDataBase(map,map.get("idUser").toString(),countDownLatch);
                countDownLatch.await();
                if (firestore.getInputResult().getResponse()) {
                    presenter.showMessage("Bienvenido");
                    presenter.insertUserLocal(map.get("idUser").toString());
                    presenter.nextActivity();}
                else { presenter.showMessage(firestore.getInputResult().getResult()); }
            }
            else { presenter.showMessage(""); }
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void getEstados(String id, MenuPresenter presenter, GlobalPresenter presenterMaster) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        firestore.getStates(countDownLatch, id);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (firestore.getInputResult().getResponse()) {
            presenter      .showState(firestore.getInputResult().getResult());
        } else {
            presenterMaster.showMessage(firestore.getInputResult().getResult());
        }
    }

    @Override
    public void getMyData(LoginPresenter presenter, String idUser) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        firestore.getMyData(countDownLatch, idUser);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (firestore.getInputResult().getResponse()) {
            presenter.insertUserOwner(firestore.getInputResult().getResult());
        } else {
            presenterMaster.showMessage("");
        }
    }

    @Override
    public void searchChat(Map<String, Object> chat, MessagePresenter messagePresenter) {
        countDownLatch = new CountDownLatch(1);
        String[] users = (String[]) chat.get("users");
        firestore.searchChat(users, countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }
        //si encontro que este usuario ya habia hablado con este usuario obtener el id de ese chat
        if (firestore.getInputResult().getResponse()){
            chat.put("idChat", firestore.getInputResult().getResult());
            messagePresenter.getMessages(chat);
        }else{
            firestore.listenerStatusUser(users[1], response -> {
                messagePresenter.showStateUser( response);
                return null;
            });
        }
    }

    @Override
    public void createChat(Map<String, Object> chat, User friend, Message message, MessagePresenter messagePresenter) {
        countDownLatch = new CountDownLatch(1);
        String[] users = (String[]) chat.get("users");
        chat.put("users", Arrays.asList(users));
        firestore.createChat(chat,countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }
        if (firestore.getInputResult().getResponse()){
            chat.put("users", users);
            chat.put("idChat",firestore.getInputResult().getResult());
            messagePresenter.insertFriendLocal(friend, chat.get("idChat").toString());
            messagePresenter.sendMessages(chat.get("idChat").toString(),message);
            messagePresenter.getMessages(chat);
        }else{
            System.out.println("NO CREADO");

        }
    }

    @Override
    public void getMessages(MessagePresenter presenter, Map<String, Object> chat) {
        String[] users = (String[]) chat.get("users");
        System.out.println("ID CHAT INTERATOR "+chat.get("idChat").toString());
        firestore.getMessagesByIdChat(chat.get("idChat").toString(),users[1], respChat->{
            presenter.showMessages(respChat, chat.get("idChat").toString() );
            return  null;
        },respStatus->{
            presenter.showStateUser(respStatus);
            return  null;
        });
    }

    @Override
    public void getAllUsers(ContactsPresenter contactsPresenter, String idUser) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        firestore.getAllUsers(countDownLatch,idUser);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (firestore.getInputResult().getResponse()) {
            contactsPresenter.showContacts(firestore.getInputResult().getResult());
        } else {

        }
    }

    @Override
    public void sendMessages(String idChat, String conversation) {
      firestore.sendMessages(idChat,conversation);
    }

    @Override
    public void getEstadoUser(String idUser) {

    }

    @Override
    public void cancelListener() {
        firestore.cancelListener(); }

    @Override
    public void updateState(String idUser, String state) {
        firestore.updateState(idUser, state);
    }

    @Override
    public void showStickers(MessagePresenter presenter, ArrayList<StickersEntity> list) {
        presenter.showStickers(list);
    }

    @Override
    public void getFriends(String idFriend, MenuPresenter presenter) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        firestore.getFriends(countDownLatch,idFriend);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (firestore.getInputResult().getResponse()) {
            presenter.addChats(firestore.getInputResult().getResult());
        } else {

        }

    }

    @Override
    public void listenerChatFriend(FriendEntity friend, MenuPresenter menuPresenter) {
        firestore.listenerChatFriend(friend.idChat,(response)->{
            friendIsWriting(friend, menuPresenter, response);
            return null;
        });
    }

    @Override
    public void destroyAllListenersFriends() {
        firestore.destroyAllListenersFriends();
    }

    @Override
    public void friendIsWriting(FriendEntity friendEntity, MenuPresenter menuPresenter, String message) {
        menuPresenter.friendIsWriting(friendEntity, message);
    }

    @Override
    public void savedAudio(String name, Uri uri, String idChat, MessagePresenter messagePresenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.savedAudio(uri, countDownLatch);
        try {
            countDownLatch.await();
            if(firestore.getInputResult().getResponse()){
                presenterMaster.sendMessage(name,idChat,messagePresenter);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadFile(String message, Message.TypesMessages record) {
        firestore.downloadFile(message,record.toString());
    }

}
