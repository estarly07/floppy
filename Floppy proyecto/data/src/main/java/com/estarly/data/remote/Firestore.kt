package com.estarly.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.estarly.data.Global.GlobalUtils
import com.estarly.data.Global.InputResult
import com.google.android.gms.tasks.Task
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.ArrayList
import java.util.concurrent.CountDownLatch

class Firestore (val context:Context) : ConnectionFirestore {

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth             : FirebaseAuth      = FirebaseAuth.getInstance()
    private val inputResult      : InputResult       = InputResult()
    private val gson             = Gson()
    private val storageReference = FirebaseStorage.getInstance().reference

    /**
     * LOGUEAR EL USUARIO
     *
     * @param data data[0] => email
     *             data[1] => password
     * */
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

    /**
     * REGISTRAR EL USUARIO
     *
     * @param data data[0] => email
     *             data[1] => password
     * */
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
                    val arrayList = snapshot.data!!["states"] as ArrayList<Map<String, String>>?
                    val state = StringBuilder()

                    state.append("[")
                    if (arrayList != null) {
                        state.append("[")
                        for (map in arrayList) {
                            state.append("{")
                            state.append("'fecha_final' :'${map["fecha_final"]}',")
                            state.append("'like' :'${map["like"]}',")
                            state.append("'imagen' :'${map["imagen"]}',")
                            state.append("'nameUser' :'Estarly',")
                            state.append("'fecha_inicial' :'${map["fecha_inicial"]}',")
                            state.append("'id' :'${map["id"]}',")
                            state.append("'mensaje' :'${map["mensaje"]}'")
                            state.append("}")
                            println(state.toString())
                        }
                        state.append("]")
                        state.append("]")
                        inputResult.response = true
                        inputResult.result = state.toString()
                    } else {
                        inputResult.response = false
                    }
                    countDownLatch.countDown()
                }
            }
    }

    override fun getMyData(countDownLatch: CountDownLatch, idUser: String) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(idUser).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    inputResult.response = true
                    inputResult.result = gson.toJson(snapshot!!.data)
                    countDownLatch.countDown()
                }
            }
    }

    /**BUSCAR UN CHAT
     *@param data data[0]=>id del owner
     *            data[1]=>id del friend
     * */
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

    override fun createChat(map: MutableMap<String, Any>, countDownLatch: CountDownLatch) {
        val id = firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document().id
        map.put("idChat",id)
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document(id).set(map)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    inputResult.response = true
                    inputResult.result   = id
                } else {
                    inputResult.response = false
                }
                countDownLatch.countDown()
            }

    }

    override fun getAllUsers(countDownLatch: CountDownLatch, idUser: String) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0])
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                val json = StringBuilder()
                json.append("[")
                if (task.isSuccessful) {
                    var index=0
                    for (document in task.result!!) {
                        /**PARA SOLO OBTENER CONTACTOS DISTINTOS A MI */
                        if (idUser != document["idUser"]) {
                            if(index >0 && task.result!!.size() >1 && index < task.result!!.size()-1 ) { json.append(',')}

                            json.append("{")
                            json.append("'idUser' :'${document.data["idUser"].toString()}',")
                            json.append("'name' :'${ document.data["name"].toString()}',")
                            json.append("'pass' :'${document.data["pass"].toString()}',")
                            json.append("'email' :'${document.data["email"].toString()}',")
                            json.append("'photo' :'${ document.data["photo"].toString()}',")
                            json.append("'messageUser' :'${ document.data["messageUser"].toString()}',")
                            json.append("'estado_user' :'${document.data["estado_user"].toString()}'")
                            json.append("}")
                            index++
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.exception)
                }
                json.append("]")
                inputResult.response = true
                inputResult.result = json.toString()
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
            println("IG $idChat ")
            listenerChat = firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document(idChat)
                .addSnapshotListener(initListenerChat(callbackChat))
            listenerStatusUser(idFriend, callbackStatus)
        }
    }
    override fun listenerStatusUser(idFriend: String, callback: (String) -> Unit) {
        listenerStateFriend = firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(
            idFriend
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

    /**OBTENER LOS AMIGOS QUE SE GUARDARON COMO CONTACTOS**/
    override fun getFriends(
        countDownLatch: CountDownLatch, idFriend: String?
    ) {
        firebaseFirestore.collection(GlobalUtils.COLLECTIONS[0]).document(
            idFriend!!
        ).get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val stringBuilder = StringBuilder()
                stringBuilder.append("{")
                stringBuilder.append("'idUser' :  '${snapshot!!["idUser"].toString()}',")
                stringBuilder.append("'name' :  '${snapshot["name"].toString()}',")
                stringBuilder.append("'pass' :  '${snapshot["pass"].toString()}',")
                stringBuilder.append("'email' :  '${snapshot["email"].toString()}',")
                stringBuilder.append("'photo' :  '${snapshot["photo"].toString()}',")
                stringBuilder.append("'messageUser' :  '${snapshot["messageUser"].toString()}',")
                stringBuilder.append("'estado_user' :  '${snapshot["estado_user"].toString()}'")
                stringBuilder.append("}")
                inputResult.response = true
                inputResult.result   = stringBuilder.toString()
                countDownLatch.countDown()
            }else{
                inputResult.response = false
                countDownLatch.countDown()
            }
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
        idChat: String,
        callback: (String) -> Unit
    ) {
        listenersfriends.add(
            firebaseFirestore.collection(GlobalUtils.COLLECTIONS[2]).document(idChat)
                .addSnapshotListener { value: DocumentSnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error)
                        return@addSnapshotListener
                    }
                    if (value != null && value.exists()) {

                        val json = value.data!!.get("mensajes").toString();
                        val type = object : TypeToken<List<Map<String,Any>>>(){}.type
                        val  messagesList :List<Map<String,Any>> = Gson().fromJson(json,type);

                        val stringBuilder = StringBuilder()
                        stringBuilder.append("{")
                        stringBuilder.append("'idMessage': '${messagesList[0]["idMessage"]}',")
                        stringBuilder.append("'message': '${messagesList[0]["message"]}',")
                        stringBuilder.append("'user': '${messagesList[0]["user"]}',")
                        stringBuilder.append("'state': '${messagesList[0]["state"]}',")

                        stringBuilder.append("'hora': '${messagesList[0]["hora"]}',")
                        stringBuilder.append("'typeMessage': '${messagesList[0]["typeMessage"]}'")

                        stringBuilder.append("}")

                        callback.invoke(stringBuilder.toString())
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
                val file = File(context.getExternalFilesDir(null), "/${GlobalUtils.TypeFile.AUDIO.dir}/")
                if (!file.exists()) {
                    file.mkdirs()
                }
                localFile = File(context.getExternalFilesDir(null), "/${GlobalUtils.TypeFile.AUDIO.dir}/$nameFile.mp3")
                storageReference.child("${GlobalUtils.TypeFile.AUDIO.dir}/$nameFile.mp3").getFile(localFile)
                    .addOnSuccessListener {
                        println(
                            "LIUSTOOOO"
                        )
                    }
                    .addOnFailureListener { exception: Exception -> println("ERROR " + exception.message) }
            }
            "IMAGE"->{
                val file = File(context.getExternalFilesDir(null), "/${GlobalUtils.TypeFile.IMAGE.dir}")
                if (!file.exists()) {
                    file.mkdirs()
                }
                localFile = File(file.path, nameFile)
                storageReference.child("${GlobalUtils.TypeFile.IMAGE.dir}/$nameFile").getFile(localFile)
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

    fun savedFile(uri: Uri, dir: GlobalUtils.TypeFile, countDownLatch: CountDownLatch) {
//        storageReference.child("audios/" + uri.lastPathSegment).putFile(uri)
        storageReference.child("${dir.dir}/${uri.lastPathSegment}").putFile(uri)
            .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot?> ->
                inputResult.response = task.isSuccessful
                countDownLatch.countDown()
            }
    }

}