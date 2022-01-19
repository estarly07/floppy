package com.estarly.data.remote

import android.content.Context
import android.net.Uri
import android.util.Log
import com.estarly.data.Global.GlobalUtils
import com.estarly.data.Global.InputResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.lang.Exception
import java.util.ArrayList
import java.util.concurrent.CountDownLatch

class Firestore (val context:Context) : ConnectionFirestore {

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth             : FirebaseAuth      = FirebaseAuth.getInstance()
    private val inputResult      : InputResult       = InputResult()
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun login(data: Array<String>, countDownLatch: CountDownLatch) {
        auth.signInWithEmailAndPassword(data[0], data[1])
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    inputResult.result = "" + auth.currentUser!!.uid
                    inputResult.response = true
                } else {
                    inputResult.response = false
                }
                countDownLatch.countDown()
            }
    }

    override fun registerUserDataBase(data: Array<String>, countDownLatch: CountDownLatch) {
        auth.createUserWithEmailAndPassword(data[0], data[1])
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    inputResult.result = "" + auth.currentUser!!.uid
                    inputResult.response = true
                } else {
                    inputResult.response = false
                }
                countDownLatch.countDown()
            }
    }

    override fun updateUserDataBase(
        map: Map<String, Any>,
        idUser: String,
        countDownLatch: CountDownLatch
    ) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(idUser).set(map)
            .addOnCompleteListener { task: Task<Void?> ->
                inputResult.response = task.isSuccessful
                countDownLatch.countDown()
            }
    }

    override fun getStates(countDownLatch: CountDownLatch, id: String) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[1]).document(id).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    println("BODY " + snapshot!!.data!!["states"])
                    inputResult.response = true
                    inputResult.result = snapshot.data!!["states"].toString()
                } else {
                    inputResult.response = false
                }
                countDownLatch.countDown()
            }
    }

    override fun getMyData(countDownLatch: CountDownLatch, idUser: String) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(idUser).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    inputResult.response = true
                    inputResult.result = snapshot!!.data.toString()
                    countDownLatch.countDown()
                }
            }
    }

    override fun searchChat(data: Array<String>, countDownLatch: CountDownLatch) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).whereArrayContains(
            "users",
            data[0]
        ).get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val users =
                        document["users"] as ArrayList<*>?
                    for (idFriend in users!!) {
                        if (idFriend == data[1]) {
                            inputResult.response = true
                            inputResult.result = document["idChat"] as String?
                            countDownLatch.countDown()
                            return@addOnCompleteListener
                        }
                    }
                }
                inputResult.response = false
                countDownLatch.countDown()
            } else {
                println("ERROR")
            }
        }
    }

    override fun createChat(map: Map<String, Any>, countDownLatch: CountDownLatch) {
        val id = firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document().id
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document(id).set(map)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    inputResult.response = true
                    inputResult.result = id
                } else {
                    inputResult.response = false
                }
                countDownLatch.countDown()
            }

    }

    override fun getAllUsers(countDownLatch: CountDownLatch, idUser: String) {
        val users = ArrayList<String>()
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0])
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (idUser != document["idUser"]) {
                            /**PARA SOLO OBTENER CONTACTOS DISTINTOS A MI */
                            users.add(document.data.toString())
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.exception)
                }
                inputResult.response = true
                inputResult.result = users.toString()
                countDownLatch.countDown()
            }
    }
    var listenerChat: ListenerRegistration? = null
    var listenerStateFriend: ListenerRegistration? = null
    override fun getMessagesByIdChat(
        idChat: String,
        idFriend: String,
        callbackChat: (String) -> Unit,
        callbackStatus: (String) -> Unit
    ) {
        if (listenerChat == null) {
            listenerChat = firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document(idChat)
                .addSnapshotListener(initListenerChat(callbackChat))
            listenerStatusUser(idFriend, callbackStatus)
        }
    }
    override fun listenerStatusUser(idFriend: String?, callback: (String) -> Unit) {
        listenerStateFriend = firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(
            idFriend!!
        ).addSnapshotListener { value: DocumentSnapshot?, _: FirebaseFirestoreException? ->
            if (value!!.exists()) {
                callback.invoke(value.data!!["estado_user"] as String)
            }
        }
    }

    private fun initListenerChat(callback: (String)->Unit): EventListener<DocumentSnapshot> {
        return  EventListener { value: DocumentSnapshot?, error: FirebaseFirestoreException? ->
            if (error != null) {
                Log.w("TAG", "Listen failed.", error)
                return@EventListener
            }
            if (value != null && value.exists()) {
                if (value.exists()) {
                    val json = value.data!!["mensajes"].toString()

                    inputResult.response = true
                    callback.invoke(json)
                }
            } else {
                Log.d("TAG", "Current data: null")
            }
        }
    }


    override fun sendMessages(idChat: String, conversation: String) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document(idChat)
            .update("mensajes", conversation)
    }

    override fun cancelListener() {
        listenerChat?.remove()
        listenerStateFriend?.remove()
    }

    override fun updateState(idUser: String, estado_user: String) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(idUser)
            .update("estado_user", estado_user)
    }

    override fun getFriends(
        countDownLatch: CountDownLatch, idFriend: String?
    ) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(
            idFriend!!
        ).get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
            if (task.isSuccessful) {
                val snapshot = task.result

                inputResult.response = true
                inputResult.result = snapshot?.data.toString()
                countDownLatch.countDown()
            }
            inputResult.response = false
            countDownLatch.countDown()
        }
    }



    /**
     * GETTERS
     */
    fun getInputResult(): InputResult {
        return inputResult
    }

    var listenersfriends = ArrayList<ListenerRegistration>()

    /**escuchar los chats para mostrar el último mensaje en el menu */
    override fun listenerChatFriend(
        friendEntity: Map<String,Any>,
        callback: (Map<String,Any>,String)->Unit
    ) {
        listenersfriends.add(
            firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document(friendEntity["idChat"] as String)
                .addSnapshotListener { value: DocumentSnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error)
                        return@addSnapshotListener
                    }
                    if (value != null && value.exists()) {
                        callback.invoke(friendEntity,value.data!!["mensajes"].toString())
                    } else {
                        Log.d("TAG", "Current data: null")
                    }
                })
    }

    /**DESCARGAR ARCHIVOS DE STORAGE DE FIREBASE
     * @param nameFile    nombre del archivo
     * @param typeMessage tipo de archivo
     */
    override fun downloadFile(nameFile: String, typeMessage: String) {
        val localFile: File
        when (typeMessage) {
            "RECORD" -> {
                val file = File(context.getExternalFilesDir(null), "/audios/")
                if (!file.exists()) {
                    file.mkdirs()
                }
                localFile = File(context.getExternalFilesDir(null), "/audios/$nameFile.mp3")
                storageReference.child("audios/$nameFile.mp3").getFile(localFile)
                    .addOnSuccessListener {
                        println(
                            "LIUSTOOOO"
                        )
                    }
                    .addOnFailureListener { exception: Exception -> println("ERROR " + exception.message) }
            }
        }
    }

    fun destroyAllListenersFriends() {
        if (listenersfriends.isNotEmpty()) {
            for (listenerRegistration in listenersfriends) {
                listenerRegistration.remove()
            }
        }
    }

    fun savedAudio(uri: Uri, countDownLatch: CountDownLatch) {
        storageReference.child("audios/" + uri.lastPathSegment).putFile(uri)
            .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot?> ->
                inputResult.response = task.isSuccessful
                countDownLatch.countDown()
            }
    }

}