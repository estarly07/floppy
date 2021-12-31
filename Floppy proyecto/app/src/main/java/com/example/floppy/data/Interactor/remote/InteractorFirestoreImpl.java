package com.example.floppy.data.Interactor.remote;

import android.app.Activity;
import android.content.Context;

import com.example.floppy.data.Conexion.remote.Firestore;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class InteractorFirestoreImpl implements Interactor{
    final private GlobalPresenter     presenterMaster;
    final private Firestore           firestore;
          private CountDownLatch      countDownLatch;

    public InteractorFirestoreImpl(Context context, GlobalPresenter presenterMaster, Activity activity) {
        firestore = new Firestore(context);
        firestore.setActivity(activity);
        this.presenterMaster = presenterMaster;

    }

    @Override
    public void Login(User user, LoginPresenter presenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.login(user,countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }

        if (firestore.getInputResult().getResponse()) {
            user.setIdUser(firestore.getInputResult().getResult());
            presenter.getMyDataRemote();
            presenter.nextActivity();
        }
        else { presenter.showMessage(firestore.getInputResult().getResult());
        }
    }

    @Override
    public void validateUser(User user, LoginPresenter presenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.login(user,countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }

        if (!firestore.getInputResult().getResponse()) {
            presenter.showInputUser(); }
        else { presenter.showMessage("Ese usuario ya existe");
        }
    }

    @Override
    public void insertUserRemote(User user, LoginPresenter presenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.registerUserDataBase(user,countDownLatch);
        try {
            countDownLatch.await();

            if (firestore.getInputResult().getResponse()) {
                user.setIdUser(firestore.getInputResult().getResult());
                countDownLatch = new CountDownLatch(1);
                firestore.updateUserDataBase(user,countDownLatch);
                countDownLatch.await();
                if (firestore.getInputResult().getResponse()) {
                    presenter.showMessage("Bienvenido");
                    presenter.insertUserLocal();
                    presenter.nextActivity();}
                else { presenter.showMessage(firestore.getInputResult().getResult()); }
            }
            else { presenter.showMessage(firestore.getInputResult().getResult()); }
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void getEstados(String id, MenuPresenter presenter, GlobalPresenter presenterMaster) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        firestore.getEstados(countDownLatch, id);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (firestore.getInputResult().getResponse()) {
            presenter      .showState(firestore.getEstados());
            System.out.println("SIZE "+firestore.getEstados().size());
        } else {
            presenterMaster.showMessage(firestore.getInputResult().getResult());
        }
    }

    @Override
    public void getMyData(LoginPresenter presenter) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        firestore.getMyData(countDownLatch, this, presenter);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (firestore.getInputResult().getResponse()) {
            presenter.insertUserOwner();
        } else {
            presenterMaster.showMessage(firestore.getInputResult().getResult());
        }
    }

    @Override
    public void searchChat(Chat chat, MessagePresenter messagePresenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.searchChat(chat,countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }
        //si encontro que este usuario ya habia hablado con este usuario obtener el id de ese chat
        if (firestore.getInputResult().getResponse()){
            chat.setIdChat(firestore.getInputResult().getResult());
            messagePresenter.getMessages(chat);
        }else{
            firestore.listenerStatusUser(chat.getUsers()[1],messagePresenter);
        }
    }

    @Override
    public void createChat(Chat chat, User friend, Message message, MessagePresenter messagePresenter) {
        countDownLatch = new CountDownLatch(1);
        firestore.createChat(chat,countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }
        if (firestore.getInputResult().getResponse()){
            chat.setIdChat(firestore.getInputResult().getResult());
            messagePresenter.insertFriendLocal(friend, chat.getIdChat());
            messagePresenter.getMessages(chat);
            messagePresenter.sendMessages(chat.getIdChat(),message);
        }else{
            System.out.println("NO CREADO");

        }
    }

    @Override
    public void getMessages(MessagePresenter presenter, Chat chat) {
        firestore.getMessagesByIdChat(chat, list ->
                presenter.showMessages(firestore.getMensajes(), chat.getIdChat() ),presenter );
        firestore.listenerStatusUser(chat.getUsers()[1],presenter);
    }

    @Override
    public void getAllUsers(ContactsPresenter contactsPresenter) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        firestore.getAllUsers(countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (firestore.getInputResult().getResponse()) {
            contactsPresenter.showContacts(firestore.getUsers());
        } else {

        }
    }

    @Override
    public void sendMessages(String idChat, Message message) {
      firestore.sendMessages(idChat, message);
    }

    @Override
    public void getEstadoUser(String idUser) {

    }

    @Override
    public void cancelListener() {firestore.cancelListener(); }

    @Override
    public void updateEstado(String idUser, Estado_User estado_user) {
        firestore.updateEstado(idUser,estado_user);
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
            User friend = new Gson().fromJson(firestore.getInputResult().getResult(),User.class) ;
            presenter.showChats(friend);
        } else {

        }

    }
}
