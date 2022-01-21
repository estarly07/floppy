package com.estarly.data.remote

import java.util.concurrent.CountDownLatch

interface ConnectionFirestore {
    fun login(data: Array<String>, countDownLatch: CountDownLatch)

    fun registerUserDataBase(data: Array<String>, countDownLatch: CountDownLatch)

    fun updateUserDataBase(
        map: Map<String, Any>,
        idUser: String,
        countDownLatch: CountDownLatch
    )

//    void saveFoto(byte[] img, User user, CountDownLatch countDownLatch);
//
//    void updateUser(User user, CountDownLatch countDownLatch);

    //    void saveFoto(byte[] img, User user, CountDownLatch countDownLatch);
    //
    //    void updateUser(User user, CountDownLatch countDownLatch);
    fun getStates(countDownLatch: CountDownLatch, id: String)

    fun getMyData(countDownLatch: CountDownLatch, idUser: String)

    fun searchChat(data: Array<String>, countDownLatch: CountDownLatch)

    fun createChat(map: Map<String, Any>, countDownLatch: CountDownLatch)

    fun getAllUsers(countDownLatch: CountDownLatch, idUser: String)

    fun getMessagesByIdChat(idChat: String, idFriend: String,callbackChat: (String)->Unit,callbackStatus: (String)->Unit)

    fun sendMessages(idChat: String, conversation: String)

    fun cancelListener()

    fun updateState(idUser: String, estado_user: String)

    fun getFriends(countDownLatch: CountDownLatch, idFriend: String?)

    fun listenerStatusUser(idFriend: String?, callback: (String) -> Unit)

    fun listenerChatFriend(
        idChat: String,
        callback: (String)->Unit
    )

    fun downloadFile(nameFile: String, typeMessage: String)
}