package com.example.floppy.data.Conexion.remote;

import com.example.floppy.Callbacks.CallBackObjects;
import com.example.floppy.Callbacks.CallbackList;
import com.example.floppy.domain.remote.Interactor;
import com.example.floppy.domain.models.Chat;
import com.example.floppy.domain.models.Estado_User;
import com.example.floppy.domain.models.Message;
import com.example.floppy.domain.models.User;
import com.example.floppy.ui.login.LoginPresenter;
import com.example.floppy.ui.message.MessagePresenter;

import java.util.concurrent.CountDownLatch;

public interface ConnectionFirestore {

    void login(User user, CountDownLatch countDownLatch);

    void registerUserDataBase(User user, CountDownLatch countDownLatch);

    void updateUserDataBase(User user, CountDownLatch countDownLatch);

    void saveFoto(byte[] img, User user, CountDownLatch countDownLatch);

    void updateUser(User user, CountDownLatch countDownLatch);

    void getStates(CountDownLatch countDownLatch, String id);

    void getMyData(CountDownLatch countDownLatch, Interactor interactor, LoginPresenter presenter);

    void searchChat(Chat chat, CountDownLatch countDownLatch);

    void createChat(Chat chat, CountDownLatch countDownLatch);

    void getAllUsers(CountDownLatch countDownLatch);

    void getMessagesByIdChat(Chat chat, CallbackList<Message> callback, MessagePresenter presenter);

    void sendMessages(String idChat, String conversation,String date);

    void cancelListener();

    void updateState(String idUser, Estado_User estado_user);

}
