package com.example.floppy.data.Conexion.remote;

import com.example.floppy.Callbacks.CallBackObjects;
import com.example.floppy.Callbacks.CallbackList;
import com.example.floppy.Domain.remote.Interactor;
import com.example.floppy.Domain.Models.Chat;
import com.example.floppy.Domain.Models.Estado_User;
import com.example.floppy.Domain.Models.Message;
import com.example.floppy.Domain.Models.User;
import com.example.floppy.ui.login.LoginPresenter;
import com.example.floppy.ui.message.MessagePresenter;

import java.util.concurrent.CountDownLatch;

public interface ConnectionFirestore {

    void login(User user, CountDownLatch countDownLatch);

    void registerUserDataBase(User user, CountDownLatch countDownLatch);

    void updateUserDataBase(User user, CountDownLatch countDownLatch);

    void saveFoto(byte[] img, User user, CountDownLatch countDownLatch);

    void updateUser(User user, CountDownLatch countDownLatch);

    void getEstados(CountDownLatch countDownLatch, String id);

    void getMyData(CountDownLatch countDownLatch, Interactor interactor, LoginPresenter presenter);

    void getChat(String idChat, Interactor interactor);

    void searchChat(Chat chat, CountDownLatch countDownLatch);

    void createChat(Chat chat, CountDownLatch countDownLatch);

    void getAllUsers(CountDownLatch countDownLatch);

    void addFriend(User user, String myId, CountDownLatch countDownLatch);

    void getMessagesByIdChat(Chat chat, CallbackList<Message> callback, MessagePresenter presenter);

    void sendMessages(String idChat, Message message,String date);

    void cancelListener();

    void getEstadoUser(String idUser, CallBackObjects<String> callBackObjects);

    void updateEstado(String idUser, Estado_User estado_user);

}
