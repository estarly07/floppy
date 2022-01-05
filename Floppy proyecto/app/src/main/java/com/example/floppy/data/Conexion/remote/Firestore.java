package com.example.floppy.data.Conexion.remote;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.floppy.Callbacks.CallBackObjects;
import com.example.floppy.Callbacks.CallbackList;
import com.example.floppy.Domain.Entitys.FriendEntity;
import com.example.floppy.Domain.remote.InteractorFirestoreImpl;
import com.example.floppy.ui.login.LoginPresenter;
import com.example.floppy.ui.menu.MenuPresenter;
import com.example.floppy.ui.message.MessagePresenter;
import com.example.floppy.utils.Global.GlobalUtils;
import com.example.floppy.Domain.remote.Interactor;
import com.example.floppy.Domain.Models.Chat;
import com.example.floppy.Domain.Models.Estado;
import com.example.floppy.Domain.Models.Estado_User;
import com.example.floppy.Domain.Models.InputResult;
import com.example.floppy.Domain.Models.Message;
import com.example.floppy.Domain.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class Firestore extends GlobalUtils implements ConnectionFirestore {
    private FirebaseFirestore firebaseFirestore;
    private String idVerificacion;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private Activity activity;
    private InputResult inputResult;
    private PhoneAuthCredential phoneAuthCredential;
    private ArrayList<ArrayList<Estado>> estados;
    private ArrayList<User> users;
    final private FirebaseAuth auth = FirebaseAuth.getInstance();

    public ArrayList<Message> getMensajes() {
        return messages;
    }

    private ArrayList<Message> messages = new ArrayList<>();
    private User user;

    public Firestore(Context context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        inputResult = new InputResult();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }


    @Override
    public void login(User user, CountDownLatch countDownLatch) {
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPass()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                inputResult.setResult(""+auth.getCurrentUser().getUid());
                inputResult.setResponse(true);
            } else {
                inputResult.setResponse(false);
                inputResult.setResult("Usuario no existe");
            }
            countDownLatch.countDown();
        });
    }

    @Override
    public void registerUserDataBase(User user, CountDownLatch countDownLatch) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPass()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                inputResult.setResult(""+auth.getCurrentUser().getUid());
                inputResult.setResponse(true);
            } else {
                inputResult.setResponse(false);
                inputResult.setResult("No se pudo registrar\n(Puede que ya exista ese Email)");
            }
            countDownLatch.countDown();
        });
    }

    @Override
    public void updateUserDataBase(User user, CountDownLatch countDownLatch) {
        Map<String, Object> map = new HashMap();
        map.put("idUser"        , user.getIdUser());
        map.put("name"          , user.getName());
        map.put("pass"          , user.getPass());
        map.put("email"         , user.getEmail());
        map.put("photo"         , user.getPhoto());
        map.put("messageUser"   , user.getMessageUser());
        map.put("estado_user"   , user.getEstado_user());
        map.put("friendEntities", user.getFriends());

        firebaseFirestore.collection(COLLECTIONS[0]).document(user.getIdUser()).set(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                inputResult.setResponse(true);
            } else {
                inputResult.setResponse(false);
                inputResult.setResult("No se pudo registrar los datos");
            }
            countDownLatch.countDown();
        });
    }

    @Override
    public void saveFoto(byte[] img, User user, CountDownLatch countDownLatch) {

    }

    @Override
    public void updateUser(User user, CountDownLatch countDownLatch) {

    }

    @Override
    public void getEstados(CountDownLatch countDownLatch, String id) {
        firebaseFirestore.collection(COLLECTIONS[1]).document(id).get().addOnCompleteListener(task -> {
            estados = new ArrayList<>();
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                System.out.println("BODY " + snapshot.getData().get("states"));
                ArrayList<Map<String, String>> arrayList = (ArrayList<Map<String, String>>) snapshot.getData().get("states");
                ArrayList<Estado> listEstados = new ArrayList<>();
                if (arrayList != null)
                    for (Map<String, String> map :
                            arrayList) {
                        Estado estado = new Estado();
                        estado.setNameUser("Estarly");
                        estado.setFecha_final(map.get("fecha_final"));
                        estado.setFecha_inicial(map.get("fecha_inicial"));
                        estado.setId(map.get("id"));
                        estado.setImagen(map.get("imagen"));
                        estado.setLike((Integer.parseInt(map.get("like"))));
                        estado.setMensaje(map.get("mensaje"));
                        listEstados.add(estado);
                    }
                estados.add(listEstados);
                inputResult.setResponse(true);

                countDownLatch.countDown();

            } else {
                inputResult.setResponse(false);
                inputResult.setResult("Hubo un error \nal obtener los estados");
                countDownLatch.countDown();
            }
        });
    }

    @Override
    public void getMyData(CountDownLatch countDownLatch, Interactor interactor, LoginPresenter presenter) {
        user = User.getInstance();
        firebaseFirestore.collection(COLLECTIONS[0]). document(user.getIdUser()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                user.setName(snapshot.get("name").toString());
                user.setMessageUser(snapshot.get("messageUser").toString());
                user.setEstado_user(Estado_User.valueOf(snapshot.get("estado_user").toString()));
                user.setEmail(snapshot.get("email").toString());
                user.setPhoto(snapshot.get("photo").toString());

//                if (getFriends) {
//                    getFriends(countDownLatch, user.getIdUser(), interactor, presenter);
//                } else {
                inputResult.setResponse(true);
                countDownLatch.countDown();
//                }
            }
        });

    }

    @Override
    public void getChat(String idChat, Interactor interactor) {

    }

    @Override
    public void searchChat(Chat chat, CountDownLatch countDownLatch) {
        firebaseFirestore.collection(COLLECTIONS[2]).whereArrayContains("users",chat.getUsers()[0]).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ArrayList<String> users= (ArrayList<String>) document.get("users");
                    for (String idFriend: users) {
                        if (idFriend.equals(chat.getUsers()[1])){
                            inputResult.setResponse(true);
                            inputResult.setResult((String) document.get("idChat"));
                            countDownLatch.countDown();
                            return;
                        }
                    }
                }
                inputResult.setResponse(false);
                countDownLatch.countDown();
            }else{
                System.out.println("ERROR");
            }
        });

    }

    @Override
    public void createChat(Chat chat, CountDownLatch countDownLatch) {

        String id = firebaseFirestore.collection(COLLECTIONS[2]).document().getId();
        Map<String, Object> map = new HashMap<>();
        map.put("idChat",id);
        map.put("mensajes", Arrays.asList(chat.getMensajes()) );
        map.put("users", Arrays.asList(chat.getUsers()));
        firebaseFirestore.collection(COLLECTIONS[2]).document(id).set(map).addOnCompleteListener(task ->
        {
            if(task.isSuccessful()){
                inputResult.setResponse(true);
                inputResult.setResult(id);
            }else{
                inputResult.setResponse(false);
            }
            countDownLatch.countDown();
        });

    }

    @Override
    public void getAllUsers(CountDownLatch countDownLatch) {
        User user = User.getInstance();
        firebaseFirestore.collection(COLLECTIONS[0])
                .get()
                .addOnCompleteListener(task -> {
                    users = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!user.getIdUser().equals(document.get("idUser"))) {/**PARA SOLO OBTENER CONTACTOS DISTINTOS A MI*/
                            User newUser = new User(
                                document.getData().get("idUser").toString(),
                                document.getData().get("name").toString(),
                                document.getData().get("pass").toString(),
                                document.getData().get("email").toString(),
                                document.getData().get("photo").toString(),
                                document.getData().get("messageUser").toString(),
                                Estado_User.valueOf(document.getData().get("estado_user").toString())
                            );

                            users.add(newUser);
                            }
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                    inputResult.setResponse(true);
                    countDownLatch.countDown();
                });

    }

    @Override
    public void addFriend(User user, String myId, CountDownLatch countDownLatch) {

    }

    ListenerRegistration listenerChat = null;
    ListenerRegistration listenerStateFriend = null;

    @Override
    public void getMessagesByIdChat(Chat chat, CallbackList<Message> callback, MessagePresenter presenter) {
        if (listenerChat == null) {
            listenerChat = firebaseFirestore.collection(COLLECTIONS[2]).document(chat.getIdChat()).addSnapshotListener(initListenerChat(callback));
            listenerStatusUser(chat.getUsers()[1],presenter);
        }
    }
    public void listenerStatusUser(String idFriend,MessagePresenter messagePresenter){
        listenerStateFriend = firebaseFirestore.collection(COLLECTIONS[0]).document(idFriend).addSnapshotListener((value, error) -> {
            if (value.exists()) {
                messagePresenter.showStateUser((String) value.getData().get("estado_user"));
            }
        });
    }

    private EventListener<DocumentSnapshot> initListenerChat(CallbackList<Message> callback) {
        return (value, error) -> {
            if (error != null) {
                Log.w("TAG", "Listen failed.", error);
                return;
            }

            if (value != null && value.exists()) {
                messages = new ArrayList<>();
                if (value.exists()) {
                    System.out.println("DATQA " + value.getData());
                    ArrayList<Map<String, Object>> messages = (ArrayList<Map<String, Object>>) value.getData().get("mensajes");
                    if (messages == null || messages.isEmpty()) {
                        callback.showList(this.messages);
                        return;
                    }
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        Message message = new Message();
                        message.setMessage(messages.get(i).get("mensage").toString());
                        message.setUser(messages.get(i).get("user").toString());
                        message.setDate(messages.get(i).get("hora").toString());
                        message.setTypeMessage(Message.TypesMessages.valueOf(messages.get(i).get("typeMessage").toString()));
                        this.messages.add(message);
                    }

                    inputResult.setResponse(true);
                    callback.showList(this.messages);
                }
            } else {
                Log.d("TAG", "Current data: null");
            }
        };

    }

    @Override
    public void sendMessages(String idChat, Message message, String date) {
        Map<String, String> map = new HashMap<>();
        map.put("idMensage", UUID.randomUUID().toString());
        map.put("user", message.getUser());
        map.put("mensage", message.getMessage());
        map.put("hora", message.getHora());
        map.put("typeMessage", String.valueOf(message.getTypeMessage()));

        firebaseFirestore.collection(COLLECTIONS[2]).document(idChat).update("lastMessage", date);
        firebaseFirestore.collection(COLLECTIONS[2]).document(idChat).update("mensajes", FieldValue.arrayUnion(map));
    }

    @Override
    public void cancelListener() {
        if (listenerChat != null) { listenerChat.remove(); }

        if (listenerStateFriend != null) { listenerStateFriend.remove(); }
    }

    @Override
    public void getEstadoUser(String idUser, CallBackObjects<String> callBackObjects) {

    }

    @Override
    public void updateEstado(String idUser, Estado_User estado_user) {
        firebaseFirestore.collection(COLLECTIONS[0]).document(idUser).update("estado_user", estado_user.toString());
    }


    User friend;
    public void getFriends(
            CountDownLatch countDownLatch, String idFriend
    ) {
        firebaseFirestore.collection(COLLECTIONS[0]).document(idFriend).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                 friend = new User(
                    snapshot.get("idUser").toString(),
                    snapshot.get("name").toString(),
                    snapshot.get("pass").toString(),
                    snapshot.get("email").toString(),
                    snapshot.get("photo").toString(),
                    snapshot.get("messageUser").toString(),
                    Estado_User.valueOf(snapshot.get("estado_user").toString())
                );

                inputResult.setResponse(true);
                inputResult.setResult(new Gson().toJson(friend));
                countDownLatch.countDown();
                return;
            }
            inputResult.setResponse(false);
            countDownLatch.countDown();
        });
    }

    /**
     * SETTERS
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * GETTRS
     */
    public InputResult getInputResult() {
        return inputResult;
    }

    public ArrayList<ArrayList<Estado>> getEstados() {
        return estados;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUser() {
        return user;
    }

    ArrayList<ListenerRegistration> listenersfriends = new ArrayList<>();
    public void listenerChatFriend(FriendEntity friendEntity, InteractorFirestoreImpl interactorFirestore, MenuPresenter menuPresenter) {
        listenersfriends.add(firebaseFirestore.collection(COLLECTIONS[2]).document(friendEntity.idChat).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("TAG", "Listen failed.", error);
                return;
            }

            if (value != null && value.exists()) {
                ArrayList<Map<String, Object>> messages = (ArrayList<Map<String, Object>>) value.getData().get("mensajes");
                Message message = new Message();
                message.setMessage(messages.get(messages.size() - 1).get("mensage").toString());
                message.setUser(messages.get(messages.size() - 1).get("user").toString());
                message.setHora(messages.get(messages.size() - 1).get("hora").toString());
                message.setTypeMessage(Message.TypesMessages.valueOf(messages.get(messages.size() - 1).get("typeMessage").toString()));
                System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAA "+listenersfriends.size()+ " "+message.getTypeMessage().toString());
                interactorFirestore.friendIsWriting(friendEntity, menuPresenter, message);
            } else {
                Log.d("TAG", "Current data: null");
            }
        }));
    }

    public void destroyAllListenersFriends(){
        if(!listenersfriends.isEmpty()){
            for (ListenerRegistration listenerRegistration:
                 listenersfriends) {
                listenerRegistration.remove();
            }
        }
    }
}
